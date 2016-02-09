package demos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import demos.fragment.FirstFragment;
import demos.fragment.SecondFragment;
import demos.fragment.ThirdFragment;

import java.util.ArrayList;

/**
 * Created by Victor on 16/2/9.
 */
public class VpMainAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;

    public VpMainAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.fragments.add(new FirstFragment());
        this.fragments.add(new SecondFragment());
        this.fragments.add(new ThirdFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
