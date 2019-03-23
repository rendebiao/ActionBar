package com.rdb.actionbar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CustomBar extends ActionBar {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private static int defaultHeight;
    private int curHeight;
    private int titleMarginLeft;
    private int titleMarginRight;
    private boolean titleAlignLeft;
    private Title title;
    private Title secondTitle;
    private Divider divider;
    private Progress progress;
    private StatusBar statusView;
    private LinearLayout leftLayout;
    private LinearLayout rightLayout;
    private LinearLayout titleLayout;
    private LinearLayout centerLayout;
    private RelativeLayout contentView;
    private int actionBarItemBackground;

    public CustomBar(Context context) {
        this(context, null);
    }

    public CustomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public static void setDefaultHeight(int defaultHeight) {
        CustomBar.defaultHeight = defaultHeight;
    }

    private void initViews(Context context) {
        setOrientation(VERTICAL);
        curHeight = defaultHeight > 0 ? defaultHeight : (int) (42 * density);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarItemBackground, typedValue, true);
        actionBarItemBackground = typedValue.resourceId;
        statusView = new StatusBar(getContext());
        contentView = new RelativeLayout(context);
        addView(statusView);
        addView(contentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, curHeight));
        initLeftLayout(context);
        initCenterLayout(context);
        initTitleLayout(context);
        initRightLayout(context);
        initOtherLayout(context);
        apply(colorPrimary, Color.WHITE);
    }

    private void initLeftLayout(Context context) {
        leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(HORIZONTAL);
        leftLayout.setMinimumWidth((int) (12 * density));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        contentView.addView(leftLayout, layoutParams);
    }

    private void initCenterLayout(Context context) {
        centerLayout = new LinearLayout(context);
        centerLayout.setOrientation(HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView.addView(centerLayout, layoutParams);
    }

    private void initRightLayout(Context context) {
        rightLayout = new LinearLayout(context);
        rightLayout.setOrientation(HORIZONTAL);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        contentView.addView(rightLayout, layoutParams);
    }

    private void initTitleLayout(Context context) {
        titleLayout = new LinearLayout(context);
        titleLayout.setOrientation(VERTICAL);
        titleLayout.setGravity(Gravity.CENTER_VERTICAL);
        AppCompatTextView titleView = new AppCompatTextView(context);
        titleView.setGravity(Gravity.CENTER);
        titleView.setSingleLine();
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        titleLayout.addView(titleView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AppCompatTextView titleSecondView = new AppCompatTextView(context);
        titleSecondView.setGravity(Gravity.CENTER);
        titleSecondView.setSingleLine();
        titleSecondView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, (int) (-3 * density), 0, 0);
        titleLayout.addView(titleSecondView, layoutParams1);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
        contentView.addView(titleLayout, layoutParams2);
        title = new Title(titleView);
        secondTitle = new Title(titleSecondView);
    }

    private void initOtherLayout(Context context) {
        View view = new View(context);
        divider = new Divider(view);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        contentView.addView(view, layoutParams);
        progress = new Progress(context);
        progress.setColor(getForegroundColor());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (density * 2));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        contentView.addView(progress.get(), layoutParams);
    }

    public void setTitleAlignLeft(boolean titleAlignLeft) {
        if (titleAlignLeft != this.titleAlignLeft) {
            this.titleAlignLeft = titleAlignLeft;
            updateTitleLayout();
        }
    }

    public void showStatusView(boolean show, boolean showShadow) {
        statusView.setVisibility(show ? VISIBLE : GONE);
        statusView.setShowShadow(showShadow);
    }

    private AppCompatImageView newImageView(int width) {
        AppCompatImageView imageView = ViewCreater.getViewCreater().newImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setDuplicateParentStateEnabled(true);
        imageView.setBackgroundResource(actionBarItemBackground);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    private AppCompatTextView newTextView(int width) {
        AppCompatTextView textView = ViewCreater.getViewCreater().newTextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setEms(4);
        textView.setSingleLine();
        textView.setMinWidth(width);
        textView.setPadding(4, 0, 4, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setDuplicateParentStateEnabled(true);
        textView.setBackgroundResource(actionBarItemBackground);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return textView;
    }

    public Image addImageAction(Type type, String tag, @Align int align) {
        return addImageAction(type, tag, align, curHeight);
    }

    public Image addImageAction(Type type, String tag, @Align int align, int width) {
        Image action = createImageAction(type, tag, newImageView(width), getForegroundColor(), null);
        addActionView(action, align);
        return action;
    }

    public Text addTextAction(Type type, String tag, @Align int align) {
        Text action = createTextAction(type, tag, newTextView(curHeight), getForegroundColor(), null);
        addActionView(action, align);
        return action;
    }

    public Custom addCustomAction(Type type, String tag, @Align int align) {
        Custom action = createCustomAction(type, tag, null);
        addActionView(action, align);
        return action;
    }

    private void addActionView(Action action, @Align int align) {
        if (action != null) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (align == LEFT) {
                leftLayout.addView(action.container, layoutParams);
            } else if (align == RIGHT) {
                rightLayout.addView(action.container, layoutParams);
            } else {
                centerLayout.addView(action.container, layoutParams);
            }
        }
    }

    public Title getTitle() {
        return title;
    }

    public Title getSecondTitle() {
        return secondTitle;
    }

    public Divider getDivider() {
        return divider;
    }

    public Progress getProgress() {
        return progress;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateTitleLayout();
    }

    public boolean performOptionMenu() {
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(actions.keyAt(i));
            if (toggleOptionMenu(action)) {
                return true;
            }
        }
        return false;
    }

    public void updateContentHeight(int height) {
        if (curHeight != height) {
            curHeight = height;
            LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
            lp.height = height;
            contentView.setLayoutParams(lp);
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(actions.keyAt(i));
                if (action instanceof Text) {
                    ((Text) action).updateMinWidth(curHeight);
                } else if (action instanceof Image) {
                    ((Image) action).updateWidth(curHeight);
                }
            }
        }
    }

    private void updateTitleLayout() {
        int marginLeft = leftLayout.getWidth();
        int marginRight = rightLayout.getWidth();
        if (!titleAlignLeft) {
            marginLeft = marginRight = Math.max(marginLeft, marginRight);
        }
        if (titleMarginLeft != marginLeft || titleMarginRight != marginRight) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
            lp.setMargins(marginLeft, 0, marginRight, 0);
            titleLayout.setLayoutParams(lp);
            getTitle().get().setGravity(titleAlignLeft ? Gravity.LEFT : Gravity.CENTER);
            getSecondTitle().get().setGravity(titleAlignLeft ? Gravity.LEFT : Gravity.CENTER);
            titleMarginLeft = marginLeft;
            titleMarginRight = marginRight;
        }
    }

    @Override
    protected void updateBackground(int backgroundColor) {
        super.updateBackground(backgroundColor);
        statusView.setBackgroundColor(backgroundColor);
        contentView.setBackgroundColor(backgroundColor);
    }

    @Override
    protected void updateForeground(int foregroundColor) {
        super.updateForeground(foregroundColor);
        title.get().setTextColor(foregroundColor);
        secondTitle.get().setTextColor(foregroundColor);
        progress.setColor(foregroundColor);
    }

    public void setOnTitleClickListener(Title.OnTitleClickListener listener) {
        title.setOnTitleClickListener(listener);
    }

    @IntDef({LEFT, CENTER, RIGHT})
    public @interface Align {
    }
}
