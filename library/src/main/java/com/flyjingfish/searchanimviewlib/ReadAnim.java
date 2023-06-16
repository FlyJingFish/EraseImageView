package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class ReadAnim extends BaseAnim {
    public ReadAnim(SearchAnimView view) {
        super(view);
        mTypeEvaluator = new ReadTypeEvaluator();
    }

    private class ReadTypeEvaluator extends SearchTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float radius;
        private float width;
        private float lineHeight;

        @Override
        public void update() {
            this.radius = mSearchRadius;
            this.width = mView.getWidth();
            float height = mView.getHeight();
            pathWidth = width - radius * 2 - mPaddingLeft - mPaddingRight;
            float pathHeight = height - radius * 2 - mPaddingTop - mPaddingBottom;
            int rows = (int) ((height - mPaddingLeft - mPaddingRight) / (radius * 2));
            if (height - mPaddingLeft - mPaddingRight - rows * (radius * 2) > radius) {
                rows += 1;
            }
            lineHeight = pathHeight / (rows - 1);
            totalLength = rows * pathWidth;
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float current = fraction * totalLength;
            int row = (int) (current / pathWidth);
            int rowLength = (int) (current % pathWidth);
            int x = (int) (radius + mPaddingLeft + rowLength);
            int y = (int) (radius + mPaddingTop + row * lineHeight);
            return new Point(x, y);
        }

    }
}
