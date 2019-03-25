package com.rdb.actionbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.rdb.menu.MenuHelper;
import com.rdb.menu.MenuListener;

import java.util.HashMap;

public abstract class ActionBar extends LinearLayout {

    protected float density;
    protected int colorPrimary;
    protected boolean isFocused;
    protected AppCompatActivity activity;
    protected LayoutInflater layoutInflater;
    protected SparseArray<Action> actions = new SparseArray<>();
    private int idIndex;
    private int foregroundColor;
    private int backgroundColor;
    private MenuListener menuListener;
    private Action.OnActionListener actionClickListener;
    private HashMap<String, Action> actionTags = new HashMap<>();
    private SparseArray<MenuHelper> menuHelpers = new SparseArray<>();

    private Action.OnActionListener actionListenerProxy = new Action.OnActionListener() {
        @Override
        public void onActionClick(Action action) {
            int actionType = action.getType();
            if (actionType == Action.BACK) {
                if (activity != null) {
                    activity.onBackPressed();
                } else if (actionClickListener != null) {
                    actionClickListener.onActionClick(action);
                }
            } else if (actionType == Action.OVERFLOW) {
                if (activity != null) {
                    toggleOptionMenu(action);
                } else if (actionClickListener != null) {
                    actionClickListener.onActionClick(action);
                }
            } else if (actionClickListener != null) {
                actionClickListener.onActionClick(action);
            }
        }
    };

    public ActionBar(Context context) {
        this(context, null);
    }

    public ActionBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutInflater = LayoutInflater.from(context);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.data;
        density = getResources().getDisplayMetrics().density;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        isFocused = hasWindowFocus;
    }

    public void bindActivity(AppCompatActivity activity) {
        this.activity = activity;
        menuListener = new MenuListener.ActivityMenuListener(activity);
    }

    public void apply(int backgroundColor, int foregroundColor) {
        if (this.foregroundColor != foregroundColor || this.backgroundColor != backgroundColor) {
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;
            updateForeground(foregroundColor);
            updateBackground(backgroundColor);
        }
    }

    protected void updateBackground(int backgroundColor) {

    }

    protected void updateForeground(int foregroundColor) {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            if (action instanceof TextAction) {
                ((TextAction) action).setTextColor(foregroundColor);
            } else if (action instanceof ImageAction) {
                ((ImageAction) action).setImageColor(foregroundColor);
            }
        }
    }

    protected TextAction createTextAction(AppCompatTextView textView, Action.OnActionListener actionClickListener) {
        idIndex++;
        TextAction action = new TextAction(getContext(), idIndex, textView, actionClickListener == null ? actionListenerProxy : actionClickListener, foregroundColor);
        addAction(action);
        return action;
    }

    protected ImageAction createImageAction(AppCompatImageView imageView, Action.OnActionListener actionClickListener) {
        idIndex++;
        ImageAction action = new ImageAction(getContext(), idIndex, imageView, actionClickListener == null ? actionListenerProxy : actionClickListener, foregroundColor);
        addAction(action);
        return action;
    }

    protected CustomAction createCustomAction(Action.OnActionListener actionClickListener) {
        idIndex++;
        CustomAction action = new CustomAction(getContext(), idIndex, actionClickListener == null ? actionListenerProxy : actionClickListener);
        addAction(action);
        return action;
    }

    private void addAction(Action action) {
        if (action != null) {
            actions.put(idIndex, action);
            if (!TextUtils.isEmpty(action.getTag())) {
                actionTags.put(action.getTag(), action);
            }
        }
    }

    public Action getAction(int id) {
        return actions.get(id);
    }

    public Action getAction(String tag) {
        return actionTags.get(tag);
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    protected boolean toggleOptionMenu(Action action) {
        if (action.isVisible()) {
            int actionType = action.getType();
            if (actionType == Action.OVERFLOW && activity != null) {
                MenuHelper menuHelper = menuHelpers.get(action.getId());
                if (menuHelper == null) {
                    menuHelper = MenuHelper.instance(getContext(), action.get(), false, menuListener);
                    int[] location = new int[2];
                    action.get().getLocationOnScreen(location);
                    boolean left = location[0] + action.get().getWidth() / 2 <= getContext().getResources().getDisplayMetrics().widthPixels / 2;
                    menuHelper.setGravity(left ? MenuHelper.LEFT_BOTTOM : MenuHelper.RIGHT_BOTTOM);
                    menuHelpers.put(action.getId(), menuHelper);
                }
                menuHelper.toggleShow();
                return true;
            }
        }
        return false;
    }

    protected void startAnimator(ValueAnimator animator) {
        if (animator.isRunning()) {
            animator.cancel();
        }
        animator.start();
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }

    public void setActionListener(Action.OnActionListener listener) {
        actionClickListener = listener;
    }
}
