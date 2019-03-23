package com.rdb.actionbar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class CustomAction extends Action {

    protected CustomAction(Context context, int id, OnActionListener actionClickListener) {
        super(context, id, actionClickListener);
    }

    public CustomAction setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public CustomAction addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomViewInner(view, layoutParams);
        return this;
    }

    public CustomAction setType(@Type int type) {
        super.setTypeInner(type);
        return this;
    }

    public CustomAction setTag(String tag) {
        super.setTagInner(tag);
        return this;
    }

    public CustomAction setMargin(int left, int right) {
        super.setMarginInner(left, right);
        return this;
    }
}
