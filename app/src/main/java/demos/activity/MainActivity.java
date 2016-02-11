package demos.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.victor.androiddemos.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demos.adapter.VpMainAdapter;
import demos.util.ShowToast;
import demos.view.FriendlyViewPager;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.view_pager_main)
    FriendlyViewPager viewPagerMain;
    @Bind(R.id.iv_btn_1)
    ImageView ivBtn1;
    @Bind(R.id.iv_btn_2)
    ImageView ivBtn2;
    @Bind(R.id.iv_btn_3)
    ImageView ivBtn3;
    @Bind(R.id.ll_main)
    LinearLayout llMain;

    @OnClick({R.id.iv_btn_1, R.id.iv_btn_2, R.id.iv_btn_3})
    void onItemClick(View v) {
        if (v == ivBtn1) {
            viewPagerMain.setCurrentItem(0, false);
        } else if (v == ivBtn2) {
            viewPagerMain.setCurrentItem(1, false);
        } else if (v == ivBtn3) {
            viewPagerMain.setCurrentItem(2, false);
        }

    }

    private ArrayList<Drawable> drawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
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

    /**
     * 设置双击退出
     */
    private long exitTime = 0L;

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
