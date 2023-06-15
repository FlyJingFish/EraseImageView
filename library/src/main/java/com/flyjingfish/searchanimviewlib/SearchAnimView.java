package com.flyjingfish.searchanimviewlib;

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
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class SearchAnimView extends AppCompatImageView {

    private final Path searchPath = new Path();
    private final Rect searchIconBounds = new Rect();
    private final RectF searchIconPercent = new RectF();
    private final RectF searchIconSubRectF = new RectF();
    private float searchRadius;
    private ValueAnimator searchAnim;
    private Drawable searchIcon;
    private DrawPathType drawPathType;
    private float animPaddingLeft;
    private float animPaddingTop;
    private float animPaddingRight;
    private float animPaddingBottom;
    private float animPaddingStart;
    private float animPaddingEnd;

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
        searchIcon = a.getDrawable(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource);
        float percentLeft = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentLeft, 0);
        float percentTop = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentTop, 0);
        float percentRight = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentRight, 0);
        float percentBottom = a.getFloat(R.styleable.SearchAnimView_FlyJFish_searchAnim_resource_percentBottom, 0);
        animPaddingLeft = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingLeft, 0);
        animPaddingTop = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingTop, 0);
        animPaddingRight = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingRight, 0);
        animPaddingBottom = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingBottom, 0);
        animPaddingStart = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingStart, 0);
        animPaddingEnd = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_paddingEnd, 0);
        searchRadius = a.getDimension(R.styleable.SearchAnimView_FlyJFish_searchAnim_radius, 30);
        a.recycle();

        drawPathType = DrawPathType.values()[drawPathTypeInt];
        searchPath.setFillType(Path.FillType.INVERSE_WINDING);

        searchIconPercent.set(percentLeft,percentTop,percentRight,percentBottom);

        calculateSearchIconSubRectF();
    }

    public void setSearchIcon(Drawable drawable,RectF searchIconPercent){
        searchIcon = drawable;
        this.searchIconPercent.set(searchIconPercent);
        calculateSearchIconSubRectF();
    }

    public float getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(float searchRadius) {
        this.searchRadius = searchRadius;
        calculateSearchIconSubRectF();
    }

    private void calculateSearchIconSubRectF(){
        float width = searchRadius * 2 /(searchIconPercent.right - searchIconPercent.left);
        float height = searchRadius * 2 /(searchIconPercent.bottom - searchIconPercent.top);
        float left = width * searchIconPercent.left + searchRadius;
        float top = height * searchIconPercent.top + searchRadius;
        float right = width * (1 - searchIconPercent.right) + searchRadius;
        float bottom = height * (1- searchIconPercent.bottom) + searchRadius;
        searchIconSubRectF.set(left,top,right,bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(searchPath);
        super.onDraw(canvas);
        canvas.restore();
        if (searchIcon != null){
            searchIcon.setBounds(searchIconBounds);
            searchIcon.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        startSearchAnim();
    }

    public void startSearchAnim(){
        stopSearchAnim();

        if (searchAnim == null){
            BaseAnim baseAnim = getAnim();
            searchAnim = ValueAnimator.ofObject(baseAnim.getTypeEvaluator(),new Point(0,0));
            searchAnim.setDuration(10000);
            searchAnim.setRepeatMode(ValueAnimator.RESTART);
            searchAnim.setRepeatCount(ValueAnimator.INFINITE);
            searchAnim.setInterpolator(new LinearInterpolator());
        }
        searchAnim.addUpdateListener(animation -> {
            Point point = (Point) animation.getAnimatedValue();
            searchPath.reset();
            searchPath.addCircle(point.x, point.y, searchRadius, Path.Direction.CW);

            searchIconBounds.set((int) (point.x - searchIconSubRectF.left), (int) (point.y - searchIconSubRectF.top), (int) (point.x + searchIconSubRectF.right), (int) (point.y + searchIconSubRectF.bottom));
            invalidate();
        });
        searchAnim.start();
    }

    private BaseAnim getAnim(){
        BaseAnim baseAnim;
        if (drawPathType == DrawPathType.Read){
            baseAnim = ReadAnim.getInstance(this,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        }else if (drawPathType == DrawPathType.Circle){
            baseAnim = CircleAnim.getInstance(this,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        }else if (drawPathType == DrawPathType.Serpentine){
            baseAnim = SerpentineAnim.getInstance(this,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        }else {
            baseAnim = CircleAnim.getInstance(this,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        }
        return baseAnim;
    }

    public void stopSearchAnim(){
        if (searchAnim != null){
            searchAnim.removeAllUpdateListeners();
            searchAnim.cancel();
        }
    }

}
