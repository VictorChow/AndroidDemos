package demos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import demos.fragment.CustomViewFragment;
import demos.fragment.FirstFragment;
import demos.fragment.FunctionFragment;

/**
 * Created by Victor on 16/2/9.
 */
public class VpMainAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public VpMainAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.fragments.add(new CustomViewFragment());
        this.fragments.add(new FunctionFragment());
        this.fragments.add(new FirstFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
