package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class SerpentineAnim extends BaseAnim {
    public SerpentineAnim(EraseImageView view) {
        super(view);
        mTypeEvaluator = new SerpentineTypeEvaluator();
    }

    private class SerpentineTypeEvaluator extends EraseTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float row1Length;
        private float width;
        private float lineHeight;

        @Override
        public void update() {
            this.width = mView.getWidth();
            float height = mView.getHeight();
            pathWidth = width - mSearchRadius * 2 - mPaddingLeft - mPaddingRight;
            float pathHeight = height - mPaddingTop - mPaddingBottom;
            int rows = (int) ((pathHeight) / (mSearchRadius * 2));
            if (pathHeight - rows * (mSearchRadius * 2) > 0) {
                rows += 1;
            }
            lineHeight = pathHeight / (rows);
            row1Length = pathWidth + lineHeight;
            totalLength = (int) (rows * row1Length - lineHeight);
        }

        @Override
        public PointParams evaluate(float fraction, PointParams startValue, PointParams endValue) {
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
                    x = (int) (width - x - (mPaddingRight - mPaddingLeft));
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
            return new PointParams(new Point(x, y),false);
        }


    }

}
