package com.rdb.actionbar;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

public abstract class ViewCreater {

    private static ViewCreater viewCreater = new ViewCreater() {
        @Override
        AppCompatTextView newTextView(Context context) {
            return new AppCompatTextView(context);
        }

        @Override
        AppCompatImageView newImageView(Context context) {
            return new AppCompatImageView(context);
        }
    };

    public static ViewCreater getViewCreater() {
        return viewCreater;
    }

    public static void setViewCreater(ViewCreater viewCreater) {
        ViewCreater.viewCreater = viewCreater;
    }

    abstract AppCompatTextView newTextView(Context context);

    abstract AppCompatImageView newImageView(Context context);
}
