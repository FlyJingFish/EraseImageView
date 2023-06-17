package com.flyjingfish.searchanimviewlib;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class EraseImageView extends AppCompatImageView {
    public static final int INFINITE = ValueAnimator.INFINITE;
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    private final Path mErasePath = new Path();
    private final Path mClipPath = new Path();
    private final Rect mEraseIconBounds = new Rect();
    private final RectF mEraseIconPercent = new RectF();
    private final RectF mEraseIconSubRectF = new RectF();
    private float mEraseRadius;
    private ValueAnimator mEraseAnim;
    private Drawable mEraseIcon;
    private DrawPathType mDrawPathType;
    private float mAnimPaddingLeft;
    private float mAnimPaddingTop;
    private float mAnimPaddingRight;
    private float mAnimPaddingBottom;
    private float mAnimPaddingStart;
    private float mAnimPaddingEnd;
    private BaseAnim mBaseAnim;
    private int mRepeatCount;
    private int mRepeatMode;
    private TimeInterpolator mInterpolator = new LinearInterpolator();
    private long mDuration;
    private boolean mAutoStart;
    private boolean mEraseMode;
    private boolean mHandMode;
    private boolean mShowEraseIcon;
    private final Paint mErasePaint;
    private final PorterDuffXfermode mDstOutXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }

    public EraseImageView(@NonNull Context context) {
        this(context, null);
    }

    public EraseImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EraseImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EraseView);
        int drawPathTypeInt = a.getInt(R.styleable.EraseView_FlyJFish_erase_DrawPathType, 0);
        mEraseIcon = a.getDrawable(R.styleable.EraseView_FlyJFish_erase_resource);
        float percentLeft = a.getFloat(R.styleable.EraseView_FlyJFish_erase_resource_percentLeft, 0);
        float percentTop = a.getFloat(R.styleable.EraseView_FlyJFish_erase_resource_percentTop, 0);
        float percentRight = a.getFloat(R.styleable.EraseView_FlyJFish_erase_resource_percentRight, 0);
        float percentBottom = a.getFloat(R.styleable.EraseView_FlyJFish_erase_resource_percentBottom, 0);
        mAnimPaddingLeft = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingLeft, 0);
        mAnimPaddingTop = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingTop, 0);
        mAnimPaddingRight = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingRight, 0);
        mAnimPaddingBottom = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingBottom, 0);
        mAnimPaddingStart = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingStart, 0);
        mAnimPaddingEnd = a.getDimension(R.styleable.EraseView_FlyJFish_erase_paddingEnd, 0);
        mEraseRadius = a.getDimension(R.styleable.EraseView_FlyJFish_erase_radius, 30);

        mDuration = a.getInteger(R.styleable.EraseView_FlyJFish_erase_duration, 1000);
        mRepeatCount = a.getInteger(R.styleable.EraseView_FlyJFish_erase_repeatCount, 0);
        mRepeatMode = a.getInt(R.styleable.EraseView_FlyJFish_erase_repeatMode, RESTART);
        mAutoStart = a.getBoolean(R.styleable.EraseView_FlyJFish_erase_autoStart, false);
        mEraseMode = a.getBoolean(R.styleable.EraseView_FlyJFish_erase_eraseMode, false);
        mHandMode = a.getBoolean(R.styleable.EraseView_FlyJFish_erase_handMode, false);
        a.recycle();

        mDrawPathType = DrawPathType.values()[drawPathTypeInt];
        mErasePath.setFillType(Path.FillType.INVERSE_WINDING);
        mErasePaint = new Paint();
        mErasePaint.setColor(Color.GREEN);
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeJoin(Paint.Join.ROUND);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);
        mErasePaint.setStrokeWidth(mEraseRadius * 2);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mEraseIconPercent.set(percentLeft, percentTop, percentRight, percentBottom);
        mShowEraseIcon = !mHandMode;
        calculateEraseIconSubRectF();

        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (mEraseAnim != null && !mEraseAnim.isStarted()) {
                    mEraseAnim.start();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (mEraseAnim != null) {
                    mEraseAnim.removeAllListeners();
                    mEraseAnim.cancel();
                }
            }
        });

        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                removeOnAttachStateChangeListener(this);
                EnsureFragmentX ensureFragmentX = EnsureFragmentXUtils.ensureInFragmentX(v);
                if (ensureFragmentX.isInFragmentX) {
                    addObserver(ensureFragmentX.lifecycleOwner);
                } else if (getContext() instanceof LifecycleOwner) {
                    addObserver((LifecycleOwner) getContext());
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                removeOnAttachStateChangeListener(this);
            }
        });


    }

    private final LifecycleEventObserver mLifecycleEventObserver = new LifecycleEventObserver() {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (mEraseAnim == null) {
                return;
            }
            if (event == Lifecycle.Event.ON_START && mEraseAnim.isPaused()) {
                mEraseAnim.resume();
            } else if (event == Lifecycle.Event.ON_STOP && mEraseAnim.isRunning()) {
                mEraseAnim.pause();
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                mEraseAnim.cancel();
            }
        }
    };

    private void addObserver(LifecycleOwner owner) {
        if (owner != null) {
            if (owner instanceof Fragment) {
                owner = ((Fragment) owner).getViewLifecycleOwner();
            }
            owner.getLifecycle().removeObserver(mLifecycleEventObserver);
            owner.getLifecycle().addObserver(mLifecycleEventObserver);
        }
    }

    public void setEraseIcon(Drawable drawable, RectF eraseIconPercent) {
        mEraseIcon = drawable;
        this.mEraseIconPercent.set(eraseIconPercent);
        calculateEraseIconSubRectF();
    }

    public float getEraseRadius() {
        return mEraseRadius;
    }

    public void setEraseRadius(float eraseRadius) {
        this.mEraseRadius = eraseRadius;
        calculateEraseIconSubRectF();
        if (mBaseAnim != null) {
            mBaseAnim.setEraseRadius();
        }
    }

    private void calculateEraseIconSubRectF() {
        float width = mEraseRadius * 2 / (mEraseIconPercent.right - mEraseIconPercent.left);
        float height = mEraseRadius * 2 / (mEraseIconPercent.bottom - mEraseIconPercent.top);
        float left = width * mEraseIconPercent.left + mEraseRadius;
        float top = height * mEraseIconPercent.top + mEraseRadius;
        float right = width * (1 - mEraseIconPercent.right) + mEraseRadius;
        float bottom = height * (1 - mEraseIconPercent.bottom) + mEraseRadius;
        mEraseIconSubRectF.set(left, top, right, bottom);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        mErasePaint.setStrokeWidth(mEraseRadius * 2);
        mErasePaint.setXfermode(null);
        canvas.saveLayer(new RectF(0, 0, getWidth(), getHeight()), mErasePaint);

        canvas.save();
        canvas.clipPath(mErasePath);
        super.onDraw(canvas);
        canvas.restore();

        mErasePaint.setXfermode(mDstOutXfermode);
        canvas.drawPath(mClipPath, mErasePaint);

        if (mEraseIcon != null && mShowEraseIcon) {
            mEraseIcon.setBounds(mEraseIconBounds);
            mEraseIcon.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAutoStart && !mHandMode) {
            startEraseAnim();
        }
    }

    private int mCurrentRepeatCount;
    private int mLastRepeatCount;
    private Point mLastPoint;

    public void setErasePoint(Point point,boolean clipPathMovePoint) {
        mErasePath.reset();
        mErasePath.addCircle(point.x, point.y, mEraseRadius, Path.Direction.CW);

        mEraseIconBounds.set((int) (point.x - mEraseIconSubRectF.left), (int) (point.y - mEraseIconSubRectF.top), (int) (point.x + mEraseIconSubRectF.right), (int) (point.y + mEraseIconSubRectF.bottom));

        if (mEraseMode) {
            if (mLastPoint == null || clipPathMovePoint) {
                mClipPath.moveTo(point.x, point.y);
            } else {
                mClipPath.lineTo(point.x, point.y);
            }
            if (mLastRepeatCount != mCurrentRepeatCount) {
                mLastPoint = null;
                mClipPath.reset();
            } else {
                mLastPoint = point;
            }
        }
        mLastRepeatCount = mCurrentRepeatCount;
        invalidate();
    }

    public void resetErasePath() {
        mLastPoint = null;
        mErasePath.reset();
        mClipPath.reset();
        invalidate();
    }

    public void startEraseAnim() {
        stopEraseAnim();
        if (mEraseAnim == null) {
            mEraseAnim = new ValueAnimator();
        }
        mBaseAnim = getAnim();
        mEraseAnim.setObjectValues(new PointParams(new Point(0, 0),false));
        mEraseAnim.setEvaluator(mBaseAnim.getTypeEvaluator());
        mEraseAnim.setDuration(mDuration);
        mEraseAnim.setRepeatMode(mRepeatMode);
        mEraseAnim.setRepeatCount(mRepeatCount);
        mEraseAnim.setInterpolator(mInterpolator);
        mEraseAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                mCurrentRepeatCount++;
            }
        });
        mEraseAnim.addUpdateListener(animation -> {
            PointParams pointParams = (PointParams) animation.getAnimatedValue();
            setErasePoint(pointParams.point,pointParams.clipPathMovePoint);
        });
        mEraseAnim.start();
    }

    public void stopEraseAnim() {
        if (mEraseAnim != null) {
            mEraseAnim.removeAllUpdateListeners();
            mEraseAnim.cancel();
            resetErasePath();
        }
    }

    public void resumeEraseAnim() {
        if (mEraseAnim != null) {
            mEraseAnim.resume();
        } else {
            startEraseAnim();
        }
    }

    public void pauseEraseAnim() {
        if (mEraseAnim != null) {
            mEraseAnim.pause();
        }
    }

    public boolean isPaused() {
        if (mEraseAnim != null){
            return mEraseAnim.isPaused();
        }
        return true;
    }

    private BaseAnim getAnim() {
        BaseAnim baseAnim;
        if (mDrawPathType == DrawPathType.Read) {
            baseAnim = new ReadAnim(this);
        } else if (mDrawPathType == DrawPathType.Circle) {
            baseAnim = new CircleAnim(this);
        } else if (mDrawPathType == DrawPathType.Serpentine) {
            baseAnim = new SerpentineAnim(this);
        } else if (mDrawPathType == DrawPathType.Lightning) {
            baseAnim = new LightningAnim(this);
        } else {
            baseAnim = new CircleAnim(this);
        }
        return baseAnim;
    }

    public float getAnimPaddingLeft() {
        return mAnimPaddingLeft;
    }

    public void setAnimPaddingLeft(float animPaddingLeft) {
        this.mAnimPaddingLeft = animPaddingLeft;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingTop() {
        return mAnimPaddingTop;
    }

    public void setAnimPaddingTop(float animPaddingTop) {
        this.mAnimPaddingTop = animPaddingTop;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingRight() {
        return mAnimPaddingRight;
    }

    public void setAnimPaddingRight(float animPaddingRight) {
        this.mAnimPaddingRight = animPaddingRight;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingBottom() {
        return mAnimPaddingBottom;
    }

    public void setAnimPaddingBottom(float animPaddingBottom) {
        this.mAnimPaddingBottom = animPaddingBottom;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingStart() {
        return mAnimPaddingStart;
    }

    public void setAnimPaddingStart(float animPaddingStart) {
        this.mAnimPaddingStart = animPaddingStart;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingEnd() {
        return mAnimPaddingEnd;
    }

    public void setAnimPaddingEnd(float animPaddingEnd) {
        this.mAnimPaddingEnd = animPaddingEnd;
        if (mBaseAnim != null) {
            mBaseAnim.setPadding();
        }
    }

    public DrawPathType getDrawPathType() {
        return mDrawPathType;
    }

    public void setDrawPathType(DrawPathType drawPathType) {
        this.mDrawPathType = drawPathType;
        stopEraseAnim();
        mEraseAnim = null;
    }

    public Drawable getEraseIcon() {
        return mEraseIcon;
    }

    public BaseAnim getBaseAnim() {
        return mBaseAnim;
    }

    public void setBaseAnim(BaseAnim baseAnim) {
        this.mBaseAnim = baseAnim;
    }

    public int getRepeatCount() {
        return mRepeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.mRepeatCount = repeatCount;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public void setRepeatMode(@RepeatMode int repeatMode) {
        this.mRepeatMode = repeatMode;
    }

    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public boolean isAutoStart() {
        return mAutoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mHandMode) {
            return super.onTouchEvent(event);
        }
        Point point = new Point((int) event.getX(), (int) event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mShowEraseIcon = true;
                setErasePoint(point,false);
                mLastPoint = point;
                break;
            case MotionEvent.ACTION_MOVE:
                setErasePoint(point,false);
                break;
            case MotionEvent.ACTION_UP:
                mShowEraseIcon = !mEraseMode;
                setErasePoint(point,false);
                mLastPoint = null;
                break;
        }

        return true;
    }

    public boolean isEraseMode() {
        return mEraseMode;
    }

    public void setEraseMode(boolean eraseMode) {
        this.mEraseMode = eraseMode;
        if (!eraseMode){
            mClipPath.reset();
            mLastPoint = null;
        }
    }

    public boolean isHandMode() {
        return mHandMode;
    }

    public void setHandMode(boolean handMode) {
        this.mHandMode = handMode;
        mShowEraseIcon = !mHandMode;
        if (handMode) {
            stopEraseAnim();
        }
        invalidate();
    }
}
