package com.hapramp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hapramp.R;
import com.hapramp.utils.PixelUtils;

public class CustomRatingBar extends android.support.v7.widget.AppCompatRatingBar {
  public static final int inActiveStar = R.drawable.star;
  public static final int activeStar = R.drawable.star_filled;
  public static final int STAR_DIMENSION_IN_DP = 28;
  private Paint mPaint;
  private Bitmap inActiveStarBitmap;
  private Bitmap activeStarBitamp;
  private int mStarDimensionInPx = 0;
  private int mSpaceBetweenStars = 0;

  public CustomRatingBar(Context context) {
    super(context);
    init();
  }

  private void init() {
    mPaint = new Paint();
    setStepSize(1f);
    mStarDimensionInPx = PixelUtils.dpToPx(STAR_DIMENSION_IN_DP);
    mSpaceBetweenStars = PixelUtils.dpToPx(14);
    inActiveStarBitmap = Bitmap.createScaledBitmap(getBitamp(inActiveStar),
      mStarDimensionInPx,
      mStarDimensionInPx,
      true);

    activeStarBitamp = Bitmap.createScaledBitmap(getBitamp(activeStar),
      mStarDimensionInPx,
      mStarDimensionInPx,
      true);

  }

  private Bitmap getBitamp(int resId) {
    return BitmapFactory.decodeResource(getResources(), resId);
  }

  public CustomRatingBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  @Override
  protected synchronized void onDraw(Canvas canvas) {
    int stars = getNumStars();
    float rating = getRating();
    int curX = 0;
    int curY = 0;
    for (int i = 0; i < stars; i++) {
      if ((i + 1) <= rating) {
        canvas.drawBitmap(activeStarBitamp, curX, curY, mPaint);
      } else {
        canvas.drawBitmap(inActiveStarBitmap, curX, curY, mPaint);
      }
      curX += mStarDimensionInPx + mSpaceBetweenStars;
      canvas.save();
    }
  }
}
