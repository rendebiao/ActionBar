package com.rdb.actionbar;


import android.content.Context;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;


public class MenuBuilder implements PopupMenu.OnMenuItemClickListener {

    private int theme;
    private int offsetX;
    private int offsetY;
    private boolean popupShow;
    private PopupMenu popupMenu;
    private MenuPopupHelper menuPopupHelper;
    private OnOptionsMenuListener optionsMenuListener;

    private MenuBuilder(Context context, View anchor, boolean showIcon, OnOptionsMenuListener optionsMenuListener) {
        popupMenu = new PopupMenu(context, anchor);
        initMenu(showIcon);
        popupMenu.setOnMenuItemClickListener(this);
        optionsMenuListener.onCreateOptionsMenu(popupMenu.getMenu());
        this.optionsMenuListener = optionsMenuListener;
    }

    private MenuBuilder(Context context, View anchor, boolean showIcon, int theme, OnOptionsMenuListener optionsMenuListener) {
        popupMenu = new PopupMenu(context, anchor, Gravity.NO_GRAVITY, 0, theme);
        initMenu(showIcon);
        popupMenu.setOnMenuItemClickListener(this);
        optionsMenuListener.onCreateOptionsMenu(popupMenu.getMenu());
        this.optionsMenuListener = optionsMenuListener;
        this.theme = theme;
    }

    private MenuBuilder(Context context, View anchor, int menuRes, boolean showIcon, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        popupMenu = new PopupMenu(context, anchor);
        initMenu(showIcon);
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener);
        popupMenu.inflate(menuRes);
    }


    public static MenuBuilder instanceOptionMenu(Context context, View anchor, boolean showIcon, OnOptionsMenuListener optionsMenuListener) {
        return new MenuBuilder(context, anchor, showIcon, optionsMenuListener);
    }

    public static MenuBuilder instanceOptionMenu(Context context, View anchor, boolean showIcon, int theme, OnOptionsMenuListener optionsMenuListener) {
        return new MenuBuilder(context, anchor, showIcon, theme, optionsMenuListener);
    }

    public static MenuBuilder instancePopupMenu(Context context, View anchor, int menuRes, boolean showIcon, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        return new MenuBuilder(context, anchor, menuRes, showIcon, onMenuItemClickListener);
    }

    private void initMenu(boolean showIcon) {
        try {
            Field field = PopupMenu.class.getDeclaredField("mPopup");
            field.setAccessible(true);
            menuPopupHelper = (MenuPopupHelper) field.get(popupMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                popupShow = false;
            }
        });
        if (showIcon) {
            menuPopupHelper.setForceShowIcon(true);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (optionsMenuListener != null) {
            return optionsMenuListener.onOptionsItemSelected(item);
        } else {
            return false;
        }
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setGravity(int gravity) {
        menuPopupHelper.setGravity(gravity);
    }

    public int getTheme() {
        return theme;
    }

    public MenuPopupHelper getMenuPopupHelper() {
        return menuPopupHelper;
    }

    public boolean toggleShow() {
        if (popupShow) {
            popupMenu.dismiss();
            if (optionsMenuListener != null) {
                optionsMenuListener.onOptionsMenuClosed(popupMenu.getMenu());
            }
        } else {
            if (optionsMenuListener != null) {
                optionsMenuListener.onPrepareOptionsMenu(popupMenu.getMenu());
            }
            menuPopupHelper.show(offsetX, offsetY);
        }
        popupShow = !popupShow;
        return popupShow;
    }

    public boolean isPopupShow() {
        return popupShow;
    }

    public interface OnOptionsMenuListener {
        boolean onCreateOptionsMenu(Menu menu);

        boolean onPrepareOptionsMenu(Menu menu);

        boolean onOptionsItemSelected(MenuItem item);

        void onOptionsMenuClosed(Menu menu);
    }
}
