package com.rdb.actionbar.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rdb.actionbar.Action;
import com.rdb.actionbar.CustomBar;
import com.rdb.actionbar.FloatingBar;
import com.rdb.actionbar.Title;
import com.rdb.actionbar.Type;
import com.rdb.menu.MenuHelper;
import com.rdb.menu.OnMenuListener;

public class MainActivity extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomBar customBar = findViewById(R.id.customBar);
        customBar.bindActivity(this);
//        customBar.showStatusView(true,true);//全屏时调用
        customBar.getTitle().setText("CustomBar").setVisible(true);
        customBar.getSecondTitle().setText("标题居中 菜单 进度条").setVisible(true);
        customBar.addImageAction(Type.OVERFLOW, "more", CustomBar.RIGHT).setImageResource(R.drawable.core_ic_custombar_v_more, true).setVisible(true);
        customBar.addImageAction(Type.OTHER, "share", CustomBar.RIGHT).setImageResource(R.drawable.core_ic_custombar_share, true).setVisible(true);
        customBar.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
        customBar.setOnTitleClickListener(new Title.OnTitleClickListener() {
            @Override
            public void onTitleClick() {
                if (menuHelper == null) {
                    menuHelper = MenuHelper.instance(MainActivity.this, customBar, true, new OnMenuListener.ActivityMenuListener(MainActivity.this));
                    menuHelper.setGravity(MenuHelper.CENTER);
                }
                menuHelper.toggleShow();
            }

            @Override
            public void onTitleDoubleClick() {

            }
        });
        customBar.getProgress().setProgress(50);
        CustomBar customBar2 = findViewById(R.id.customBar2);
        customBar2.bindActivity(this);
        customBar2.setTitleAlignLeft(true);
        customBar2.getTitle().setText("CustomBar").setVisible(true);
        customBar2.getSecondTitle().setText("标题居左  Indeterminate进度条").setVisible(true);
        customBar2.addImageAction(Type.BACK, "back", CustomBar.LEFT).setImageResource(R.drawable.core_ic_custombar_arrow_back, true).setVisible(true);
        customBar2.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        customBar2.getProgress().setIndeterminate(true);
        CustomBar customBar3 = findViewById(R.id.customBar3);
        customBar3.bindActivity(this);
        customBar3.apply(0xffD81B60, Color.WHITE);
        customBar3.updateContentHeight((int) (getResources().getDisplayMetrics().density * 48));
        customBar3.getTitle().setText("CustomBar").setVisible(true);
        customBar3.getSecondTitle().setText("自定义高度和颜色").setVisible(true);
        customBar3.addImageAction(Type.BACK, "back", CustomBar.LEFT).setImageResource(R.drawable.core_ic_custombar_close, true).setVisible(true);
        customBar3.addImageAction(Type.OVERFLOW, "more", CustomBar.LEFT).setImageResource(R.drawable.core_ic_custombar_v_more, true).setVisible(true);
        customBar3.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingBar floatingBar = findViewById(R.id.floatingBar);
        floatingBar.bindActivity(this);
        floatingBar.setOrientation(LinearLayout.VERTICAL);
        floatingBar.setParentDrawableId(R.drawable.core_ic_custombar_v_more);
        floatingBar.addImageAction(Type.OVERFLOW, "add").setImageResource(R.drawable.core_ic_custombar_add, true).setVisible(true);
        floatingBar.addImageAction(Type.OVERFLOW, "search").setImageResource(R.drawable.core_ic_custombar_search, true).setVisible(true);
        floatingBar.addImageAction(Type.OVERFLOW, "refresh").setImageResource(R.drawable.core_ic_custombar_refresh, true).setVisible(true);
        floatingBar.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "FloatingBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
