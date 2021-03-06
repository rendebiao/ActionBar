package com.rdb.menu;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;

import java.util.List;

class MenuItemAdapter extends BaseAdapter {

    private final int iconId;
    private final int textId;
    private final float density;
    private final boolean showIcon;
    private final List<MenuItemImpl> menuItems;
    private int foregroundColor;

    public MenuItemAdapter(List<MenuItemImpl> menuItems, boolean showIcon, int foregroundColor, float density) {
        this.density = density;
        this.showIcon = showIcon;
        this.menuItems = menuItems;
        this.foregroundColor = foregroundColor;
        iconId = ViewCompat.generateViewId();
        textId = ViewCompat.generateViewId();
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
        notifyDataSetChanged();
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
            int padding = (int) (MenuStyle.menuItemPaddingHorizontal_DP * density);
            convertView.setPadding(padding, 0, padding, 0);
            ((LinearLayout) convertView).setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams layoutParams = null;
            if (showIcon) {
                iconView = MenuStyle.newImageView(parent.getContext());
                iconView.setId(iconId);
                iconView.setScaleType(ImageView.ScaleType.CENTER);
                int iconSize = (int) (MenuStyle.menuItemIconSize_DP * density);
                layoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);
                layoutParams.setMargins(0, 0, (int) (MenuStyle.menuItemIconPadding_DP * density), 0);
                ((LinearLayout) convertView).addView(iconView, layoutParams);
            }
            textView = MenuStyle.newTextView(parent.getContext());
            textView.setId(textId);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, MenuStyle.menuItemTextSize_DP);
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (MenuStyle.menuItemHeight_DP * density));
            ((LinearLayout) convertView).addView(textView, layoutParams);
        } else {
            iconView = convertView.findViewById(iconId);
            textView = convertView.findViewById(textId);
        }
        MenuItemImpl menuItem = getItem(position);
        if (showIcon) {
            Drawable drawable = menuItem.getIcon();
            iconView.setImageDrawable(drawable);
            if (drawable != null) {
                drawable.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN);
            }
        }
        textView.setTextColor(foregroundColor);
        textView.setText(menuItem.getTitle());
        return convertView;
    }
}
