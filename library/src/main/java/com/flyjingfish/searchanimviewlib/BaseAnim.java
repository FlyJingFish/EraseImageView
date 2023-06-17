package com.flyjingfish.searchanimviewlib;

import android.util.LayoutDirection;

import androidx.core.text.TextUtilsCompat;

import java.util.Locale;

public class BaseAnim {

    protected float mPaddingLeft;
    protected float mPaddingTop;
    protected float mPaddingRight;
    protected float mPaddingBottom;
    protected EraseImageView mView;
    protected float mSearchRadius;
    protected SearchTypeEvaluator mTypeEvaluator;
    protected SearchTypeEvaluator getTypeEvaluator() {
        return mTypeEvaluator;
    }

    protected BaseAnim(EraseImageView view) {
        this.mView = view;
        this.mSearchRadius = view.getEraseRadius();
        setPadding();
    }

    public void setEraseRadius() {
        this.mSearchRadius = mView.getEraseRadius();
        if (mTypeEvaluator != null){
            mTypeEvaluator.update();
        }
    }

    protected void setPadding() {
        float animPaddingLeft = mView.getAnimPaddingLeft();
        float animPaddingTop = mView.getAnimPaddingTop();
        float animPaddingRight = mView.getAnimPaddingRight();
        float animPaddingBottom = mView.getAnimPaddingBottom();
        float animPaddingStart = mView.getAnimPaddingStart();
        float animPaddingEnd = mView.getAnimPaddingEnd();

        boolean isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL;

        mPaddingTop = animPaddingTop;
        mPaddingBottom = animPaddingBottom;

        if (isRtl){
            if (animPaddingEnd != 0){
                this.mPaddingLeft = animPaddingEnd;
            }else {
                this.mPaddingLeft = animPaddingLeft;
            }
        }else {
            if (animPaddingStart != 0){
                this.mPaddingLeft = animPaddingStart;
            }else {
                this.mPaddingLeft = animPaddingLeft;
            }

        }

        if (isRtl){
            if (animPaddingStart != 0){
                this.mPaddingRight = animPaddingStart;
            }else {
                this.mPaddingRight = animPaddingRight;
            }
        }else {
            if (animPaddingEnd != 0){
                this.mPaddingRight = animPaddingEnd;
            }else {
                this.mPaddingRight = animPaddingRight;
            }

        }
        if (mTypeEvaluator != null){
            mTypeEvaluator.update();
        }
    }

}
