package com.rdb.actionbar;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionStyle {

    public static float toolBarHeight = 42;
    public static float toolBarTitleSize = 17;
    public static float toolBarSecondTitleSize = 13;
    public static float toolBarActionTextSize = 13;
    public static float toolBarDividerHeight = 0.5f;
    public static float toolBarProgressHeight = 2;

    public static float floatingBarParentSize = 54;
    public static float floatingBarChildSize = 54;
    public static float floatingBarMargin = 20;
    public static float floatingBarActionTextSize = 13;
    public static Class<? extends AppCompatTextView> textViewClass = AppCompatTextView.class;
    public static Class<? extends AppCompatImageView> imageViewClass = AppCompatImageView.class;

    protected static AppCompatTextView newTextView(Context context) {
        return newView(context, textViewClass, AppCompatTextView.class);
    }

    protected static AppCompatImageView newImageView(Context context) {
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
