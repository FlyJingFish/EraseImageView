package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class SerpentineAnim extends BaseAnim {
    private SerpentineAnim(SearchAnimView view) {
        super(view);
        mTypeEvaluator = new SerpentineTypeEvaluator();
    }

    public static BaseAnim getInstance(SearchAnimView view){
        return new SerpentineAnim(view);
    }

    private class SerpentineTypeEvaluator extends SearchTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float row1Length;
        private float width;
        private float lineHeight;

        public SerpentineTypeEvaluator() {
            update();
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float current = fraction * totalLength;
            int row = (int) (current / row1Length);
            int rowLength = (int) (current % row1Length);
            int x;
            int y;
            int lineCenterY = (int) (mSearchRadius + row * lineHeight);
            if (rowLength <= pathWidth) {
                y = lineCenterY;
                x = (int) (mSearchRadius + mPaddingLeft + rowLength);
                if (row % 2 != 0) {
                    x = (int) (width - x);
                }
            } else {
                y = (int) (lineCenterY + rowLength - pathWidth);
                if (row % 2 != 0) {
                    x = (int) ((int) mSearchRadius + mPaddingLeft);
                } else {
                    x = (int) (width - mPaddingRight - mSearchRadius);
                }
            }
            y += mPaddingTop;
            return new Point(x, y);
        }

        @Override
        public void update() {
            this.width = mView.getWidth();
            float height = mView.getHeight();
            pathWidth = width - mSearchRadius * 2 - mPaddingLeft - mPaddingRight;
            float pathHeight = height - mSearchRadius * 2- mPaddingTop - mPaddingBottom;
            int rows = (int) ((height - mPaddingLeft - mPaddingRight) / (mSearchRadius * 2));
            if (height - mPaddingLeft - mPaddingRight - rows * (mSearchRadius * 2) > mSearchRadius) {
                rows += 1;
            }
            lineHeight = pathHeight / (rows - 1);
            row1Length = pathWidth + lineHeight;
            totalLength = (int) (rows * row1Length - lineHeight);
        }
    }

}
