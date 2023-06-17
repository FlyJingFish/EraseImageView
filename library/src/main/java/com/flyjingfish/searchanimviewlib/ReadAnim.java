package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class ReadAnim extends BaseAnim {
    public ReadAnim(EraseView view) {
        super(view);
        mTypeEvaluator = new ReadTypeEvaluator();
    }

    private class ReadTypeEvaluator extends SearchTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float width;
        private float lineHeight;
        private int lastRow;
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
            lineHeight = pathHeight / rows;
            totalLength = rows * pathWidth;
        }

        @Override
        public PointParams evaluate(float fraction, PointParams startValue, PointParams endValue) {
            boolean clipPathMovePoint = false;
            float current = fraction * totalLength;
            int row = (int) (current / pathWidth);
            int rowLength = (int) (current % pathWidth);
            int x = (int) (mSearchRadius + mPaddingLeft + rowLength);
            int y = (int) (mSearchRadius + mPaddingTop + row * lineHeight);
            if (row != lastRow){
                clipPathMovePoint = true;
            }
            lastRow = row;
            return new PointParams(new Point(x, y),clipPathMovePoint);
        }

    }
}
