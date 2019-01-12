package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xw.repo.BubbleSeekBar;

public class CustomViewPager extends ViewPager {
  public CustomViewPager(@NonNull Context context) {
    super(context);
  }

  public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
    Log.d("CheckingView", "View " + v);
    if (v instanceof SliderView) {
      return false;
    }
    if (v != this && v instanceof ViewPager) {
      return true;
    }
    return super.canScroll(v, checkV, dx, x, y);
  }

}
