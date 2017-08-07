package pers.victor.androiddemos.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.AndroidDemos;
import pers.victor.androiddemos.adapter.VpMainAdapter;
import pers.victor.androiddemos.annotations.bind.Bind;
import pers.victor.androiddemos.annotations.bind.BindView;
import pers.victor.androiddemos.annotations.bus.Bus;
import pers.victor.androiddemos.annotations.bus.BusMethod;
import pers.victor.androiddemos.util.ShowToast;
import pers.victor.androiddemos.view.CircleTextImageView;
import pers.victor.androiddemos.view.CountDownView;
import pers.victor.androiddemos.view.MarqueeView;
import pers.victor.androiddemos.view.MenuLayout;
import pers.victor.androiddemos.view.ShaderTextView;
import pers.victor.androiddemos.view.tetris.TetrisShape;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager_main)
    ViewPager viewPagerMain;
    @BindView(R.id.menu_layout)
    MenuLayout menuLayout;
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.tool_bar_main)
    Toolbar toolbar;
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;
    @BindView(R.id.iv_myself)
    CircleTextImageView circleTextImageView;
    @BindView(R.id.shader_text)
    ShaderTextView shaderTextView;

    //设置双击退出
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        Bind.bind(this);
        Bus.register(this);
        init();
        getMetaData();
    }

    private void init() {
        getWindow().setBackgroundDrawable(null);
        initToolbar();
        initTabs();

        CountDownView countDownView = $(R.id.count_down_view);
        countDownView.setCountDownTime(1, 23, 45);

        circleTextImageView.setOnClickListener(v -> new AlertDialog.Builder(MainActivity.this)
                .setTitle("问一下")
                .setMessage("生成桌面快捷方式?")
                .setNegativeButton("否", null)
                .setPositiveButton("嗯", (dialog, which) -> {
                    createShortcut(MainActivity.this, ShortcutActivity.class, "Victor");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                })
                .show());
    }

    private void getMetaData() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String name = applicationInfo.metaData.getString("MY_NAME");
            shaderTextView.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> menuLayout.open());
    }

    private void initTabs() {
        viewPagerMain.setAdapter(new VpMainAdapter(getSupportFragmentManager()));
        setMenuItemClick();
        menuLayout.shouldInterceptTouchEventToShowMenu(() -> viewPagerMain.getCurrentItem() == 0);
        tabLayout.setupWithViewPager(viewPagerMain);
    }


    private void setMenuItemClick() {
        List<String> data = new ArrayList<>();
        data.add("自定义VIEW");
        data.add("其它");
        marqueeView.setContentData(data);
        marqueeView.setOnItemClickListener(pos -> {
            viewPagerMain.setCurrentItem(pos, false);
            menuLayout.close();
        });
    }

    @Override
    public void onBackPressed() {
        if (menuLayout.isOpen()) {
            menuLayout.close();
            return;
        }
        if ((System.currentTimeMillis() - exitTime) > 2500) {
            ShowToast.shortToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            exitTime = 0;
        }
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        marqueeView.onDestroyView();
        super.onDestroy();
    }

    private void createShortcut(final Context context, final Class<?> clazz, final String name) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutIntent.setClass(context, clazz);
        /*
          设置这条属性，可以使点击快捷方式后关闭其他的任务栈的其他activity，然后创建指定的acticity
         */
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent shortcut = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(AndroidDemos.getInstance(), R.drawable.iv_myself);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(shortcut);
    }

    @BusMethod
    public void onEvent(OpenMenu event) {
        menuLayout.open();
    }

    protected <T> T $(int id) {
        return (T) findViewById(id);
    }

    public static final class OpenMenu {
    }
}
