package com.rdb.actionbar;

import android.view.View;

abstract class Holder<T extends View> {

    private T view;
    private OnVisibleChangeListener visibleChangeListener;

    public Holder(T view) {
        this.view = view;
    }

    protected T get() {
        return view;
    }

    public boolean isVisible() {
        return view.getVisibility() == View.VISIBLE;
    }

    protected void setVisibleInner(boolean visible) {
        if ((visible && view.getVisibility() == View.GONE) || (!visible && view.getVisibility() == View.VISIBLE)) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            if (visibleChangeListener != null) {
                visibleChangeListener.onVisibleChanged(visible);
            }
        }
    }

    public void setOnVisibleChangeListener(OnVisibleChangeListener visibleChangeListener) {
        this.visibleChangeListener = visibleChangeListener;
    }

    public interface OnVisibleChangeListener {
        void onVisibleChanged(boolean visible);
    }
}
