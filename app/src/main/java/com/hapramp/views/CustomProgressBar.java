package com.hapramp.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.hapramp.R;
import com.hapramp.utils.PixelUtils;

public class CustomProgressBar extends View {
  private String primaryColor;
  private String secondaryColor;
  private Paint primaryPaint;
  private Paint secondaryPaint;
  private Paint textPaint;
  private int percent;
  private Rect textRect;

  public CustomProgressBar(Context context) {
    super(context);
  }

  public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0);
    try {
      primaryColor = typedArray.getString(R.styleable.CustomProgressBar_primaryColor);
      secondaryColor = typedArray.getString(R.styleable.CustomProgressBar_secondaryColor);
    }
    finally {
      typedArray.recycle();
    }
    init();
  }

  private void init() {
    primaryPaint = new Paint();
    primaryPaint.setAntiAlias(false);
    primaryPaint.setStrokeCap(Paint.Cap.ROUND);
    primaryPaint.setStrokeWidth(64);
    primaryPaint.setColor(Color.parseColor(primaryColor));

    secondaryPaint = new Paint();
    secondaryPaint.setAntiAlias(false);
    secondaryPaint.setStrokeCap(Paint.Cap.ROUND);
    secondaryPaint.setStrokeWidth(64);
    secondaryPaint.setColor(Color.parseColor(secondaryColor));

    textPaint = new Paint();
    textPaint.setColor(Color.WHITE);
    textPaint.setTextSize(36);
    textPaint.setTypeface(Typeface.DEFAULT_BOLD);
    textPaint.setStyle(Paint.Style.FILL);
    textRect = new Rect();
  }

  public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int h = getMeasuredHeight();
    int w = getMeasuredWidth();
    int progress = (w * percent) / 100;
    canvas.drawRoundRect(new RectF(0, 0, progress, h), h, h, primaryPaint);
    canvas.drawRoundRect(new RectF(0, 0, w, h), h, h, secondaryPaint);
    String text = percent + " % ";
    textPaint.getTextBounds(text, 0, text.length(), textRect);
    int textStartY = (h - textRect.height()/2);
    int textStartX = progress - textRect.width() - 24;
    canvas.drawText(text, textStartX, textStartY, textPaint);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int desiredWidth = PixelUtils.dpToPx(PixelUtils.getWidth());
    int desiredHeight = PixelUtils.dpToPx(24);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int width;
    int height;
    //Measure Width
    if (widthMode == MeasureSpec.EXACTLY) {
      width = widthSize;
    } else if (widthMode == MeasureSpec.AT_MOST) {
      width = Math.min(desiredWidth, widthSize);
    } else {
      width = desiredWidth;
    }
    if (heightMode == MeasureSpec.EXACTLY) {
      height = heightSize;
    } else if (heightMode == MeasureSpec.AT_MOST) {
      height = Math.min(desiredHeight, heightSize);
    } else {
      height = desiredHeight;
    }
    setMeasuredDimension(width, height);
  }

  public void setProgressPercent(int percent) {
    this.percent = percent;
    requestLayout();
  }
}
