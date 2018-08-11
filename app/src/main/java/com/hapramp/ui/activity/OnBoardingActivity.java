package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.OnBoardingPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingActivity extends AppCompatActivity {

  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.intro_btn_skip)
  Button introBtnSkip;
  @BindView(R.id.intro_indicator_0)
  ImageView introIndicator0;
  @BindView(R.id.intro_indicator_1)
  ImageView introIndicator1;
  @BindView(R.id.intro_indicator_2)
  ImageView introIndicator2;
  @BindView(R.id.intro_btn_finish)
  Button introBtnFinish;
  @BindView(R.id.intro_btn_next)
  ImageView introBtnNext;
  private int page;
  private View[] indicators;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_on_boarding);
    ButterKnife.bind(this);
    initialize();
    attachListeners();
  }

  private void initialize() {
    viewpager.setAdapter(new OnBoardingPageAdapter(this));
    indicators = new View[]{introIndicator0, introIndicator1, introIndicator2};
  }

  private void attachListeners() {
    viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        page = position;
        updatePageIndicators(position);
        introBtnNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
        introBtnFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    introBtnSkip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToLoginPage();
      }
    });

    introBtnFinish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToLoginPage();
      }
    });

    introBtnNext.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewpager.setCurrentItem(page + 1);
      }
    });
  }

  private void navigateToLoginPage() {
    HaprampPreferenceManager.getInstance().setOnBoardingVisited();
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  private void updatePageIndicators(int position) {
    for (int i = 0; i < 3; i++) {
      indicators[i].setBackgroundResource(
        i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
      );
    }
  }
}
