package demos.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.adapter.VpMainAdapter;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindView;
import demos.annotations.bus.Bus;
import demos.annotations.bus.BusMethod;
import demos.util.ShowToast;
import demos.view.CountDownView;
import demos.view.MarqueeView;
import demos.view.MenuLayout;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager_main)
    ViewPager viewPagerMain;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.menu_layout)
    MenuLayout menuLayout;
    @BindView(R.id.tab_main)
    TabLayout tabLayout;
    @BindView(R.id.tool_bar_main)
    Toolbar toolbar;
    @BindView(R.id.marquee_view)
    MarqueeView marqueeView;

    //设置双击退出
    private long exitTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        Bind.bind(this);
        Bus.register(this);
        init();
    }

    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setBackgroundDrawable(null);
        initToolbar();
        initTabs();

        CountDownView countDownView = $(R.id.count_down_view);
        countDownView.setCountDownTime(1, 23, 45);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuLayout.open();
            }
        });
    }

    private void initTabs() {
        viewPagerMain.setAdapter(new VpMainAdapter(getSupportFragmentManager()));
        setMenuItemClick();
        menuLayout.shouldInterceptTouchEventToShowMenu(new MenuLayout.InterceptListener() {
            @Override
            public boolean shouldInterceptTouchEvent() {
                return viewPagerMain.getCurrentItem() == 0;
            }
        });
        tabLayout.setupWithViewPager(viewPagerMain);
    }


    private void setMenuItemClick() {
        List<String> data = new ArrayList<>();
        data.add("自定义VIEW");
        data.add("其它");
        marqueeView.setContentData(data);
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                viewPagerMain.setCurrentItem(pos, false);
                menuLayout.close();
            }
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
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        Bus.unregister(this);
        marqueeView.onDestroyView();
        super.onDestroy();
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
