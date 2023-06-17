package com.flyjingfish.searchanimviewlib;

import android.graphics.Point;

public class PointParams {
    public Point point;
    public boolean clipPathMovePoint;

    public PointParams(Point point, boolean clipPathMovePoint) {
        this.point = point;
        this.clipPathMovePoint = clipPathMovePoint;
    }
}
