package com.hapramp.views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.PixelUtils;

public class ManagedGridViewGroup extends ViewGroup {
  private int mCols = 5;
  private int verticalSpaceBetweenChild;
  private int horizontalSpaceBetweenChild;
  private int singleChildHeight;
  private int singleChildWidth;
  private int measureHeight;
  private int measureWidth;

  public ManagedGridViewGroup(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    Point deviceDisplay = new Point();
    display.getSize(deviceDisplay);
  }

  public ManagedGridViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ManagedGridViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setChildInfo(int cols, int childs) {
    this.mCols = cols;
    singleChildHeight = PixelUtils.dpToPx(84);
    singleChildWidth = PixelUtils.dpToPx(72);
    int width = HaprampPreferenceManager.getInstance().getDeviceWidth();
    int availableWidth = width - 2 * PixelUtils.dpToPx(16);
    availableWidth = availableWidth - singleChildWidth * cols;
    horizontalSpaceBetweenChild = availableWidth / 3;
    verticalSpaceBetweenChild = horizontalSpaceBetweenChild;
    int rows = getRows(childs);
    measureHeight = singleChildHeight * rows + ((rows - 1) * verticalSpaceBetweenChild);
    measureWidth = mCols * singleChildWidth + (mCols - 1) * horizontalSpaceBetweenChild;
  }

  public int getRows(int childs) {
    int cmpr = childs / mCols;
    int additionalRow = childs - (mCols * cmpr);
    cmpr += additionalRow > 0 ? 1 : 0;
    return cmpr;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();
    int curLeft, curTop, curRow;
    curLeft = 0;
    curTop = 0;
    curRow = 0;
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE)
        return;
      if (i % mCols == 0) {
        curLeft = 0;
        curTop = (curRow++) * (singleChildHeight + verticalSpaceBetweenChild);
      }
      child.layout(curLeft, curTop, curLeft + singleChildWidth, curTop + singleChildHeight);
      curLeft += singleChildWidth + horizontalSpaceBetweenChild;
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
    int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
    measureChildren(widthMeasureSpec, heightMeasureSpec);
    int childCount = getChildCount();
    if (childCount == 0) {
      setMeasuredDimension(0, 0);
    } else if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
      setMeasuredDimension(measureWidth, measureHeight + getPaddingTop() + getPaddingBottom());
    } else {
      setMeasuredDimension(widthSpecSize, heightSpecSize);
    }
  }
}
