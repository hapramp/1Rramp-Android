package com.hapramp.ui.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hapramp.ui.fragments.CommunityPostFragment;
import com.hapramp.ui.fragments.MicroCommunityProfileFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MicroCommunityPagerAdapter extends FragmentPagerAdapter {
  private final String mCommunityTag;
  ArrayList<Fragment> fragments = new ArrayList<>();
  ArrayList<String> titles = new ArrayList<>();

  public MicroCommunityPagerAdapter(FragmentManager fm,String communityTag) {
    super(fm);
    this.mCommunityTag = communityTag;
    //prepare tabs and title
    MicroCommunityProfileFragment communityProfileFragment = new MicroCommunityProfileFragment();
    Bundle bundle = new Bundle();
    bundle.putString(MicroCommunityProfileFragment.EXTRA_HASHTAG,mCommunityTag);
    communityProfileFragment.setArguments(bundle);

    CommunityPostFragment communityPostFragment = new CommunityPostFragment();
    //profile
    fragments.add(communityProfileFragment);
    titles.add(mCommunityTag.toUpperCase());
    //posts
    fragments.add(communityPostFragment);
    titles.add("POSTS");
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
