package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.ui.adapters.MicroCommunityPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MicroCommunityActivity extends AppCompatActivity {

  public static final String EXTRA_COMMUNITY_TAG = "communityTag";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.tabs_container)
  TabLayout tabsContainer;
  private MicroCommunityPagerAdapter microCommunityPagerAdapter;
  private String mCommunityTag;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_micro_community);
    ButterKnife.bind(this);
    collectExtras();
    initialize();
  }

  private void collectExtras() {
    Intent intent = getIntent();
    if (intent != null) {
      mCommunityTag = intent.getStringExtra(EXTRA_COMMUNITY_TAG);
    }
  }

  private void initialize() {
    if (mCommunityTag == null) {
      return;
    }
    microCommunityPagerAdapter = new MicroCommunityPagerAdapter(getSupportFragmentManager(), mCommunityTag);
    tabsContainer.setupWithViewPager(viewpager);
    viewpager.setAdapter(microCommunityPagerAdapter);

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
