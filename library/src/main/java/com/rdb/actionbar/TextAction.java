package com.rdb.actionbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;

public class TextAction extends Action {

    private int tintColor;
    private AppCompatTextView textView;

    protected TextAction(Context context, int id, AppCompatTextView textView, OnActionListener actionClickListener, int color) {
        super(context, id, actionClickListener);
        this.tintColor = color;
        this.textView = textView;
        textView.setTextColor(color);
        get().addView(textView);
    }

    public TextAction setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public TextAction addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomViewInner(view, layoutParams);
        return this;
    }

    public TextAction setType(@Type int type) {
        super.setTypeInner(type);
        return this;
    }

    public TextAction setTag(String tag) {
        super.setTagInner(tag);
        return this;
    }

    public TextAction setMargin(int left, int right) {
        super.setMarginInner(left, right);
        return this;
    }

    public TextAction setText(int resid) {
        textView.setText(resid);
        return this;
    }

    public TextAction setText(CharSequence text) {
        textView.setText(text);
        return this;
    }

    public TextAction setImageResource(int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(textView.getContext(), drawableId).getConstantState().newDrawable().mutate();
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        tintDrawable(tintColor);
        return this;
    }

    public TextAction setImageDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        return this;
    }

    protected void setTintColor(int tintColor) {
        if (this.tintColor != tintColor) {
            this.tintColor = tintColor;
            tintDrawable(tintColor);
        }
    }

    private void tintDrawable(int tintColor) {
        Drawable drawable = textView.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }

    protected void updateMinWidth(int minWidth) {
        textView.setMinWidth(minWidth);
    }

    protected void setTextColor(int color) {
        textView.setTextColor(color);
        setTintColor(color);
    }
}
