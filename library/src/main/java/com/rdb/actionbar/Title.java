package com.rdb.actionbar;

import android.view.View;
import android.widget.TextView;

public class Title extends ViewHolder implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME = 200;
    private TextView textView;
    private long lastTitleClickTime;
    private OnTitleClickListener titleClickListener;
    private Runnable mTitleClickRunnable = new Runnable() {

        @Override
        public void run() {
            if (titleClickListener != null) {
                lastTitleClickTime = 0;
                titleClickListener.onTitleClick();
            }
        }
    };

    protected Title(TextView textView) {
        this.textView = textView;
        this.textView.setOnClickListener(this);
    }

    @Override
    protected TextView get() {
        return textView;
    }

    public Title setText(int resid) {
        textView.setText(resid);
        return this;
    }

    public Title setText(CharSequence text) {
        textView.setText(text);
        return this;
    }

    protected void setOnTitleClickListener(OnTitleClickListener listener) {
        titleClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (titleClickListener != null) {
            long clickTime = System.currentTimeMillis();
            textView.removeCallbacks(mTitleClickRunnable);
            if (clickTime - lastTitleClickTime <= DOUBLE_CLICK_TIME) {
                lastTitleClickTime = 0;
                titleClickListener.onTitleDoubleClick();
            } else if (clickTime - lastTitleClickTime > DOUBLE_CLICK_TIME) {
                lastTitleClickTime = clickTime;
                textView.postDelayed(mTitleClickRunnable, DOUBLE_CLICK_TIME);
            }
        }
    }

    public interface OnTitleClickListener {

        void onTitleClick();

        void onTitleDoubleClick();
    }
}
