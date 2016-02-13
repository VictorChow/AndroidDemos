package demos.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Victor on 16/2/9.
 * 用于解决首页ViewPager和侧边栏的滑动冲突
 */
public class FriendlyViewPager extends ViewPager {

    public static boolean IS_FIRST_PAGER;

    public FriendlyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        IS_FIRST_PAGER = true;
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                IS_FIRST_PAGER = position == 0;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
