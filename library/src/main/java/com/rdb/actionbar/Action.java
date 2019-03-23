package com.rdb.actionbar;

import android.content.Context;
import android.view.View;

public abstract class Action extends Container implements View.OnClickListener {

    private Type type;
    private String tag;
    private OnActionListener actionListener;

    protected Action(Context context, int id, OnActionListener actionListener) {
        super(context, id);
        this.actionListener = actionListener;
        container.setOnClickListener(this);
    }

    public Action setType(Type type) {
        this.type = type;
        return this;
    }

    public Type getType() {
        return type;
    }

    public Action setTag(String tag) {
        this.tag = tag;
        return this;
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
}
