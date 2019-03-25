package com.rdb.actionbar.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rdb.actionbar.Action;
import com.rdb.actionbar.FloatingBar;
import com.rdb.actionbar.Title;
import com.rdb.actionbar.ToolBar;
import com.rdb.menu.MenuHelper;
import com.rdb.menu.MenuListener;

public class MainActivity extends AppCompatActivity {

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MenuStyle设置菜单样式
        //ActionStyle设置ToolBar和FloatingBar样式
        final ToolBar toolBar = findViewById(R.id.customBar);
        toolBar.bindActivity(this);
//        customBar.showStatusView(true,true);//全屏时调用
        toolBar.getTitle().setText("CustomBar").setVisible(true);//主标题 默认不显示
        toolBar.getSecondTitle().setText("标题居中 菜单 进度条").setVisible(true);//副标题 默认不显示
        toolBar.getDivider().setColor(Color.WHITE);//底部divider 默认显示 颜色透明
        toolBar.addImageAction(ToolBar.RIGHT).setType(Action.OVERFLOW).setTag("more").setImageResource(R.drawable.core_ic_custombar_v_more, true);
        toolBar.addImageAction(ToolBar.RIGHT).setTag("share").setImageResource(R.drawable.core_ic_custombar_share, true);
        toolBar.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
        toolBar.setOnTitleClickListener(new Title.OnTitleClickListener() {
            @Override
            public void onTitleClick() {
                if (menuHelper == null) {
                    menuHelper = MenuHelper.instance(MainActivity.this, toolBar, true, new MenuListener.ActivityMenuListener(MainActivity.this));
                    menuHelper.setGravity(MenuHelper.CENTER);
                }
                menuHelper.toggleShow();
            }

            @Override
            public void onTitleDoubleClick() {

            }
        });
        toolBar.getProgress().setProgress(50);
        ToolBar toolBar2 = findViewById(R.id.customBar2);
        toolBar2.bindActivity(this);
        toolBar2.setTitleAlignLeft(true);
        toolBar2.getTitle().setText("CustomBar").setVisible(true);
        toolBar2.getSecondTitle().setText("标题居左  Indeterminate进度条");
        toolBar2.addImageAction(ToolBar.LEFT).setType(Action.BACK).setTag("back").setImageResource(R.drawable.core_ic_custombar_arrow_back, true);
        toolBar2.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        ToolBar toolBar3 = findViewById(R.id.customBar3);
        toolBar3.bindActivity(this);
        toolBar3.apply(0xffD81B60, Color.WHITE);
        toolBar3.updateContentHeight((int) (getResources().getDisplayMetrics().density * 48));
        toolBar3.getProgress().setIndeterminate(true);
        toolBar3.getTitle().setText("CustomBar").setVisible(true);
        toolBar3.getSecondTitle().setText("自定义高度和颜色").setVisible(true);
        toolBar3.addTextAction(ToolBar.LEFT).setType(Action.BACK).setTag("back").setText("返回");
        toolBar3.addImageAction(ToolBar.LEFT).setType(Action.OVERFLOW).setTag("more").setImageResource(R.drawable.core_ic_custombar_v_more, true);
        toolBar3.setActionListener(new Action.OnActionListener() {
            @Override
            public void onActionClick(Action action) {
                Toast.makeText(MainActivity.this, "CustomBar action id = " + action.getId() + " tag = " + action.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingBar floatingBar = findViewById(R.id.floatingBar);
        floatingBar.bindActivity(this);
        floatingBar.setOrientation(LinearLayout.VERTICAL);
        floatingBar.setParentDrawableId(R.drawable.core_ic_custombar_v_more);
        floatingBar.addImageAction().setType(Action.OVERFLOW).setTag("add").setImageResource(R.drawable.core_ic_custombar_add, true);
        floatingBar.addImageAction().setTag("search").setImageResource(R.drawable.core_ic_custombar_search, true);
        floatingBar.addImageAction().setTag("refresh").setImageResource(R.drawable.core_ic_custombar_refresh, true);
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
