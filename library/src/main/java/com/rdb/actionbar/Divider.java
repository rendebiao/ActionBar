package com.rdb.actionbar;

import android.view.View;

public class Divider extends ViewHolder {

    private View view;

    protected Divider(View view) {
        this.view = view;
    }

    @Override
    protected View get() {
        return view;
    }
}