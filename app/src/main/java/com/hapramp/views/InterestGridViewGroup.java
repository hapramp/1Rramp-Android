package com.hapramp.views;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hapramp.utils.PixelUtils;

public class InterestGridViewGroup  extends ViewGroup {
  private int mCols = 5;
  private int verticalSpaceBetweenChild;
  private int horizontalSpaceBetweenChild;
  private int singleChildHeight;
  private int singleChildWidth;
  private int measureHeight;
  private int measureWidth;

  public InterestGridViewGroup(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    Point deviceDisplay = new Point();
    display.getSize(deviceDisplay);
  }

  public InterestGridViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public InterestGridViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setChildInfo(int cols, int childs) {
    this.mCols = cols;
    verticalSpaceBetweenChild = PixelUtils.dpToPx(12);
    horizontalSpaceBetweenChild = PixelUtils.dpToPx(8);
    singleChildHeight = PixelUtils.dpToPx(64);
    singleChildWidth = PixelUtils.dpToPx(64);
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
