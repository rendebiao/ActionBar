package com.rdb.actionbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;

public class Image extends Action {

    private int tintColor;
    private AppCompatImageView imageView;

    protected Image(Context context, int id, AppCompatImageView imageView, OnActionListener actionClickListener, int tintColor) {
        super(context, id, actionClickListener);
        this.tintColor = tintColor;
        this.imageView = imageView;
        container.addView(imageView);
    }

    @Override
    public Image addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomView(view, layoutParams);
        return this;
    }

    @Override
    public Image setType(Type type) {
        super.setType(type);
        return this;
    }

    public Image setTag(String tag) {
        super.setTag(tag);
        return this;
    }

    @Override
    public Image setMargin(int left, int right) {
        super.setMargin(left, right);
        return this;
    }

    public Image setImageResource(@DrawableRes int drawableId, boolean tint) {
        if (tint) {
            imageView.setImageDrawable(AppCompatResources.getDrawable(imageView.getContext(), drawableId).getConstantState().newDrawable().mutate());
            tintDrawable(tintColor);
        } else {
            imageView.setImageResource(drawableId);
        }
        return this;
    }

    public Image setImageDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        return this;
    }

    protected void updateWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        lp.width = width;
        imageView.setLayoutParams(lp);
    }

    protected void setTintColor(int tintColor) {
        if (this.tintColor != tintColor) {
            this.tintColor = tintColor;
            tintDrawable(tintColor);
        }
    }

    private void tintDrawable(int tintColor) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
        }
    }

    public Drawable getDrawable() {
        return imageView.getDrawable();
    }
}
