package pers.victor.androiddemos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import pers.victor.androiddemos.fragment.AppInfoFragment;

/**
 * Created by Victor on 16/2/13.
 */
public class VpAppFragmentAdapter extends FragmentPagerAdapter {
    private static final String[] title = new String[]{"系统应用", "非系统应用"};

    private List<AppInfoFragment> list;
    private AppInfoFragment systemAppFragment;
    private AppInfoFragment notSystemAppFragment;

    public VpAppFragmentAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        systemAppFragment = new AppInfoFragment();
        notSystemAppFragment = new AppInfoFragment();
        list.add(systemAppFragment.setSystemApp(true));
        list.add(notSystemAppFragment.setSystemApp(false));
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
