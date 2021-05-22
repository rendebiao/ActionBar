package com.rdb.actionbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;

public class TextAction extends Action {

    private final int textColor;
    private final AppCompatTextView textView;
    private boolean tint;

    protected TextAction(Context context, int id, AppCompatTextView textView, OnActionListener actionClickListener, int textColor) {
        super(context, id, actionClickListener);
        this.textView = textView;
        this.textColor = textColor;
        textView.setTextColor(textColor);
        get().addView(textView);
    }

    @Override
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

    public TextAction setImageResource(@DrawableRes int drawableId, boolean tint) {
        Drawable drawable = null;
        try {
            drawable = AppCompatResources.getDrawable(textView.getContext(), drawableId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setImageDrawable(drawable, tint);
        }
        return this;
    }

    public TextAction setImageDrawable(Drawable drawable, boolean tint) {
        this.tint = tint;
        if (tint) {
            drawable = drawable.getConstantState().newDrawable().mutate();
            tintDrawable(textColor);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        return this;
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

    protected void setTextColor(int textColor) {
        textView.setTextColor(textColor);
        if (tint) {
            tintDrawable(textColor);
        }
    }
}
