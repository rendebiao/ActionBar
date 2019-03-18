package com.rdb.actionbar;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

public class Custom extends Action {

    protected Custom(Context context, int id, Type type, String tag, OnActionListener actionClickListener) {
        super(context, id, type, tag, actionClickListener);
    }

    @Override
    public Custom addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomView(view, layoutParams);
        return this;
    }

    @Override
    public Custom setMargin(int left, int right) {
        super.setMargin(left, right);
        return this;
    }
}