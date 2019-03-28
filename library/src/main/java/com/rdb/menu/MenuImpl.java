package com.rdb.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import java.util.ArrayList;
import java.util.List;

class MenuImpl implements Menu {


    private int count;
    private Context context;
    private List<MenuItemImpl> menuItems = new ArrayList<>();
    private List<MenuItemImpl> showMenuItems = new ArrayList<>();

    public MenuImpl(Context context) {
        this.context = context;
    }

    private MenuItemImpl createMenuItemImpl(int id, CharSequence title) {
        MenuItemImpl menuItem = new MenuItemImpl(context, id, title);
        menuItems.add(menuItem);
        return menuItem;
    }

    @Override
    public MenuItem add(CharSequence title) {
        return createMenuItemImpl(count++, title);
    }

    @Override
    public MenuItem add(int titleRes) {
        return createMenuItemImpl(count++, context.getString(titleRes));
    }

    public void refresh() {
        showMenuItems.clear();
        for (MenuItemImpl menuItem : menuItems) {
            if (menuItem.isVisible()) {
                showMenuItems.add(menuItem);
            }
        }
    }

    public List<MenuItemImpl> getMenuItems() {
        return showMenuItems;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        return createMenuItemImpl(itemId, title);
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return createMenuItemImpl(itemId, context.getString(titleRes));
    }

    @Override
    public SubMenu addSubMenu(CharSequence title) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int titleRes) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return null;
    }

    @Override
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        return 0;
    }

    @Override
    public void removeItem(int id) {
        for (MenuItemImpl menuItem : menuItems) {
            if (menuItem.getItemId() == id) {
                menuItems.remove(menuItem);
                break;
            }
        }
    }

    @Override
    public void removeGroup(int groupId) {

    }

    @Override
    public void clear() {
        menuItems.clear();
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {

    }

    @Override
    public void setGroupVisible(int group, boolean visible) {

    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {

    }

    @Override
    public boolean hasVisibleItems() {
        return showMenuItems.size() > 0;
    }

    @Override
    public MenuItem findItem(int id) {
        for (MenuItemImpl menuItem : menuItems) {
            if (menuItem.getItemId() == id) {
                return menuItem;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return menuItems.size();
    }

    @Override
    public MenuItem getItem(int index) {
        return menuItems.get(index);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        return false;
    }

    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {
        return false;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {

    }
}
