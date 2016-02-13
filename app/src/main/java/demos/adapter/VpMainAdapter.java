package demos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import demos.fragment.FirstFragment;
import demos.fragment.FunctionFragment;
import demos.fragment.CustomViewFragment;

/**
 * Created by Victor on 16/2/9.
 */
public class VpMainAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;

    public VpMainAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.fragments.add(new FirstFragment());
        this.fragments.add(new FunctionFragment());
        this.fragments.add(new CustomViewFragment());
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
