package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.OnBoardingPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import xute.progressdot.ProgressDotView;

public class OnBoardingActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.dotsView)
  ProgressDotView dotsView;
  @BindView(R.id.back)
  TextView back;
  @BindView(R.id.next)
  TextView next;

  private int mCurrentIndex;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_on_boarding);
    ButterKnife.bind(this);
    viewpager.setAdapter(new OnBoardingPageAdapter(this));
    viewpager.addOnPageChangeListener(this);
    setListeners();
  }

  private void setListeners() {
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mCurrentIndex > 0) {
          viewpager.setCurrentItem(mCurrentIndex - 1);
        }
      }
    });
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mCurrentIndex < 2) {
          Log.d("OnBoarding", "Index " + mCurrentIndex);
          viewpager.setCurrentItem(mCurrentIndex + 1);
        } else {
          navigateToLoginPage();
        }
      }
    });
  }

  private void navigateToLoginPage() {
    HaprampPreferenceManager.getInstance().setOnBoardingVisited();
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    if (position > mCurrentIndex) {
      dotsView.moveToNext();
    } else {
      dotsView.moveBack();
    }
    mCurrentIndex = position;
    invalidateNavButton(mCurrentIndex);
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  private void invalidateNavButton(int mCurrentIndex) {
    if (mCurrentIndex == 2) {
      next.setText("START EARNING!");
    } else {
      next.setText("NEXT");
    }
    if (mCurrentIndex == 0) {
      back.setTextColor(getResources().getColor(R.color.Black38));
      back.setEnabled(false);
    } else {
      back.setTextColor(getResources().getColor(R.color.colorPrimary));
      back.setEnabled(true);
    }
  }
}
