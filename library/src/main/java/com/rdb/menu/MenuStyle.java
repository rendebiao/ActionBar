package com.rdb.menu;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MenuStyle {

    public static int backgroundColor = Color.WHITE;
    public static int foregroundColor = Color.BLACK;
    public static int backgroundElevation = 5;
    public static int backgroundCornerRadius = 2;
    public static int menuItemHeight = 42;
    public static int menuItemTextSize = 13;
    public static int menuItemPaddingHorizontal = 12;
    public static int menuItemIconSize = 32;
    public static int menuItemIconPadding = 8;
    public static int popupWidth = 140;
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
