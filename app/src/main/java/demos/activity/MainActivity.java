package demos.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.victor.androiddemos.R;

import java.util.ArrayList;

import demos.adapter.VpMainAdapter;
import demos.annotations.bind.Bind;
import demos.annotations.bind.BindClick;
import demos.annotations.bind.BindView;
import demos.util.DisplayUtil;
import demos.util.ShowToast;
import demos.view.MenuLayout;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager_main)
    ViewPager viewPagerMain;
    @BindView(R.id.iv_btn_1)
    ImageView ivBtn1;
    @BindView(R.id.iv_btn_2)
    ImageView ivBtn2;
    @BindView(R.id.iv_btn_3)
    ImageView ivBtn3;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.menu_layout)
    MenuLayout menuLayout;
    @BindView(R.id.ll_main_tab)
    LinearLayout llTab;

    private ArrayList<Drawable> drawables;
    /**
     * 设置双击退出
     */
    private long exitTime = 0L;

    @BindClick({R.id.iv_btn_1, R.id.iv_btn_2, R.id.iv_btn_3})
    void onItemClick(View v) {
        if (v == ivBtn1) {
            viewPagerMain.setCurrentItem(0, false);
        } else if (v == ivBtn2) {
            viewPagerMain.setCurrentItem(1, false);
        } else if (v == ivBtn3) {
            viewPagerMain.setCurrentItem(2, false);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind.bind(this);
        init();
    }

    private void init() {
        ViewCompat.setElevation(llTab, DisplayUtil.dp2px(10));

        getWindow().setBackgroundDrawable(null);
        viewPagerMain.setAdapter(new VpMainAdapter(getSupportFragmentManager()));
        viewPagerMain.addOnPageChangeListener(this);
        ivBtn1.setBackgroundResource(R.drawable.bitmap_bg_1);
        ivBtn2.setBackgroundResource(R.drawable.bitmap_bg_2);
        ivBtn3.setBackgroundResource(R.drawable.bitmap_bg_3);
        ivBtn1.setImageResource(R.drawable.icon1);
        ivBtn2.setImageResource(R.drawable.icon2);
        ivBtn3.setImageResource(R.drawable.icon3);
        Drawable drawable1 = ivBtn1.getDrawable();
        Drawable drawable2 = ivBtn2.getDrawable();
        Drawable drawable3 = ivBtn3.getDrawable();
        drawable1.setAlpha(255);
        drawable2.setAlpha(0);
        drawable3.setAlpha(0);
        drawables = new ArrayList<>();
        drawables.add(drawable1);
        drawables.add(drawable2);
        drawables.add(drawable3);

        setMenuItemClick();

        menuLayout.shouldInterceptTouchEventToShowMenu(() -> viewPagerMain.getCurrentItem() == 0);
    }

    private void setMenuItemClick() {
        int count = llMain.getChildCount();
        for (int i = 0; i < count; i++) {
            final int finalI = i;
            llMain.getChildAt(i).setOnClickListener(v -> ShowToast.shortToast("第" + finalI + "项"));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        double alpha = 255 * positionOffset;
        if (position + 1 < drawables.size()) {
            drawables.get(position).setAlpha(255 - (int) alpha);
            drawables.get(position + 1).setAlpha((int) alpha);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setDrawableAlpha(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void setDrawableAlpha(int pos) {
        for (int i = 0; i < drawables.size(); i++) {
            if (i == pos) {
                drawables.get(i).setAlpha(255);
            } else {
                drawables.get(i).setAlpha(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2500) {
            ShowToast.shortToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
