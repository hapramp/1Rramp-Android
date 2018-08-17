package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.search.FollowCountManager;
import com.hapramp.ui.fragments.FollowersFragment;
import com.hapramp.ui.fragments.FollowingsFragment;
import com.hapramp.views.UserItemView;

import java.util.ArrayList;
import java.util.Locale;

import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

public class FollowListPagerAdapter extends FragmentPagerAdapter implements FollowCountManager.FollowCountCallback {
  private final FollowingsFragment followingsFragment;
  private final FollowersFragment followersFragment;
  private final String mUsername;
  private FollowCountManager followCountManager;
  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();
  private UserItemView.FollowStateChangeListener followStateChangeListener;
  private int followingCount;
  private int followersCount;
  private Handler mHandler;

  public FollowListPagerAdapter(FragmentManager fm, String username) {
    super(fm);
    this.mUsername = username;
    mHandler = new Handler();
    Bundle bundle = new Bundle();
    followCountManager = new FollowCountManager(this);
    bundle.putString(EXTRA_KEY_USERNAME, username);
    followingsFragment = new FollowingsFragment();
    followersFragment = new FollowersFragment();
    followersFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followings fragment
        followingsFragment.refreshData();
        followCountManager.requestFollowInfo(mUsername);
      }
    });
    followingsFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followers fragment
        followersFragment.refreshData();
        followCountManager.requestFollowInfo(mUsername);
      }
    });

    followingsFragment.setArguments(bundle);
    followersFragment.setArguments(bundle);
    fragments.add(followingsFragment);
    fragments.add(followersFragment);
    titles.add("Following");
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
    if (position == 0) {
      return String.format(Locale.US, "(%1$d) Following", followingCount);
    }
    if (position == 1) {
      return String.format(Locale.US, "(%1$d) Followers", followersCount);
    }
    return titles.get(position);
  }

  public void setFollowInfo(int followers, int following) {
    this.followersCount = followers;
    this.followingCount = following;
  }

  @Override
  public void onFollowInfo(int follower, int followings) {
    this.followersCount = follower;
    this.followingCount = followings;
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        notifyDataSetChanged();
      }
    });
  }

  @Override
  public void onFollowInfoError(String e) {

  }
}
