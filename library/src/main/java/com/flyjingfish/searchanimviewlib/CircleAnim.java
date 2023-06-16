package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class CircleAnim extends BaseAnim{
    private CircleAnim(SearchAnimView view) {
        super(view);
        mTypeEvaluator = new CircleTypeEvaluator();
    }

    public static BaseAnim getInstance(SearchAnimView view){
        return new CircleAnim(view);
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
            float width = mView.getWidth();
            float height = mView.getHeight();
            if (width < height){
                radius = (width - mPaddingLeft - mPaddingRight)/2 - mSearchRadius;
                centerX = mPaddingLeft + radius + mSearchRadius;
                centerY = (height - mPaddingTop - mPaddingBottom)/2 - mPaddingTop;
            }else {
                radius = (height - mPaddingTop - mPaddingBottom)/2 - mSearchRadius;
                centerX = (width - mPaddingLeft - mPaddingRight)/2 - mPaddingLeft;
                centerY = mPaddingTop + radius + mSearchRadius;
            }
        }
    }
}
