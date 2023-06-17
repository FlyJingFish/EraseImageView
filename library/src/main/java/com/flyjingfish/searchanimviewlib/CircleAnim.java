package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class CircleAnim extends BaseAnim {
    public CircleAnim(EraseImageView view) {
        super(view);
        mTypeEvaluator = new CircleTypeEvaluator();
    }

    private class CircleTypeEvaluator extends SearchTypeEvaluator {
        private float radius;
        private float centerX;
        private float centerY;

        @Override
        public void update() {
            float width = mView.getWidth();
            float height = mView.getHeight();
            if (width < height) {
                radius = (width - mPaddingLeft - mPaddingRight) / 2 - mSearchRadius;
                centerX = mPaddingLeft + radius + mSearchRadius;
                centerY = (height - mPaddingTop - mPaddingBottom) / 2 - mPaddingTop;
            } else {
                radius = (height - mPaddingTop - mPaddingBottom) / 2 - mSearchRadius;
                centerX = (width - mPaddingLeft - mPaddingRight) / 2 - mPaddingLeft;
                centerY = mPaddingTop + radius + mSearchRadius;
            }
        }

        @Override
        public PointParams evaluate(float fraction, PointParams startValue, PointParams endValue) {
            float angle = fraction * 360;
            int x = (int) (centerX + radius * Math.cos(angle * Math.PI / 180));
            int y = (int) (centerY + radius * Math.sin(angle * Math.PI / 180));
            return new PointParams(new Point(x, y),false);
        }
    }
}
