package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class SerpentineAnim extends BaseAnim {
    private SerpentineAnim(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                           float animPaddingBottom,float animPaddingStart,float animPaddingEnd) {
        super(view, searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        mTypeEvaluator = new SerpentineTypeEvaluator();
    }

    public static BaseAnim getInstance(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd){
        return new SerpentineAnim(view,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
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
            int lineCenterY = (int) (searchRadius + row * lineHeight);
            if (rowLength <= pathWidth) {
                y = lineCenterY;
                x = (int) (searchRadius + paddingLeft+ rowLength);
                if (row % 2 != 0) {
                    x = (int) (width - x);
                }
            } else {
                y = (int) (lineCenterY + rowLength - pathWidth);
                if (row % 2 != 0) {
                    x = (int) ((int) searchRadius + paddingLeft);
                } else {
                    x = (int) (width - paddingRight - searchRadius);
                }
            }
            y += paddingTop;
            return new Point(x, y);
        }

        @Override
        public void update() {
            this.width = view.getWidth();
            float height = view.getHeight();
            pathWidth = width - searchRadius * 2 - paddingLeft - paddingRight;
            float pathHeight = height - searchRadius * 2- paddingTop- paddingBottom;
            int rows = (int) ((height - paddingLeft - paddingRight) / (searchRadius * 2));
            if (height - paddingLeft - paddingRight - rows * (searchRadius * 2) > searchRadius) {
                rows += 1;
            }
            lineHeight = pathHeight / (rows - 1);
            row1Length = pathWidth + lineHeight;
            totalLength = (int) (rows * row1Length - lineHeight);
        }
    }

}
