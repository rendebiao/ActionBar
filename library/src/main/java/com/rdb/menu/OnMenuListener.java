package com.rdb.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public interface OnMenuListener {

    boolean onCreateMenu(Menu menu);

    boolean onPrepareMenu(Menu menu);

    boolean onItemSelected(MenuItem item);

    void onMenuClosed(Menu menu);

    class ActivityMenuListener implements OnMenuListener {

        protected Activity activity;

        public ActivityMenuListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onCreateMenu(Menu menu) {
            if (activity != null) {
                return activity.onCreateOptionsMenu(menu);
            }
            return false;
        }

        @Override
        public boolean onPrepareMenu(Menu menu) {
            if (activity != null) {
                return activity.onPrepareOptionsMenu(menu);
            }
            return false;
        }

        @Override
        public boolean onItemSelected(MenuItem item) {
            if (activity != null) {
                return activity.onOptionsItemSelected(item);
            }
            return false;
        }

        @Override
        public void onMenuClosed(Menu menu) {
            if (activity != null) {
                activity.onOptionsMenuClosed(menu);
            }
        }
    }
}
