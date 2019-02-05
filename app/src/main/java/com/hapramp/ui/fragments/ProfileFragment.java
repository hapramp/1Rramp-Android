package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.EventReporter;
import com.hapramp.ui.adapters.ProfileFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {
  @BindView(R.id.tabs_container)
  TabLayout tabsContainer;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  private Unbinder unbinder;
  private ProfileFragmentPagerAdapter profileFragmentPagerAdapter;
  private Context mContext;
  private String username;

  public ProfileFragment() {
    EventReporter.addEvent(AnalyticsParams.EVENT_BROWSE_HOME);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    unbinder = ButterKnife.bind(this, view);
    init();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void init() {
    profileFragmentPagerAdapter = new ProfileFragmentPagerAdapter(getChildFragmentManager(),
      username);
    tabsContainer.setupWithViewPager(viewpager);
    viewpager.setAdapter(profileFragmentPagerAdapter);
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
