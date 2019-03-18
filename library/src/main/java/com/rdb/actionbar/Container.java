package com.rdb.actionbar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

abstract class Container extends ViewHolder {

    protected int id;
    protected FrameLayout container;

    protected Container(Context context, int id) {
        this.id = id;
        container = new FrameLayout(context);
    }

    public Container addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        container.addView(view, layoutParams);
        return this;
    }

    @Override
    protected View get() {
        return container;
    }

    public int getId() {
        return id;
    }

    public int getCenterX() {
        return (container.getLeft() + container.getRight()) / 2;
    }

    public int getCenterY() {
        return (container.getTop() + container.getBottom()) / 2;
    }

    public Container setMargin(int left, int right) {
        if (container.getChildCount() > 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) container.getChildAt(0).getLayoutParams();
            layoutParams.leftMargin = left;
            layoutParams.rightMargin = right;
        }
        return this;
    }
}
