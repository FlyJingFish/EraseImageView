package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class ReadAnim  extends BaseAnim{
    private ReadAnim(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                     float animPaddingBottom,float animPaddingStart,float animPaddingEnd) {
        super(view, searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        mTypeEvaluator = new ReadTypeEvaluator();
    }

    public static BaseAnim getInstance(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd){
        return new ReadAnim(view,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
    }

    private class ReadTypeEvaluator extends SearchTypeEvaluator {
        private float totalLength;
        private float pathWidth;
        private float radius;
        private float width;
        private float lineHeight;

        public ReadTypeEvaluator() {
            update();
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float current = fraction * totalLength;
            int row = (int) (current / pathWidth);
            int rowLength = (int) (current % pathWidth);
            int x = (int) (radius + paddingLeft + rowLength);
            int y = (int) (radius + paddingTop + row * lineHeight);
            return new Point(x, y);
        }

        @Override
        public void update() {
            this.radius = searchRadius;
            this.width = view.getWidth();
            float height = view.getHeight();
            pathWidth = width - radius * 2 - paddingLeft - paddingRight;
            float pathHeight = height - radius * 2- paddingTop- paddingBottom;
            int rows = (int) ((height - paddingLeft - paddingRight) / (radius * 2));
            if (height - paddingLeft - paddingRight - rows * (radius * 2) > radius) {
                rows += 1;
            }
            lineHeight = pathHeight / (rows - 1);
            totalLength = rows * pathWidth;
        }
    }
}
