package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.ui.fragments.EarningFragment;
import com.hapramp.ui.fragments.UserBlogFragment;
import com.hapramp.ui.fragments.UserCreditsFragment;
import com.hapramp.ui.fragments.UserInfoFragment;

import java.util.ArrayList;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {

  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();

  public ProfileFragmentPagerAdapter(FragmentManager fm, String username) {
    super(fm);
    UserInfoFragment userInfoFragment = new UserInfoFragment();
    UserCreditsFragment userCreditsFragment = new UserCreditsFragment();
    UserBlogFragment userBlogFragment = new UserBlogFragment();
    EarningFragment earningFragment = new EarningFragment();

    Bundle args = new Bundle();
    args.putString(EarningFragment.ARG_USERNAME, username);
    userBlogFragment.setUsername(username);
    userCreditsFragment.setUsername(username);
    userInfoFragment.setUsername(username);
    earningFragment.setArguments(args);

    fragments.add(userInfoFragment);
    fragments.add(userBlogFragment);
    fragments.add(userCreditsFragment);
    fragments.add(earningFragment);

    titles.add("Profile");
    titles.add("Posts");
    titles.add("Credits");
    titles.add("Wallet");
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
