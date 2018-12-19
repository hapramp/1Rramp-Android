package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.models.MicroCommunity;
import com.hapramp.ui.fragments.CommunityPostFragment;
import com.hapramp.ui.fragments.MicroCommunityProfileFragment;

import java.util.ArrayList;

public class MicroCommunityPagerAdapter extends FragmentPagerAdapter {
  private final MicroCommunity mCommunity;
  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();

  public MicroCommunityPagerAdapter(FragmentManager fm, MicroCommunity microCommunity) {
    super(fm);
    this.mCommunity = microCommunity;
    //prepare tabs and title
    MicroCommunityProfileFragment communityProfileFragment = new MicroCommunityProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable(MicroCommunityProfileFragment.EXTRA_COMMUNITY_MODEL, mCommunity);
    communityProfileFragment.setArguments(bundle);

    CommunityPostFragment communityPostFragment = new CommunityPostFragment();
    Bundle cmpf = new Bundle();
    cmpf.putString(CommunityPostFragment.COMMUNITY_TAG, mCommunity.getTag());
    communityPostFragment.setArguments(cmpf);

    //posts
    fragments.add(communityPostFragment);
    titles.add("POSTS");

    //profile
    fragments.add(communityProfileFragment);
    titles.add("About Us");
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
