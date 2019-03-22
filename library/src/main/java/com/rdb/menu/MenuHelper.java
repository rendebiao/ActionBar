package com.rdb.menu;


import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;


public class MenuHelper implements AdapterView.OnItemClickListener {

    public static final int LEFT_TOP = android.view.Gravity.LEFT | android.view.Gravity.TOP;
    public static final int LEFT_BOTTOM = android.view.Gravity.LEFT | android.view.Gravity.BOTTOM;
    public static final int LEFT_CENTER = android.view.Gravity.LEFT | android.view.Gravity.CENTER_VERTICAL;
    public static final int CENTER = android.view.Gravity.CENTER;
    public static final int CENTER_TOP = android.view.Gravity.TOP | android.view.Gravity.CENTER_HORIZONTAL;
    public static final int CENTER_BOTTOM = android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL;
    public static final int RIGHT_TOP = android.view.Gravity.RIGHT | android.view.Gravity.TOP;
    public static final int RIGHT_BOTTOM = android.view.Gravity.RIGHT | android.view.Gravity.BOTTOM;
    public static final int RIGHT_CENTER = android.view.Gravity.RIGHT | android.view.Gravity.CENTER_VERTICAL;
    private int offsetX;
    private int offsetY;
    private int gravity = LEFT_BOTTOM;
    private View anchor;
    private float density;
    private MenuImpl menu;
    private PopupWindow window;
    private OnMenuListener menuListener;
    private MenuItemAdapter menuItemAdapter;

    private MenuHelper(Context context, View anchor, int style, boolean showIcon, OnMenuListener menuListener) {
        density = context.getResources().getDisplayMetrics().density;
        this.anchor = anchor;
        menu = new MenuImpl(context);
        window = new PopupWindow(context, null, style);
        ListView listView = new ListView(context);
        listView.setDivider(null);
        window.setContentView(listView);
        menuItemAdapter = new MenuItemAdapter(menu.getMenuItems(), showIcon, density);
        window.setWidth((int) (density * MenuConfig.popupWidth));
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setOutsideTouchable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setElevation(density * MenuConfig.backgroundElevation);
        }
        window.setFocusable(true);
        PaintDrawable drawable = new PaintDrawable(MenuConfig.backgroundColor);
        drawable.setCornerRadius(MenuConfig.backgroundCornerRadius * density);
        drawable.getPaint().setColor(MenuConfig.backgroundColor);
        window.setBackgroundDrawable(drawable);
        listView.setAdapter(menuItemAdapter);
        listView.setOnItemClickListener(this);
        window.setAnimationStyle(-1);
        if (menuListener != null) {
            menuListener.onCreateMenu(menu);
        }
        this.menuListener = menuListener;
    }

    public static MenuHelper instance(Context context, View anchor, boolean showIcon, OnMenuListener menuListener) {
        return new MenuHelper(context, anchor, 0, showIcon, menuListener);
    }

    public static MenuHelper instance(Context context, View anchor, int style, boolean showIcon, OnMenuListener menuListener) {
        return new MenuHelper(context, anchor, style, showIcon, menuListener);
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public boolean toggleShow() {
        if (window.isShowing()) {
            window.dismiss();
            if (menuListener != null) {
                menuListener.onMenuClosed(menu);
            }
        } else {
            if (menuListener != null) {
                menuListener.onPrepareMenu(menu);
            }
            int newOffsetX = offsetX;
            int newOffsetY = offsetY;
            if (gravity == LEFT_TOP) {
                newOffsetY = newOffsetY - anchor.getHeight();
            } else if (gravity == LEFT_CENTER) {
                newOffsetY = newOffsetY - anchor.getHeight() / 2;
            } else if (gravity == CENTER_TOP) {
                newOffsetX = newOffsetX + anchor.getWidth() / 2 - window.getWidth() / 2;
                newOffsetY = newOffsetY - anchor.getHeight();
            } else if (gravity == CENTER) {
                newOffsetX = newOffsetX + anchor.getWidth() / 2 - window.getWidth() / 2;
                newOffsetY = newOffsetY - anchor.getHeight() / 2;
            } else if (gravity == CENTER_BOTTOM) {
                newOffsetX = newOffsetX + anchor.getWidth() / 2 - window.getWidth() / 2;
            } else if (gravity == RIGHT_TOP) {
                newOffsetX = newOffsetX + anchor.getWidth() - window.getWidth();
                newOffsetY = newOffsetY - anchor.getHeight();
            } else if (gravity == RIGHT_CENTER) {
                newOffsetX = newOffsetX + anchor.getWidth() - window.getWidth();
                newOffsetY = newOffsetY - anchor.getHeight() / 2;
            } else if (gravity == RIGHT_BOTTOM) {
                newOffsetX = newOffsetX + anchor.getWidth() - window.getWidth();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.showAsDropDown(anchor, newOffsetX, newOffsetY, LEFT_BOTTOM);
            } else {
                window.showAsDropDown(anchor, newOffsetX, newOffsetY);
            }
        }
        return window.isShowing();
    }

    public boolean isPopupShow() {
        return window.isShowing();
    }

    public void setGravity(@Gravity int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MenuItem menuItem = menuItemAdapter.getItem(position);
        if (menuListener != null) {
            menuListener.onItemSelected(menuItem);
        }
        window.dismiss();
    }

    @IntDef({LEFT_TOP, LEFT_BOTTOM, LEFT_CENTER, CENTER, CENTER_TOP, CENTER_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, RIGHT_CENTER})
    public @interface Gravity {
    }

}