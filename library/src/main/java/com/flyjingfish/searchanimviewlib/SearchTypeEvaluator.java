package com.flyjingfish.searchanimviewlib;

import android.animation.TypeEvaluator;
import android.graphics.Point;

public abstract class SearchTypeEvaluator implements TypeEvaluator<PointParams> {
    public SearchTypeEvaluator() {
        update();
    }

    public abstract void update();
}
