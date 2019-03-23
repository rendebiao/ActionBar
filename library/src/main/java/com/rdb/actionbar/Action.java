package com.rdb.actionbar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;

public abstract class Action extends Container implements View.OnClickListener {

    public static final int BACK = 0;
    public static final int FINISH = 1;
    public static final int OVERFLOW = 2;
    private String tag;
    private @Type
    int type;
    private OnActionListener actionListener;

    protected Action(Context context, int id, OnActionListener actionListener) {
        super(context, id);
        this.actionListener = actionListener;
        container.setOnClickListener(this);
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
