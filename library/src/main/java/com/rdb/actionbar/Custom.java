package com.rdb.actionbar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class Custom extends Action {

    protected Custom(Context context, int id, OnActionListener actionClickListener) {
        super(context, id, actionClickListener);
    }

    public Custom setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public Custom addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomViewInner(view, layoutParams);
        return this;
    }

    public Custom setType(@Type int type) {
        super.setTypeInner(type);
        return this;
    }

    public Custom setTag(String tag) {
        super.setTagInner(tag);
        return this;
    }

    public Custom setMargin(int left, int right) {
        super.setMarginInner(left, right);
        return this;
    }
}
