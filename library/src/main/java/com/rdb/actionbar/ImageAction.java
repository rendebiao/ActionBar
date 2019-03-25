package com.rdb.actionbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;

public class ImageAction extends Action {

    private boolean tint;
    private int imageColor;
    private AppCompatImageView imageView;

    protected ImageAction(Context context, int id, AppCompatImageView imageView, OnActionListener actionClickListener, int imageColor) {
        super(context, id, actionClickListener);
        this.imageView = imageView;
        this.imageColor = imageColor;
        get().addView(imageView);
    }

    @Override
    public ImageAction setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public ImageAction addCustomView(View view, FrameLayout.LayoutParams layoutParams) {
        super.addCustomViewInner(view, layoutParams);
        return this;
    }

    public ImageAction setType(@Type int type) {
        super.setTypeInner(type);
        return this;
    }

    public ImageAction setTag(String tag) {
        super.setTagInner(tag);
        return this;
    }

    public ImageAction setMargin(int left, int right) {
        super.setMarginInner(left, right);
        return this;
    }

    public ImageAction setImageResource(@DrawableRes int drawableId, boolean tint) {
        if (!tint) {
            this.tint = false;
            imageView.setImageResource(drawableId);
        } else {
            Drawable drawable = null;
            try {
                drawable = AppCompatResources.getDrawable(imageView.getContext(), drawableId);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setImageDrawable(drawable, tint);
            }
        }
        return this;
    }

    public ImageAction setImageDrawable(Drawable drawable, boolean tint) {
        this.tint = tint;
        if (!tint || drawable == null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setImageDrawable(drawable.getConstantState().newDrawable().mutate());
            tintDrawable(imageColor);
        }
        return this;
    }

    protected void updateWidth(int width) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        lp.width = width;
        imageView.setLayoutParams(lp);
    }

    protected void setImageColor(int imageColor) {
        if (this.imageColor != imageColor) {
            this.imageColor = imageColor;
            if (tint) {
                tintDrawable(imageColor);
            }
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
