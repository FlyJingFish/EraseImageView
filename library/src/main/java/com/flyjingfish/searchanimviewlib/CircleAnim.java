package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class CircleAnim extends BaseAnim{
    private CircleAnim(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd) {
        super(view, searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
        mTypeEvaluator = new CircleTypeEvaluator();
    }

    public static BaseAnim getInstance(SearchAnimView view, float searchRadius,float animPaddingLeft,float animPaddingTop,float animPaddingRight,
                                       float animPaddingBottom,float animPaddingStart,float animPaddingEnd){
        return new CircleAnim(view,searchRadius,animPaddingLeft,animPaddingTop,animPaddingRight,animPaddingBottom,animPaddingStart,animPaddingEnd);
    }
    private class CircleTypeEvaluator extends SearchTypeEvaluator {
        private float radius;
        private float centerX;
        private float centerY;

        public CircleTypeEvaluator() {
            update();
        }

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            float angle = fraction * 360;
            int x= (int) (centerX+radius*Math.cos(angle*Math.PI/180));
            int y= (int) (centerY+radius*Math.sin(angle*Math.PI/180));
            return new Point(x,y);
        }

        @Override
        public void update() {
            float width = view.getWidth();
            float height = view.getHeight();
            if (width < height){
                radius = (width - paddingLeft - paddingRight)/2 - searchRadius;
                centerX = paddingLeft + radius + searchRadius;
                centerY = (height - paddingTop - paddingBottom)/2 - paddingTop;
            }else {
                radius = (height - paddingTop - paddingBottom)/2 - searchRadius;
                centerX = (width - paddingLeft - paddingRight)/2 - paddingLeft;
                centerY = paddingTop + radius + searchRadius;
            }
        }
    }
}
