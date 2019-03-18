package com.rdb.actionbar;

import android.content.Context;
import android.view.View;

public abstract class Action extends Container implements View.OnClickListener {

    private Type type;
    private String tag;
    private OnActionListener actionListener;

    protected Action(Context context, int id, Type type, String tag, OnActionListener actionListener) {
        super(context, id);
        this.type = type;
        this.tag = tag;
        this.actionListener = actionListener;
        container.setOnClickListener(this);
    }

    public Type getType() {
        return type;
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
