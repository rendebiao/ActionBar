package com.rdb.menu;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MenuStyle {

    public static int backgroundColor = Color.WHITE;
    public static int foregroundColor = Color.BLACK;
    public static float backgroundElevation_DP = 5;
    public static float backgroundCornerRadius_DP = 3;
    public static float menuItemHeight_DP = 42;
    public static float menuItemTextSize_DP = 15;
    public static float menuItemPaddingHorizontal_DP = 12;
    public static float menuItemIconSize_DP = 32;
    public static float menuItemIconPadding_DP = 8;
    public static float popupWidth_DP = 140;
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
