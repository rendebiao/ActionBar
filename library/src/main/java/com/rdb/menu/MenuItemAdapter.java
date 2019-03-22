package com.rdb.menu;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

class MenuItemAdapter extends BaseAdapter {

    private int iconId;
    private int textId;
    private float density;
    private boolean showIcon;
    private List<MenuItemImpl> menuItems;

    public MenuItemAdapter(List<MenuItemImpl> menuItems, boolean showIcon, float density) {
        this.density = density;
        this.showIcon = showIcon;
        this.menuItems = menuItems;
        iconId = ViewCompat.generateViewId();
        textId = ViewCompat.generateViewId();
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public MenuItemImpl getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppCompatImageView iconView = null;
        AppCompatTextView textView;
        if (convertView == null) {
            convertView = new LinearLayout(parent.getContext());
            int padding = (int) (MenuConfig.menuPaddingHorizontal * density);
            convertView.setPadding(padding, 0, padding, 0);
            ((LinearLayout) convertView).setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams layoutParams = null;
            if (showIcon) {
                iconView = new AppCompatImageView(parent.getContext());
                iconView.setId(iconId);
                iconView.setScaleType(ImageView.ScaleType.CENTER);
                int iconSize = (int) (MenuConfig.menuIconSize * density);
                layoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);
                layoutParams.setMargins(0, 0, (int) (MenuConfig.menuIconPadding * density), 0);
                ((LinearLayout) convertView).addView(iconView, layoutParams);
            }
            textView = new AppCompatTextView(parent.getContext());
            textView.setId(textId);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, MenuConfig.textSize);
            textView.setTextColor(MenuConfig.textColor);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (MenuConfig.menuHeight * density));
            ((LinearLayout) convertView).addView(textView, layoutParams);
        } else {
            iconView = convertView.findViewById(iconId);
            textView = convertView.findViewById(textId);
        }
        MenuItemImpl menuItem = getItem(position);
        if (showIcon) {
            iconView.setImageDrawable(menuItem.getIcon());
        }
        textView.setText(menuItem.getTitle());
        return convertView;
    }
}