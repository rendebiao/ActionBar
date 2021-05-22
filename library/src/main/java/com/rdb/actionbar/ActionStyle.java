package com.rdb.actionbar;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionStyle {

    public static float toolBarHeight_DP = 42;
    public static float toolBarTitleSize_DP = 17;
    public static float toolBarSecondTitleSize_DP = 13;
    public static float toolBarActionTextSize_DP = 13;
    public static int toolBarDividerHeight_PX = 1;
    public static float toolBarProgressHeight_DP = 2;

    public static float floatingBarParentSize_DP = 54;
    public static float floatingBarChildSize_DP = 54;
    public static float floatingBarMargin_DP = 20;
    public static float floatingBarActionTextSize_DP = 13;
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
