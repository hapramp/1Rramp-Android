package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.FollowListPagerAdapter;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowListActivity extends AppCompatActivity {
  public static final String EXTRA_KEY_USERNAME = "username";
  public static final String EXTRA_KEY_FOLLOWING = "followin";
  public static final String EXTRA_KEY_FOLLOWERS = "followers";
  @BindView(R.id.closeBtn)
  TextView closeBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.tabs)
  TabLayout tabs;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  private String username;
  private int followers;
  private int following;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_follow_list);
    ButterKnife.bind(this);
    if (getIntent() != null) {
      username = getIntent().getExtras().getString(EXTRA_KEY_USERNAME,
        HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
      followers = getIntent().getExtras().getInt(EXTRA_KEY_FOLLOWERS, 0);
      following = getIntent().getExtras().getInt(EXTRA_KEY_FOLLOWING, 0);
    }
    initView();
  }

  private void initView() {
    setupViewPager(viewpager);
    tabs.setupWithViewPager(viewpager);
    tabs.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void setupViewPager(ViewPager viewPager) {
    FollowListPagerAdapter adapter = new FollowListPagerAdapter(getSupportFragmentManager(), username);
    adapter.setFollowInfo(followers,following);
    viewPager.setAdapter(adapter);
  }
}
