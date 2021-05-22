package com.rdb.actionbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;

public class StatusBar extends View {

    private final RectF rectF;
    private final Paint shadowPaint;
    private final LinearGradient linearGradient;
    private int height;
    private boolean showShadow;

    public StatusBar(Context context) {
        super(context);
        rectF = new RectF();
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            height = getResources().getDimensionPixelSize(resId);
        } catch (Exception e) {

        } finally {
            if (height <= 0) {
                height = (int) (getResources().getDisplayMetrics().density * 25);
            }
            ViewGroup.LayoutParams lp = getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            } else {
                lp.height = height;
            }
            setLayoutParams(lp);
            linearGradient = new LinearGradient(0, 0, 0, height, 0x40000000, 0x00000000, Shader.TileMode.MIRROR);
            shadowPaint.setShader(linearGradient);
        }
        setVisibility(GONE);
    }

    public boolean isShowShadow() {
        return showShadow;
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showShadow) {
            rectF.set(0, 0, getWidth(), getHeight());
            canvas.drawRect(rectF, shadowPaint);
        }
    }
}
