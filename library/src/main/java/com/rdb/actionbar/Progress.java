package com.rdb.actionbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

public class Progress extends ViewHolder {

    private ProgressBar progressBar;

    public Progress(Context context) {
        this.progressBar = new ProgressBar(context);
    }

    @Override
    protected View get() {
        return progressBar;
    }

    public Progress setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public Progress setColor(int color) {
        progressBar.setColor(color);
        return this;
    }

    public Progress setIndeterminate(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
        return this;
    }

    public Progress setStrokeCap(Paint.Cap cap) {
        progressBar.setStrokeCap(cap);
        return this;
    }

    public Progress setProgress(float progress) {
        progressBar.setProgress(progress, true, true);
        return this;
    }

    public Progress setAlignTop(boolean alignTop) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
        lp.addRule(alignTop ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        progressBar.setLayoutParams(lp);
        return this;
    }

    public static class ProgressBar extends View implements ValueAnimator.AnimatorUpdateListener {

        private int color;
        private Paint paint;
        private float density;
        private float toWidth0;
        private float center0;
        private float curWidth0;
        private float fromWidth0;
        private float center1;
        private float toWidth1;
        private float curWidth1;
        private float fromWidth1;
        private float toProgress;
        private float curProgress;
        private float fromProgress;
        private long animatedCount;
        private boolean attached;
        private boolean indeterminate;
        private float lastAnimatedValue = 1;
        private RectF rectF = new RectF();
        private float[] pointRadius = new float[]{0, 0, 0, 0};
        private ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);

        public ProgressBar(Context context) {
            super(context);
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            color = typedValue.data;
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            density = context.getResources().getDisplayMetrics().density;
            pointRadius[3] = 2 * density;
            valueAnimator.addUpdateListener(this);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setInterpolator(new LinearInterpolator());
        }

        public void setColor(int color) {
            this.color = color;
            postInvalidate();
        }

        public void setIndeterminate(boolean indeterminate) {
            this.indeterminate = indeterminate;
            if (indeterminate) {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
                animatedCount = 0;
                valueAnimator.setDuration(1500);
                valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                if (attached) {
                    valueAnimator.start();
                }
            } else {
                valueAnimator.setDuration(200);
                valueAnimator.setRepeatCount(0);
            }
            requestLayout();
        }

        public void setStrokeCap(Paint.Cap cap) {
            paint.setStrokeCap(cap);
            postInvalidate();
        }

        public float getProgress() {
            return curProgress;
        }

        public void setProgress(float progress) {
            setProgress(progress, true, true);
        }

        public void setProgress(float progress, boolean animateWhenUp, boolean animateWhenDown) {
            if (!indeterminate) {
                progress = updateProgress(progress);
                boolean animate = progress - curProgress > 0 ? animateWhenUp : animateWhenDown;
                if (animate) {
                    this.toProgress = progress;
                    this.fromProgress = curProgress;
                    if (valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                    }
                    valueAnimator.start();
                } else {
                    this.fromProgress = this.curProgress = this.toProgress = progress;
                    postInvalidate();
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
            paint.setPathEffect(null);
            paint.setStrokeWidth(rectF.height());
            float start = rectF.left + paint.getStrokeWidth() / 2;
            float width = rectF.right - rectF.left - paint.getStrokeWidth();
            paint.setColor(color);
            if (indeterminate) {
                float leftValue, rigthValue;
                if (curWidth0 > 0) {
                    leftValue = center0 - curWidth0 / 2;
                    rigthValue = center0 + curWidth0 / 2;
                    if ((leftValue > 0 && leftValue < 1) || (rigthValue > 0 && rigthValue < 1)) {
                        canvas.drawLine(Math.max(leftValue * width, start), rectF.centerY(), Math.min(rigthValue * width, start + width), rectF.centerY(), paint);
                    }
                }
                if (curWidth1 > 0) {
                    leftValue = center1 - curWidth1 / 2;
                    rigthValue = center1 + curWidth1 / 2;
                    if ((leftValue > 0 && leftValue < 1) || (rigthValue > 0 && rigthValue < 1)) {
                        canvas.drawLine(Math.max(leftValue * width, start), rectF.centerY(), Math.min(rigthValue * width, start + width), rectF.centerY(), paint);
                    }
                }
            } else {
                canvas.drawLine(start, rectF.centerY(), start + width * curProgress / 100, rectF.centerY(), paint);
            }
        }

        private float updateProgress(float progress) {
            if (progress > 100) {
                progress = 100;
            } else if (curProgress < 0) {
                progress = 0;
            }
            return progress;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float animatedValue = (float) animation.getAnimatedValue();
            if (indeterminate) {
                boolean value = animatedCount % 2 == 0;
                if (lastAnimatedValue > 0.5f && animatedValue < 0.5f) {
                    curWidth0 = fromWidth0 = value ? 1 : 0.8f;
                    toWidth0 = value ? 0.2f : 0;
                    animatedCount++;
                } else if (lastAnimatedValue < 0.5f && animatedValue >= 0.5f) {
                    curWidth1 = fromWidth1 = value ? 0.2f : 0;
                    toWidth1 = value ? 1 : 0.8f;
                }
                center0 = -0.5f + animatedValue * 2;
                center1 = -0.5f + (animatedValue > 0.5f ? (animatedValue - 0.5f) : (animatedValue + 0.5f)) * 2;
                curWidth0 = fromWidth0 + (toWidth0 - fromWidth0) * animatedValue;
                curWidth1 = fromWidth1 + (toWidth1 - fromWidth1) * ((animatedValue + 0.5f) % 1f);
            } else {
                curProgress = fromProgress + (toProgress - fromProgress) * animatedValue;
            }
            lastAnimatedValue = animatedValue;
            postInvalidate();
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            attached = true;
            if (getVisibility() == View.VISIBLE && indeterminate) {
                if (!valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
            }
        }

        @Override
        protected void onVisibilityChanged(View changedView, int visibility) {
            super.onVisibilityChanged(changedView, visibility);
            if (attached && visibility == View.VISIBLE) {
                if (indeterminate) {
                    if (!valueAnimator.isRunning()) {
                        valueAnimator.start();
                    }
                }
            } else {
                if (indeterminate) {
                    if (valueAnimator.isRunning()) {
                        valueAnimator.cancel();
                    }
                }
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            attached = false;
            if (indeterminate) {
                if (valueAnimator.isRunning()) {
                    valueAnimator.cancel();
                }
            }
            super.onDetachedFromWindow();
        }
    }
}
