package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class LightningAnim extends BaseAnim{
    protected LightningAnim(SearchAnimView view) {
        super(view);
        mTypeEvaluator = new LightningTypeEvaluator();
    }

    private class LightningTypeEvaluator extends SearchTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float row1Length;
        private float width;
        private float lineHeight;
        private float sin;
        private float cos;

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
            lineHeight = pathHeight / rows ;
            float slashLength = (float) Math.sqrt(Math.pow(pathWidth,2)+Math.pow(lineHeight,2));
            row1Length = (float) Math.sqrt(Math.pow(pathWidth,2)+Math.pow(lineHeight,2));
            sin = lineHeight/row1Length;
            cos = pathWidth/row1Length;
            totalLength = (int) (rows * row1Length);
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float current = fraction * totalLength;
            int row = (int) (current / row1Length);
            int rowLength = (int) (current % row1Length);
            int x;
            int y;
            int lineCenterY;
            if (row % 2 == 0){
                lineCenterY = (int) (mSearchRadius + row * lineHeight);
                x = (int) (mSearchRadius + mPaddingLeft + rowLength * cos);
                y = (int) (lineCenterY + rowLength * sin);
            }else {
                lineCenterY = (int) (mSearchRadius + (row -1) * lineHeight);
                x = (int) (width - mSearchRadius - mPaddingRight - rowLength * cos);
                y = (int) (lineHeight + (lineCenterY + rowLength * sin));
            }
            return new Point(x, y);
        }
    }
}
