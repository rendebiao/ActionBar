package com.rdb.actionbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class FloatingBar extends ActionBar {

    private boolean expand;
    private int strokeColor;
    private int strokeWidth;
    private int childWidth;
    private int childHeight;
    private int parentWidth;
    private int parentHeight;
    private boolean horizontal;
    private int parentDrawableId;
    private ImageAction parentAction;
    private Action.OnActionListener actionClickListener;
    private ValueAnimator expandAnimator = ValueAnimator.ofFloat(1, 0);
    private ValueAnimator unexpandAnimator = ValueAnimator.ofFloat(0, 1);

    private Action.OnActionListener parentClickListener = new Action.OnActionListener() {
        @Override
        public void onActionClick(Action action) {
            if (expand) {
                startAnimator(unexpandAnimator);
            } else {
                startAnimator(expandAnimator);
            }
            expand = !expand;
        }
    };

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                if (action != parentAction && action.isVisible()) {
                    action.get().setTranslationX((parentAction.getCenterX() - action.getCenterX()) * value);
                    action.get().setTranslationY((parentAction.getCenterY() - action.getCenterY()) * value);
                    action.get().setAlpha(1 - value);
                }
            }
        }
    };

    private Action.OnActionListener actionClickListenerProxy = new Action.OnActionListener() {
        @Override
        public void onActionClick(Action action) {
            if (actionClickListener != null) {
                actionClickListener.onActionClick(action);
            }
            if (expand) {
                parentAction.get().callOnClick();
            }
        }
    };

    private Holder.OnVisibleChangeListener visibleChnageListener = new Holder.OnVisibleChangeListener() {

        int visibleCount = 0;

        @Override
        public void onVisibleChanged(boolean visible) {
            visibleCount += (visible ? 1 : -1);
            if (parentAction != null) {
                parentAction.setVisible(visibleCount > 0);
            }
        }
    };

    public FloatingBar(@NonNull Context context) {
        this(context, null);
    }

    public FloatingBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        strokeWidth = 1;
        parentWidth = parentHeight = Math.round(Math.max(ActionStyle.floatingBarParentSize_DP, 36) * density);
        childWidth = childHeight = Math.round(Math.max(ActionStyle.floatingBarChildSize_DP, 36) * density);
        horizontal = getOrientation() == HORIZONTAL;
        initAnimator();
        super.setActionListener(actionClickListenerProxy);
        apply(colorPrimary, Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (expand && event.getAction() == MotionEvent.ACTION_DOWN) {
            switchExpand();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void initAnimator() {
        expandAnimator.setDuration(300);
        expandAnimator.setInterpolator(new OvershootInterpolator());
        expandAnimator.addUpdateListener(animatorUpdateListener);
        unexpandAnimator.setDuration(300);
        unexpandAnimator.setInterpolator(new AnticipateInterpolator());
        unexpandAnimator.addUpdateListener(animatorUpdateListener);
    }

    public void switchExpand() {
        if (parentAction != null && parentAction.isVisible()) {
            parentClickListener.onActionClick(parentAction);
        }
    }

    public boolean isExpand() {
        return expand;
    }

    public void setParentDrawableId(@DrawableRes int parentDrawableId) {
        this.parentDrawableId = parentDrawableId;
    }

    private AppCompatImageView newImageView() {
        AppCompatImageView imageView = ActionStyle.newImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setDuplicateParentStateEnabled(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    private AppCompatTextView newTextView() {
        AppCompatTextView textView = ActionStyle.newTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setEms(4);
        textView.setSingleLine();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ActionStyle.floatingBarActionTextSize_DP);
        textView.setDuplicateParentStateEnabled(true);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    public ImageAction addImageAction() {
        return addImageAction(false);
    }

    private ImageAction addImageAction(boolean parent) {
        ImageAction action = createImageAction(newImageView(), parent ? parentClickListener : null);
        addActionView(action, parent);
        return action;
    }

    public TextAction addTextAction() {
        TextAction action = createTextAction(newTextView(), null);
        addActionView(action, false);
        return action;
    }

    public CustomAction addCustomAction() {
        CustomAction action = createCustomAction(null);
        addActionView(action, false);
        return action;
    }

    private void addActionView(Action action, boolean parent) {
        if (action != null) {
            action.get().setBackgroundDrawable(new BackgroundDrawable(getBackgroundColor()));
            LayoutParams lp = new LayoutParams(parent ? parentWidth : childWidth, parent ? parentHeight : childHeight);
            int defaultMargin = Math.round(density * ActionStyle.floatingBarMargin_DP);
            if (horizontal) {
                lp.setMargins(0, 0, defaultMargin, defaultMargin);
            } else {
                lp.setMargins(0, 0, defaultMargin, defaultMargin);
            }
            addView(action.get(), getChildCount() < 3 ? getChildCount() : getChildCount() - 1, lp);
            if (!parent) {
                action.setOnVisibleChangeListener(visibleChnageListener);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                action.get().setElevation(density * 5);
            }
            if (actions.size() == 2 && parentAction == null && !parent) {
                parentAction = addImageAction(true).setImageResource(parentDrawableId, true).setType(Action.OVERFLOW);
            }
            if (parentAction != null) {
                parentAction.get().bringToFront();
            }
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        horizontal = orientation == HORIZONTAL;
        updateActionLayout();
    }

    @Override
    public void setGravity(int gravity) {
//        super.setGravity(gravity);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !expand && parentAction != null) {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                if (action != parentAction) {
                    action.get().setTranslationX(parentAction.getCenterX() - action.getCenterX());
                    action.get().setTranslationY(parentAction.getCenterY() - action.getCenterY());
                    action.get().setAlpha(0);
                }
            }
        }
    }

    public boolean performOptionMenu() {
        if (parentAction != null && parentAction.isVisible()) {
            parentClickListener.onActionClick(parentAction);
            return true;
        }
        return false;
    }

    @Override
    protected void updateBackground(int backgroundColor) {
        super.updateBackground(backgroundColor);
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            BackgroundDrawable background = (BackgroundDrawable) action.get().getBackground();
            background.setColor(backgroundColor);
        }
    }

    private void updateActionLayout() {
        if (actions != null) {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                boolean isParent = action == parentAction;
                LayoutParams lp = (LayoutParams) action.get().getLayoutParams();
                lp.width = isParent ? parentWidth : childWidth;
                lp.height = isParent ? parentHeight : childHeight;
                if (action instanceof TextAction) {
                    ((TextAction) action).updateMinWidth(lp.width);
                } else if (action instanceof ImageAction) {
                    ((ImageAction) action).updateWidth(lp.width);
                }
                action.get().setLayoutParams(lp);
            }
        }
    }

    public void setBackgroundStroke(int strokeWidth, int strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            BackgroundDrawable background = (BackgroundDrawable) action.get().getBackground();
            background.setStroke(strokeWidth, strokeColor);
        }
    }

    @Override
    public void setActionListener(Action.OnActionListener listener) {
        this.actionClickListener = listener;
    }

    class BackgroundDrawable extends StateListDrawable {

        private GradientDrawable normalDrawable;
        private GradientDrawable pressedDrawable;

        public BackgroundDrawable(int color) {
            pressedDrawable = new GradientDrawable();
            normalDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.OVAL);
            normalDrawable.setShape(GradientDrawable.OVAL);
            addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            addState(new int[]{}, normalDrawable);
            setColor(color);
            setStroke(strokeWidth, strokeColor);
        }

        public void setColor(int color) {
            pressedDrawable.setColor(color);
            normalDrawable.setColor(Color.argb((int) (0.7f * 255), Color.red(color), Color.green(color), Color.blue(color)));
            invalidateSelf();
        }

        public void setStroke(int strokeWidth, int strokeColor) {
            pressedDrawable.setStroke(strokeWidth, strokeColor);
            normalDrawable.setStroke(strokeWidth, strokeColor);
            invalidateSelf();
        }
    }
}
