package com.flyjingfish.searchanimviewlib;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class ScratchCardLayout extends RelativeLayout {
    private EraseImageView mEraseImageView;
    private View mScratchView;

    private OnScratchListener mOnScratchListener;
    private boolean mIsEraseAllAreaAfterScratchOff;

    public ScratchCardLayout(Context context) {
        this(context, null);
    }

    public ScratchCardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view instanceof EraseImageView) {
                mEraseImageView = (EraseImageView) view;
            }
        }
        if (mEraseImageView == null) {
            throw new IllegalArgumentException("未找到 EraseImageView ,请添加 EraseImageView");
        }
        mEraseImageView.setOnEraseEndListener(bounds -> {
            float[] xy = getLineXY(mScratchView);
            final RectF scratchViewRectF = new RectF();
            scratchViewRectF.left = xy[0];
            scratchViewRectF.top = xy[1];
            scratchViewRectF.right = scratchViewRectF.left+mScratchView.getWidth();
            scratchViewRectF.bottom = scratchViewRectF.top+mScratchView.getHeight();
            if (scratchViewRectF.left >= bounds.left && scratchViewRectF.top >= bounds.top && scratchViewRectF.right <= bounds.right && scratchViewRectF.bottom <= bounds.bottom) {
                if (mOnScratchListener != null) {
                    mOnScratchListener.onScratchOff();
                }
                if (mIsEraseAllAreaAfterScratchOff) {
                    mEraseImageView.eraseAllArea();
                }
            }
        });
    }

    private float[] getLineXY(View view) {
        if (view.getParent() instanceof ScratchCardLayout) {
            return new float[]{view.getX(), view.getY()};
        } else {
            float[] xy = new float[2];
            xy[0] = view.getX();
            xy[1] = view.getY();
            return getLineXY(((ViewGroup) view.getParent()), xy);
        }
    }
    private float[] getLineXY(View view, float[] xy) {
        ViewParent viewParent = view.getParent();
        xy[0] += view.getX();
        xy[1] += view.getY();
        if (viewParent instanceof ScratchCardLayout) {
            return xy;
        } else {
            return getLineXY(((ViewGroup) viewParent), xy);
        }
    }
    public View getScratchView() {
        return mScratchView;
    }

    /**
     * 设置{@link EraseImageView}下边的隐藏内容 View（即刮开后看见的奖项内容）
     * @param scratchView {@link EraseImageView}下边的隐藏内容 View
     */
    public void setScratchView(View scratchView) {
        this.mScratchView = scratchView;
    }

    public boolean isEraseAllAreaAfterScratchOff() {
        return mIsEraseAllAreaAfterScratchOff;
    }

    public void setEraseAllAreaAfterScratchOff(boolean isEraseAllAreaAfterScratchOff) {
        this.mIsEraseAllAreaAfterScratchOff = isEraseAllAreaAfterScratchOff;
    }

    public interface OnScratchListener {
        void onScratchOff();
    }

    public void setOnScratchListener(OnScratchListener onScratchListener) {
        this.mOnScratchListener = onScratchListener;
    }
}
