package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.ui.fragments.FollowersFragment;
import com.hapramp.ui.fragments.FollowingsFragment;
import com.hapramp.views.UserItemView;

import java.util.ArrayList;

import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

public class FollowListPagerAdapter extends FragmentPagerAdapter {
  private final FollowingsFragment followingsFragment;
  private final FollowersFragment followersFragment;
  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();
  private UserItemView.FollowStateChangeListener followStateChangeListener;

  public FollowListPagerAdapter(FragmentManager fm, String username) {
    super(fm);
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_KEY_USERNAME, username);
    followingsFragment = new FollowingsFragment();
    followersFragment = new FollowersFragment();

    followersFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followings fragment
        followingsFragment.refreshData();
      }
    });
    followingsFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followers fragment
        followersFragment.refreshData();
      }
    });

    followingsFragment.setArguments(bundle);
    followersFragment.setArguments(bundle);
    fragments.add(followingsFragment);
    fragments.add(followersFragment);
    titles.add("Followings");
    titles.add("Followers");
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
