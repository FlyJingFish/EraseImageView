package com.flyjingfish.searchanimviewlib;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class ScratchCardLayout extends RelativeLayout {
    private EraseImageView mEraseImageView;
    private View mScratchView;
    private boolean mOnLayoutFinish;
    private final RectF mScratchViewRectF = new RectF();
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
            if (mScratchViewRectF.left >= bounds.left && mScratchViewRectF.top >= bounds.top && mScratchViewRectF.right <= bounds.right && mScratchViewRectF.bottom <= bounds.bottom) {
                if (mOnScratchListener != null) {
                    mOnScratchListener.onScratchOff();
                }
                if (mIsEraseAllAreaAfterScratchOff) {
                    mEraseImageView.eraseAllArea();
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mOnLayoutFinish = true;
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
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] scratchViewShowRect = new int[2];
                mScratchView.getLocationOnScreen(scratchViewShowRect);
                int[] eraseImageViewShowRect = new int[2];
                mEraseImageView.getLocationOnScreen(eraseImageViewShowRect);
                int left = scratchViewShowRect[0] - eraseImageViewShowRect[0];
                int top = scratchViewShowRect[1] - eraseImageViewShowRect[1];
                mScratchViewRectF.set(left, top, left + mScratchView.getWidth(), top + mScratchView.getHeight());
            }
        };
        if (mOnLayoutFinish) {
            listener.onGlobalLayout();
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(listener);
        }

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
