package pers.victor.androiddemos.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import pers.victor.androiddemos.fragment.ViewFragment;

/**
 * Created by Victor on 16/2/9.
 */
public class VpMainAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"自定义View", "其它"};

    public VpMainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", position == 0 ? ViewFragment.LAYOUT_VIEW : ViewFragment.LAYOUT_FUNCTION);
        Fragment fragment = new ViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
