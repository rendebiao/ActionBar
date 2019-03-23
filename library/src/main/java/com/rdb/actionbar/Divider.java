package com.rdb.actionbar;

import android.support.annotation.ColorInt;
import android.view.View;

public class Divider extends Holder {

    private View view;

    protected Divider(View view) {
        this.view = view;
    }

    @Override
    protected View get() {
        return view;
    }

    public Divider setColor(@ColorInt int color) {
        view.setBackgroundColor(color);
        return this;
    }

    public Divider setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }
}