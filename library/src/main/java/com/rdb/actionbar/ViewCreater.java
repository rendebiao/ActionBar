package com.rdb.actionbar;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

public abstract class ViewCreater {

    private static ViewCreater viewCreater = new ViewCreater() {
        @Override
        public AppCompatTextView newTextView(Context context) {
            return new AppCompatTextView(context);
        }

        @Override
        public AppCompatImageView newImageView(Context context) {
            return new AppCompatImageView(context);
        }

        @Override
        public AppCompatTextView newMenuTextView(Context context) {
            return new AppCompatTextView(context);
        }

        @Override
        public AppCompatImageView newMenuImageView(Context context) {
            return new AppCompatImageView(context);
        }
    };

    public static ViewCreater getViewCreater() {
        return viewCreater;
    }

    public static void setViewCreater(ViewCreater viewCreater) {
        ViewCreater.viewCreater = viewCreater;
    }

    public abstract AppCompatTextView newTextView(Context context);

    public abstract AppCompatImageView newImageView(Context context);

    public abstract AppCompatTextView newMenuTextView(Context context);

    public abstract AppCompatImageView newMenuImageView(Context context);
}
