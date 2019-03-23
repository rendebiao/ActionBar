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

    private int minSize;
    private boolean expand;
    private int childWidth;
    private int childHeight;
    private int parentWidth;
    private int parentHeight;
    private boolean horizontal;
    private int parentDrawableId;
    private Image parentAction;
    private Action.OnActionListener actionClickListener;
    private ValueAnimator expandAnimator = ValueAnimator.ofFloat(1, 0);
    private ValueAnimator unexpandAnimator = ValueAnimator.ofFloat(0, 1);

    private Action.OnActionListener menuClickListener = new Action.OnActionListener() {
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
                    action.container.setTranslationX((parentAction.getCenterX() - action.getCenterX()) * value);
                    action.container.setTranslationY((parentAction.getCenterY() - action.getCenterY()) * value);
                    action.container.setAlpha(1 - value);
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
                parentAction.container.callOnClick();
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
        minSize = (int) (36 * getResources().getDisplayMetrics().density);
        parentWidth = parentHeight = childWidth = childHeight = (int) (54 * getResources().getDisplayMetrics().density);
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
            menuClickListener.onActionClick(parentAction);
        }
    }

    public boolean isExpand() {
        return expand;
    }

    public void setParentDrawableId(@DrawableRes int parentDrawableId) {
        this.parentDrawableId = parentDrawableId;
    }

    private AppCompatImageView newImageView() {
        AppCompatImageView imageView = ViewCreater.getViewCreater().newImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setDuplicateParentStateEnabled(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    private AppCompatTextView newTextView() {
        AppCompatTextView textView = ViewCreater.getViewCreater().newTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setEms(4);
        textView.setSingleLine();
        textView.setPadding(4, 0, 4, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setDuplicateParentStateEnabled(true);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    public Image addImageAction() {
        return addImageAction(false);
    }

    private Image addImageAction(boolean isMenu) {
        Image action = createImageAction(newImageView(), Color.WHITE, isMenu ? menuClickListener : null);
        addActionView(action, isMenu);
        return action;
    }

    public Text addTextAction() {
        Text action = createTextAction(newTextView(), Color.WHITE, null);
        addActionView(action, false);
        return action;
    }

    public Custom addCustomAction() {
        Custom action = createCustomAction(null);
        addActionView(action, false);
        return action;
    }

    private void addActionView(Action action, boolean isMenu) {
        if (action != null) {
            action.container.setBackgroundDrawable(new BackgroundDrawable(colorPrimary));
            LayoutParams lp = new LayoutParams(isMenu ? parentWidth : childWidth, isMenu ? parentHeight : childHeight);
            int defaultPadding = (int) (density * 20);
            if (horizontal) {
                lp.setMargins(0, 0, defaultPadding, defaultPadding);
            } else {
                lp.setMargins(0, 0, defaultPadding, defaultPadding);
            }
            addView(action.container, getChildCount() < 3 ? getChildCount() : getChildCount() - 1, lp);
            if (Build.VERSION.SDK_INT >= 21) {
                action.container.setElevation(density * 5);
            }
            if (actions.size() == 2 && parentAction == null && !isMenu) {
                parentAction = addImageAction(true).setImageResource(parentDrawableId, true).setType(Type.OVERFLOW);
            }
            if (parentAction != null) {
                parentAction.container.bringToFront();
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

    public void setActionSize(int parentWidth, int parentHeight, int childWidth, int childHeight) {
        this.parentWidth = Math.max(minSize, parentWidth);
        this.parentHeight = Math.max(minSize, parentHeight);
        this.childWidth = Math.max(minSize, childWidth);
        this.childHeight = Math.max(minSize, childHeight);
        updateActionLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !expand && parentAction != null) {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                if (action != parentAction) {
                    action.container.setTranslationX(parentAction.getCenterX() - action.getCenterX());
                    action.container.setTranslationY(parentAction.getCenterY() - action.getCenterY());
                    action.container.setAlpha(0);
                }
            }
        }
    }

    public boolean performOptionMenu() {
        if (parentAction != null && parentAction.isVisible()) {
            menuClickListener.onActionClick(parentAction);
            return true;
        }
        return false;
    }

    @Override
    protected void updateBackground(int backgroundColor) {
        super.updateBackground(backgroundColor);
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            BackgroundDrawable background = (BackgroundDrawable) action.container.getBackground();
            background.setColor(backgroundColor);
        }
    }

    private void updateActionLayout() {
        if (actions != null) {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                boolean isParent = action == parentAction;
                LayoutParams lp = (LayoutParams) action.container.getLayoutParams();
                lp.width = isParent ? parentWidth : childWidth;
                lp.height = isParent ? parentHeight : childHeight;
                if (action instanceof Text) {
                    ((Text) action).updateMinWidth(lp.width);
                } else if (action instanceof Image) {
                    ((Image) action).updateWidth(lp.width);
                }
                action.container.setLayoutParams(lp);
            }
        }
    }

    public void setBackgroundStrokeWidth(int strokeWidth) {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            BackgroundDrawable background = (BackgroundDrawable) action.container.getBackground();
            background.setStrokeWidth(strokeWidth);
        }
    }

    public void setBackgroundStrokeColor(int strokeColor) {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            BackgroundDrawable background = (BackgroundDrawable) action.container.getBackground();
            background.setStrokeColor(strokeColor);
        }
    }

    @Override
    public void setActionListener(Action.OnActionListener listener) {
        this.actionClickListener = listener;
    }

    class BackgroundDrawable extends StateListDrawable {

        private int strokeColor;
        private int strokeWidth;
        private GradientDrawable normalDrawable;
        private GradientDrawable pressedDrawable;

        public BackgroundDrawable(int color) {
            strokeWidth = (int) (density * 1);
            pressedDrawable = new GradientDrawable();
            normalDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.OVAL);
            normalDrawable.setShape(GradientDrawable.OVAL);
            addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            addState(new int[]{}, normalDrawable);
            setColor(color);
        }

        public void setColor(int color) {
            pressedDrawable.setColor(color);
            normalDrawable.setColor(Color.argb((int) (0.7f * 255), Color.red(color), Color.green(color), Color.blue(color)));
            invalidateSelf();
        }

        public void setStrokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            pressedDrawable.setStroke(strokeWidth, strokeColor);
            normalDrawable.setStroke(strokeWidth, strokeColor);
            invalidateSelf();
        }

        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            pressedDrawable.setStroke(strokeWidth, strokeColor);
            normalDrawable.setStroke(strokeWidth, strokeColor);
            invalidateSelf();
        }
    }
}
