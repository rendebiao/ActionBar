package com.rdb.actionbar;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class ActionStyle {

    public static int toolBarHeight = 42;
    public static int toolBarTitleSize = 17;
    public static int toolBarSecondTitleSize = 13;
    public static int toolBarActionTextSize = 13;
    public static int toolBarProgressHeight = 2;
    public static int toolBarDividerHeight = 1;

    public static int floatingBarParentSize = 54;
    public static int floatingBarChildSize = 54;
    public static int floatingBarMargin = 20;
    public static int floatingBarActionTextSize = 13;
    public static Class<? extends AppCompatTextView> textViewClass = AppCompatTextView.class;
    public static Class<? extends AppCompatImageView> imageViewClass = AppCompatImageView.class;

    public static AppCompatTextView newTextView(Context context) {
        return newView(context, textViewClass, AppCompatTextView.class);
    }

    public static AppCompatImageView newImageView(Context context) {
        return newView(context, imageViewClass, AppCompatImageView.class);
    }

    private static <T> T newView(Context context, Class<? extends T> cls, Class<T> classT) {
        try {
            Constructor constructor = null;
            if (cls != null) {
                constructor = cls.getConstructor(Context.class);
            }
            if (constructor == null) {
                constructor = classT.getConstructor(Context.class);
            }
            return (T) constructor.newInstance(context);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
