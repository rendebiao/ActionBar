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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.HashMap;

abstract class ActionBar extends LinearLayout {

    protected float density;
    protected int colorPrimary;
    protected boolean isFocused;
    protected AppCompatActivity activity;
    protected LayoutInflater layoutInflater;
    protected SparseArray<Action> actions = new SparseArray<>();
    private int idIndex;
    private int foregroundColor;
    private int backgroundColor;
    private Action.OnActionListener actionClickListener;
    private HashMap<String, Action> actionTags = new HashMap<>();
    private SparseArray<MenuBuilder> menuBuilders = new SparseArray<>();

    private MenuBuilder.OnOptionsMenuListener optionsMenuListener = new MenuBuilder.OnOptionsMenuListener() {

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            if (activity != null) {
                return activity.onCreateOptionsMenu(menu);
            }
            return false;
        }

        @Override
        public boolean onPrepareOptionsMenu(Menu menu) {
            if (activity != null) {
                return activity.onPrepareOptionsMenu(menu);
            }
            return false;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (activity != null) {
                return activity.onOptionsItemSelected(item);
            }
            return false;
        }

        @Override
        public void onOptionsMenuClosed(Menu menu) {
            if (activity != null) {
                activity.onOptionsMenuClosed(menu);
            }
        }
    };

    private Action.OnActionListener actionClickListenerProxy = new Action.OnActionListener() {
        @Override
        public void onActionClick(Action action) {
            Type actionType = action.getType();
            if (actionType == Type.BACK) {
                if (activity != null) {
                    activity.onBackPressed();
                } else if (actionClickListener != null) {
                    actionClickListener.onActionClick(action);
                }
            } else if (actionType == Type.OVERFLOW) {
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
            if (action instanceof Text) {
                ((Text) action).setTextColor(foregroundColor);
            } else if (action instanceof Image) {
                ((Image) action).setTintColor(foregroundColor);
            }
        }
    }

    protected Text createTextAction(Type type, String tag, AppCompatTextView textView, int foregroundColor, Action.OnActionListener actionClickListener) {
        idIndex++;
        Text action = new Text(getContext(), idIndex, type, tag, textView, actionClickListener == null ? actionClickListenerProxy : actionClickListener, foregroundColor);
        addAction(action);
        return action;
    }

    protected Image createImageAction(Type type, String tag, AppCompatImageView imageView, int foregroundColor, Action.OnActionListener actionClickListener) {
        idIndex++;
        Image action = new Image(getContext(), idIndex, type, tag, imageView, actionClickListener == null ? actionClickListenerProxy : actionClickListener, foregroundColor);
        addAction(action);
        return action;
    }

    protected Custom createCustomAction(Type type, String tag, Action.OnActionListener actionClickListener) {
        idIndex++;
        Custom action = new Custom(getContext(), idIndex, type, tag, actionClickListener == null ? actionClickListenerProxy : actionClickListener);
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
            Type actionType = action.getType();
            if (actionType == Type.OVERFLOW) {
                MenuBuilder menuBuilder = menuBuilders.get(action.getId());
                if (menuBuilder == null) {
                    menuBuilder = MenuBuilder.instanceOptionMenu(getContext(), action.get(), false, optionsMenuListener);
                    menuBuilders.put(action.getId(), menuBuilder);
                }
                menuBuilder.toggleShow();
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

    public void setActionListener(Action.OnActionListener listener) {
        actionClickListener = listener;
    }
}
