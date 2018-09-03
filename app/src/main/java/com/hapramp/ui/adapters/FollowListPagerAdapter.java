package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.ui.fragments.FollowersFragment;
import com.hapramp.ui.fragments.FollowingsFragment;
import com.hapramp.views.UserItemView;

import java.util.ArrayList;
import java.util.Locale;

import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

public class FollowListPagerAdapter extends FragmentPagerAdapter {
  private final FollowingsFragment followingsFragment;
  private final FollowersFragment followersFragment;
  private final String mUsername;
  final FollowInfoCallback followInfoCallback = new FollowInfoCallback() {
    @Override
    public void onFollowInfoAvailable(int followers, int followings) {
      followersCount = followers;
      followingCount = followings;
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          notifyDataSetChanged();
        }
      });
    }

    @Override
    public void onFollowInfoError(String msg) {

    }
  };
  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();
  private int followingCount;
  private int followersCount;
  private Handler mHandler;
  private DataStore dataStore;

  public FollowListPagerAdapter(FragmentManager fm, String username) {
    super(fm);
    this.mUsername = username;
    mHandler = new Handler();
    Bundle bundle = new Bundle();
    dataStore = new DataStore();
    bundle.putString(EXTRA_KEY_USERNAME, username);
    followingsFragment = new FollowingsFragment();
    followersFragment = new FollowersFragment();
    followersFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followings fragment
        followingsFragment.refreshData();
        dataStore.requestFollowInfo(mUsername, followInfoCallback);
      }
    });
    followingsFragment.setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
      @Override
      public void onFollowStateChanged() {
        //inform followers fragment
        followersFragment.refreshData();
        dataStore.requestFollowInfo(mUsername, followInfoCallback);
      }
    });

    followingsFragment.setArguments(bundle);
    followersFragment.setArguments(bundle);
    fragments.add(followersFragment);
    fragments.add(followingsFragment);
    titles.add("Followers");
    titles.add("Following");
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
      return String.format(Locale.US, "Followers (%1$d) ", followersCount);
    }
    if (position == 1) {
      return String.format(Locale.US, "Following (%1$d) ", followingCount);
    }
    return titles.get(position);
  }

  public void setFollowInfo(int followers, int following) {
    this.followersCount = followers;
    this.followingCount = following;
  }

}
