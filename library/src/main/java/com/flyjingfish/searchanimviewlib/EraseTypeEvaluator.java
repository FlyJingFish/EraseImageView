package com.flyjingfish.searchanimviewlib;

import android.animation.TypeEvaluator;

public abstract class EraseTypeEvaluator implements TypeEvaluator<PointParams> {
    public EraseTypeEvaluator() {
        update();
    }

    public abstract void update();
}
