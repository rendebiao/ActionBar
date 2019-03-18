package com.rdb.actionbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
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
    private Image parentAction;
    private int parentDrawableId;
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
            action.container.setSelected(expand);
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (expand) {
                performOptionMenu();
                return true;
            }
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

    public boolean performOptionMenu() {
        if (parentAction != null && parentAction.isVisible()) {
            menuClickListener.onActionClick(parentAction);
        } else {
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                if (toggleOptionMenu(action)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setParentDrawableId(@DrawableRes int parentDrawableId) {
        this.parentDrawableId = parentDrawableId;
    }

    private AppCompatImageView newImageView() {
        AppCompatImageView imageView = new AppCompatImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setDuplicateParentStateEnabled(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    private AppCompatTextView newTextView() {
        AppCompatTextView textView = new AppCompatTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setEms(4);
        textView.setSingleLine();
        textView.setPadding(4, 0, 4, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setDuplicateParentStateEnabled(true);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    public Image addImageAction(String tag) {
        return addImageAction(tag, false);
    }

    private Image addImageAction(String tag, boolean isMenu) {
        Image action = createImageAction(isMenu ? Type.OVERFLOW : Type.OTHER, tag, newImageView(), Color.WHITE, isMenu ? menuClickListener : null);
        addActionView(action, isMenu);
        return action;
    }

    public Text addTextAction(String tag) {
        Text action = createTextAction(Type.OTHER, tag, newTextView(), Color.WHITE, null);
        addActionView(action, false);
        return action;
    }

    public Custom addCustomAction(String tag) {
        Custom action = createCustomAction(Type.OTHER, tag, null);
        addActionView(action, false);
        return action;
    }

    private void addActionView(Action action, boolean isMenu) {
        if (action != null) {
            action.container.setBackgroundDrawable(new Background(colorPrimary));
            LayoutParams lp = new LayoutParams(isMenu ? parentWidth : childWidth, isMenu ? parentHeight : childHeight);
            if (horizontal) {
                lp.setMargins(getChildCount() == 0 ? childWidth : childWidth / 4, 0, 0, 0);
            } else {
                lp.setMargins(0, getChildCount() == 0 ? childHeight : childHeight / 4, 0, 0);
            }
            addView(action.container, getChildCount() < 3 ? getChildCount() : getChildCount() - 1, lp);
            if (!isMenu && parentAction != null) {
                action.container.setSelected(true);
            }
            if (actions.size() == 2 && parentAction == null && !isMenu) {
                for (int i = 0; i < actions.size(); i++) {
                    Action a = actions.get(actions.keyAt(i));
                    a.container.setSelected(true);
                }
                parentAction = addImageAction(null, true).setImageResource(parentDrawableId, true);
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

    @Override
    protected void updateBackground(int backgroundColor) {
        super.updateBackground(backgroundColor);
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            Background background = (Background) action.container.getBackground();
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
                if (isParent) {
                    if (horizontal) {
                        lp.setMargins(getChildCount() == 0 ? childWidth : childWidth / 4, 0, 0, 0);
                    } else {
                        lp.setMargins(0, getChildCount() == 0 ? childHeight : childHeight / 4, 0, 0);
                    }
                }
                if (action instanceof Text) {
                    ((Text) action).updateMinWidth(lp.width);
                } else if (action instanceof Image) {
                    ((Image) action).updateWidth(lp.width);
                }
                action.container.setLayoutParams(lp);
            }
        }
    }

    @Override
    public void setActionListener(Action.OnActionListener listener) {
        this.actionClickListener = listener;
    }

    class Background extends Drawable {

        private int color;
        private int alphaColor;
        private boolean pressed;
        private boolean selected;
        private Paint paint = new Paint();
        private RectF rectF = new RectF();

        public Background(int color) {
            this.color = color;
            alphaColor = Color.argb((int) (0.7f * 255), Color.red(color), Color.green(color), Color.blue(color));
            paint.setAntiAlias(true);
        }

        public void setColor(int color) {
            this.color = color;
            alphaColor = Color.argb((int) (0.7f * 255), Color.red(color), Color.green(color), Color.blue(color));
            invalidateSelf();
        }

        @Override
        protected boolean onStateChange(int[] state) {
            boolean newPressed = false;
            boolean newSelected = false;
            for (int value : state) {
                if (value == android.R.attr.state_pressed) {
                    newPressed = true;
                } else if (value == android.R.attr.state_selected) {
                    newSelected = true;
                }
            }
            if (newPressed != pressed || newSelected != selected) {
                pressed = newPressed;
                selected = newSelected;
                invalidateSelf();
                return true;
            }
            return false;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect bounds = getBounds();
            rectF.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
            paint.setColor(pressed == selected ? alphaColor : color);
            canvas.drawOval(rectF, paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }
}
