package com.rdb.actionbar;

import android.view.View;

abstract class ViewHolder {

    protected abstract View get();

    public boolean isVisible() {
        return get().getVisibility() == View.VISIBLE;
    }

    protected void setVisibleInner(boolean visible) {
        get().setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
