package com.flyjingfish.searchanimviewlib;

import android.util.LayoutDirection;

import androidx.core.text.TextUtilsCompat;

import java.util.Locale;

public class BaseAnim {

    protected float paddingLeft;
    protected float paddingTop;
    protected float paddingRight;
    protected float paddingBottom;
    protected SearchAnimView view;
    protected float searchRadius;
    protected SearchTypeEvaluator mTypeEvaluator;
    protected SearchTypeEvaluator getTypeEvaluator() {
        return mTypeEvaluator;
    }

    protected BaseAnim(SearchAnimView view, float searchRadius,
                       float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd) {
        this.view = view;
        this.searchRadius = searchRadius;
        setPadding(animPaddingLeft, animPaddingTop, animPaddingRight, animPaddingBottom, animPaddingStart, animPaddingEnd);
    }

    protected void setPadding(float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd) {

        boolean isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL;

        paddingTop = animPaddingTop;
        paddingBottom = animPaddingBottom;

        if (isRtl){
            if (animPaddingEnd != 0){
                this.paddingLeft = animPaddingEnd;
            }else {
                this.paddingLeft = animPaddingLeft;
            }
        }else {
            if (animPaddingStart != 0){
                this.paddingLeft = animPaddingStart;
            }else {
                this.paddingLeft = animPaddingLeft;
            }

        }

        if (isRtl){
            if (animPaddingStart != 0){
                this.paddingRight = animPaddingStart;
            }else {
                this.paddingRight = animPaddingRight;
            }
        }else {
            if (animPaddingEnd != 0){
                this.paddingRight = animPaddingEnd;
            }else {
                this.paddingRight = animPaddingRight;
            }

        }
        if (mTypeEvaluator != null){
            mTypeEvaluator.update();
        }
    }

}
