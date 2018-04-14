package com.hapramp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.fragments.HotFragment;
import com.hapramp.fragments.NewFragment;
import com.hapramp.fragments.TrendingFragment;

import java.util.ArrayList;

/**
 * Created by Ankit on 4/14/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {

        super(fm);
        fragments.add(new TrendingFragment());
        fragments.add(new NewFragment());
        fragments.add(new HotFragment());
        titles.add("Trending");
        titles.add("New");
        titles.add("Hot");

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
