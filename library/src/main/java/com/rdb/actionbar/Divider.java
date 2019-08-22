package com.rdb.actionbar;

import android.support.annotation.ColorInt;
import android.view.View;

public class Divider extends Holder<View> {

    protected Divider(View view) {
        super(view);
    }

    public Divider setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public Divider setColor(@ColorInt int color) {
        get().setBackgroundColor(color);
        return this;
    }
}