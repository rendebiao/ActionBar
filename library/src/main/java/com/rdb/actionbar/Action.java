package com.rdb.actionbar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import android.widget.FrameLayout;

public abstract class Action extends Holder implements View.OnClickListener {

    public static final int BACK = 1;
    public static final int FINISH = 2;
    public static final int OVERFLOW = 3;
    protected int id;
    private String tag;
    private @Type
    int type;
    private FrameLayout container;
    private OnActionListener actionListener;

    protected Action(Context context, int id, OnActionListener actionListener) {
        this.id = id;
        container = new FrameLayout(context);
        container.setOnClickListener(this);
        this.actionListener = actionListener;
    }

    protected void addCustomViewInner(View view, FrameLayout.LayoutParams layoutParams) {
        container.addView(view, layoutParams);
    }

    @Override
    protected FrameLayout get() {
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

    public void setMarginInner(int left, int right) {
        if (container.getChildCount() > 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) container.getChildAt(0).getLayoutParams();
            layoutParams.leftMargin = left;
            layoutParams.rightMargin = right;
        }
    }
    protected void setTypeInner(@Type int type) {
        this.type = type;
    }

    public @Type
    int getType() {
        return type;
    }

    protected void setTagInner(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void onClick(View v) {
        actionListener.onActionClick(this);
    }

    public interface OnActionListener {
        void onActionClick(Action action);
    }

    @IntDef({BACK, FINISH, OVERFLOW})
    public @interface Type {
    }
}
