package com.flyjingfish.searchanimviewlib;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

public class SearchAnimView extends AppCompatImageView {
    public static final int INFINITE = ValueAnimator.INFINITE;
    public static final int RESTART = ValueAnimator.RESTART;
    public static final int REVERSE = ValueAnimator.REVERSE;
    private final Path mSearchPath = new Path();
    private final Rect mSearchIconBounds = new Rect();
    private final RectF mSearchIconPercent = new RectF();
    private final RectF mSearchIconSubRectF = new RectF();
    private float mSearchRadius;
    private ValueAnimator mSearchAnim;
    private Drawable mSearchIcon;
    private DrawPathType mDrawPathType;
    private float mAnimPaddingLeft;
    private float mAnimPaddingTop;
    private float mAnimPaddingRight;
    private float mAnimPaddingBottom;
    private float mAnimPaddingStart;
    private float mAnimPaddingEnd;
    private BaseAnim mBaseAnim;
    private int mRepeatCount = 0;
    private int mRepeatMode = ValueAnimator.RESTART;
    private TimeInterpolator mInterpolator = new LinearInterpolator();
    private long mDuration;
    private boolean mAutoStart;

    @IntDef({RESTART, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }
    public SearchAnimView(@NonNull Context context) {
        this(context,null);
    }

    public SearchAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchAnimView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchAnimView);
        int drawPathTypeInt = a.getInt(R.styleable.SearchAnimView_FlyJFish_searchAnim_DrawPathType, 0);
        mSearchIcon = a.getDrawable(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource);
        float percentLeft = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentLeft, 0);
        float percentTop = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentTop, 0);
        float percentRight = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentRight, 0);
        float percentBottom = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentBottom, 0);
        mAnimPaddingLeft = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingLeft, 0);
        mAnimPaddingTop = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingTop, 0);
        mAnimPaddingRight = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingRight, 0);
        mAnimPaddingBottom = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingBottom, 0);
        mAnimPaddingStart = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingStart, 0);
        mAnimPaddingEnd = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingEnd, 0);
        mSearchRadius = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_radius, 30);

        mDuration = a.getInteger(R.styleable.SearchAnimView_FlyJFish_searchAnim_duration, 1000);
        mRepeatCount = a.getInteger(R.styleable.SearchAnimView_FlyJFish_searchAnim_repeatCount, 0);
        mRepeatMode = a.getInt(R.styleable.SearchAnimView_FlyJFish_searchAnim_repeatMode, RESTART);
        mAutoStart = a.getBoolean(R.styleable.SearchAnimView_FlyJFish_searchAnim_autoStart, false);
        a.recycle();

        mDrawPathType = DrawPathType.values()[drawPathTypeInt];
        mSearchPath.setFillType(Path.FillType.INVERSE_WINDING);

        mSearchIconPercent.set(percentLeft,percentTop,percentRight,percentBottom);

        calculateSearchIconSubRectF();

        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (mSearchAnim != null && !mSearchAnim.isStarted()){
                    mSearchAnim.start();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (mSearchAnim != null){
                    mSearchAnim.removeAllListeners();
                    mSearchAnim.cancel();
                }
            }
        });

        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                removeOnAttachStateChangeListener(this);
                EnsureFragmentX ensureFragmentX = EnsureFragmentXUtils.ensureInFragmentX(v);
                if (ensureFragmentX.isInFragmentX){
                    addObserver(ensureFragmentX.lifecycleOwner);
                }else if (getContext() instanceof LifecycleOwner){
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
            if (mSearchAnim == null) {
                return;
            }
            if (event == Lifecycle.Event.ON_START && mSearchAnim.isPaused()) {
                mSearchAnim.resume();
            } else if (event == Lifecycle.Event.ON_STOP && mSearchAnim.isRunning()) {
                mSearchAnim.pause();
            }else if (event == Lifecycle.Event.ON_DESTROY) {
                mSearchAnim.cancel();
            }
        }
    };
    private void addObserver(LifecycleOwner owner) {
        if (owner != null) {
            if (owner instanceof Fragment){
                owner = ((Fragment) owner).getViewLifecycleOwner();
            }
            owner.getLifecycle().removeObserver(mLifecycleEventObserver);
            owner.getLifecycle().addObserver(mLifecycleEventObserver);
        }
    }

    public void setSearchIcon(Drawable drawable,RectF searchIconPercent){
        mSearchIcon = drawable;
        this.mSearchIconPercent.set(searchIconPercent);
        calculateSearchIconSubRectF();
    }

    public float getSearchRadius() {
        return mSearchRadius;
    }

    public void setSearchRadius(float searchRadius) {
        this.mSearchRadius = searchRadius;
        calculateSearchIconSubRectF();
        if (mBaseAnim != null){
            mBaseAnim.setSearchRadius();
        }
    }

    private void calculateSearchIconSubRectF(){
        float width = mSearchRadius * 2 /(mSearchIconPercent.right - mSearchIconPercent.left);
        float height = mSearchRadius * 2 /(mSearchIconPercent.bottom - mSearchIconPercent.top);
        float left = width * mSearchIconPercent.left + mSearchRadius;
        float top = height * mSearchIconPercent.top + mSearchRadius;
        float right = width * (1 - mSearchIconPercent.right) + mSearchRadius;
        float bottom = height * (1- mSearchIconPercent.bottom) + mSearchRadius;
        mSearchIconSubRectF.set(left,top,right,bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mSearchPath);
        super.onDraw(canvas);
        canvas.restore();
        if (mSearchIcon != null){
            mSearchIcon.setBounds(mSearchIconBounds);
            mSearchIcon.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAutoStart){
            startSearchAnim();
        }
    }

    public void startSearchAnim(){
        stopSearchAnim();

        if (mSearchAnim == null){
            mSearchAnim = new ValueAnimator();
        }
        mBaseAnim = getAnim();
        mSearchAnim.setObjectValues(new Point(0,0));
        mSearchAnim.setEvaluator(mBaseAnim.getTypeEvaluator());
        mSearchAnim.setDuration(mDuration);
        mSearchAnim.setRepeatMode(mRepeatMode);
        mSearchAnim.setRepeatCount(mRepeatCount);
        mSearchAnim.setInterpolator(mInterpolator);
        mSearchAnim.addUpdateListener(animation -> {
            Point point = (Point) animation.getAnimatedValue();
            setSearchPoint(point);
        });
        mSearchAnim.start();
    }

    public void setSearchPoint(Point point){
        mSearchPath.reset();
        mSearchPath.addCircle(point.x, point.y, mSearchRadius, Path.Direction.CW);

        mSearchIconBounds.set((int) (point.x - mSearchIconSubRectF.left), (int) (point.y - mSearchIconSubRectF.top), (int) (point.x + mSearchIconSubRectF.right), (int) (point.y + mSearchIconSubRectF.bottom));
        invalidate();
    }

    public void stopSearchAnim(){
        if (mSearchAnim != null){
            mSearchAnim.removeAllUpdateListeners();
            mSearchAnim.cancel();
        }
    }

    public void resumeSearchAnim() {
        if (mSearchAnim != null) {
            mSearchAnim.resume();
        } else {
            startSearchAnim();
        }
    }

    public void pauseSearchAnim() {
        if (mSearchAnim != null) {
            mSearchAnim.pause();
        }
    }

    private BaseAnim getAnim(){
        BaseAnim baseAnim;
        if (mDrawPathType == DrawPathType.Read){
            baseAnim = ReadAnim.getInstance(this);
        }else if (mDrawPathType == DrawPathType.Circle){
            baseAnim = CircleAnim.getInstance(this);
        }else if (mDrawPathType == DrawPathType.Serpentine){
            baseAnim = SerpentineAnim.getInstance(this);
        }else {
            baseAnim = CircleAnim.getInstance(this);
        }
        return baseAnim;
    }

    public float getAnimPaddingLeft() {
        return mAnimPaddingLeft;
    }

    public void setAnimPaddingLeft(float animPaddingLeft) {
        this.mAnimPaddingLeft = animPaddingLeft;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingTop() {
        return mAnimPaddingTop;
    }

    public void setAnimPaddingTop(float animPaddingTop) {
        this.mAnimPaddingTop = animPaddingTop;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingRight() {
        return mAnimPaddingRight;
    }

    public void setAnimPaddingRight(float animPaddingRight) {
        this.mAnimPaddingRight = animPaddingRight;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingBottom() {
        return mAnimPaddingBottom;
    }

    public void setAnimPaddingBottom(float animPaddingBottom) {
        this.mAnimPaddingBottom = animPaddingBottom;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingStart() {
        return mAnimPaddingStart;
    }

    public void setAnimPaddingStart(float animPaddingStart) {
        this.mAnimPaddingStart = animPaddingStart;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public float getAnimPaddingEnd() {
        return mAnimPaddingEnd;
    }

    public void setAnimPaddingEnd(float animPaddingEnd) {
        this.mAnimPaddingEnd = animPaddingEnd;
        if (mBaseAnim != null){
            mBaseAnim.setPadding();
        }
    }

    public DrawPathType getDrawPathType() {
        return mDrawPathType;
    }

    public void setDrawPathType(DrawPathType drawPathType) {
        this.mDrawPathType = drawPathType;
        stopSearchAnim();
        mSearchAnim = null;
    }

    public Drawable getSearchIcon() {
        return mSearchIcon;
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

}
