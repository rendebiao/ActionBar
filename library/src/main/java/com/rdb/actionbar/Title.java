package com.rdb.actionbar;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class Title extends Holder<AppCompatTextView> implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME = 200;
    private long lastTitleClickTime;
    private OnTitleClickListener titleClickListener;
    private Runnable titleClickRunnable = new Runnable() {

        @Override
        public void run() {
            if (titleClickListener != null) {
                lastTitleClickTime = 0;
                titleClickListener.onTitleClick();
            }
        }
    };

    protected Title(AppCompatTextView textView) {
        super(textView);
        get().setOnClickListener(this);
    }

    public Title setVisible(boolean visible) {
        super.setVisibleInner(visible);
        return this;
    }

    public Title setText(int resid) {
        get().setText(resid);
        return this;
    }

    public Title setText(CharSequence text) {
        get().setText(text);
        return this;
    }

    protected void setOnTitleClickListener(OnTitleClickListener listener) {
        titleClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (titleClickListener != null) {
            long clickTime = System.currentTimeMillis();
            get().removeCallbacks(titleClickRunnable);
            if (clickTime - lastTitleClickTime <= DOUBLE_CLICK_TIME) {
                lastTitleClickTime = 0;
                titleClickListener.onTitleDoubleClick();
            } else if (clickTime - lastTitleClickTime > DOUBLE_CLICK_TIME) {
                lastTitleClickTime = clickTime;
                get().postDelayed(titleClickRunnable, DOUBLE_CLICK_TIME);
            }
        }
    }

    public interface OnTitleClickListener {
        void onTitleClick();
        void onTitleDoubleClick();
    }
}
