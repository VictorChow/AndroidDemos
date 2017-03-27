package pers.victor.androiddemos.activity;

import pers.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.Arrays;

import pers.victor.androiddemos.view.CycleViewPagerParent;

public class CycleViewPagerActivity extends ToolbarActivity {
    CycleViewPagerParent pagerParent;

    @Override
    public int bindLayout() {
        return R.layout.activity_cycle_view_pager;
    }

    @Override
    public void initView() {
        pagerParent = $(R.id.cycle_view_pager);
        String[] titles = new String[]{"image1", "image2", "image3"};
        int[] images = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3};
        pagerParent.setUp(images, new ArrayList<>(Arrays.asList(titles)));
    }

    @Override
    protected void onDestroy() {
        pagerParent.onDestroyView();
        super.onDestroy();
    }
}
