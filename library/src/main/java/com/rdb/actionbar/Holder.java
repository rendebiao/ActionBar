package com.rdb.actionbar;

import android.view.View;

abstract class Holder {

    private OnVisibleChangeListener visibleChangeListener;

    protected abstract View get();

    public boolean isVisible() {
        return get().getVisibility() == View.VISIBLE;
    }

    protected void setVisibleInner(boolean visible) {
        if ((visible && get().getVisibility() == View.GONE) || (!visible && get().getVisibility() == View.VISIBLE)) {
            get().setVisibility(visible ? View.VISIBLE : View.GONE);
            if (visibleChangeListener != null) {
                visibleChangeListener.onVisibleChanged(visible);
            }
        }
    }

    public void setOnVisibleChnageListener(OnVisibleChangeListener visibleChangeListener) {
        this.visibleChangeListener = visibleChangeListener;
    }

    public interface OnVisibleChangeListener {
        void onVisibleChanged(boolean visible);
    }
}
