package com.hapramp.views.extraa;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class BubbleProgressBar extends View {

		public static final int BUBBLE_COUNT = 5;
		public static final int BUBBLE_MAX_RADIUS_IN_DP = 4;
		public static final int BUBBLE_MIN_RADIUS_IN_DP = 2;
		public static final int BUBBLE_GAP_IN_DP = 4;
		public static final int ANIMATION_TIME = 800; // in milliseconds
		public static final double[] radiuses = {0, 0, 0, 0, 0};
		private static double mPhaseDifference = 0;
		private static final String color = "#555555";
		private Resources resources;
		private float mBubbleMaxRadiusInPx;
		private float mBubbleMinRadiusInPx;
		private float mBubbleGapInPx;
		private Paint mPaint;
		private int calculatedViewWidth;
		private int calculatedViewHeight;

		public BubbleProgressBar(Context context) {
				super(context);
				init(context);
		}

		public BubbleProgressBar(Context context, @Nullable AttributeSet attrs) {
				super(context, attrs);
				init(context);
		}

		public BubbleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
				super(context, attrs, defStyleAttr);
				init(context);
		}

		private void init(Context context) {
				resources = context.getResources();
				mBubbleMaxRadiusInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BUBBLE_MAX_RADIUS_IN_DP, resources.getDisplayMetrics());
				mBubbleMinRadiusInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BUBBLE_MIN_RADIUS_IN_DP, resources.getDisplayMetrics());
				mBubbleGapInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BUBBLE_GAP_IN_DP, resources.getDisplayMetrics());
				mPaint = new Paint();
				mPaint.setStyle(Paint.Style.FILL);
				mPaint.setAntiAlias(true);
				mPaint.setColor(Color.parseColor(color));
				mPhaseDifference = 90 / (BUBBLE_COUNT - 1);
				calculatedViewWidth = (int) (2 * BUBBLE_COUNT * mBubbleMaxRadiusInPx + (BUBBLE_COUNT - 1) * mBubbleGapInPx);
				calculatedViewHeight = (int) (2 * mBubbleMaxRadiusInPx);
				tryAnimation();
		}

		@Override
		protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				float originX = mBubbleMaxRadiusInPx;
				float originY = mBubbleMaxRadiusInPx;

				for (int i = 0; i < BUBBLE_COUNT; i++) {
						//draw circle
						canvas.drawCircle(originX, originY, (float) radiuses[i], mPaint);
						//prepare next origin info
						originX += 2 * mBubbleMaxRadiusInPx + mBubbleGapInPx;
				}
		}

		private void tryAnimation() {
				final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360);
				valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator valueAnimator) {
								for (int i = 0; i < BUBBLE_COUNT; i++) {
										radiuses[i] = mBubbleMaxRadiusInPx * getFactor((Integer) valueAnimator.getAnimatedValue(), i);
								}
								invalidate();
						}
				});
				valueAnimator.setRepeatMode(ValueAnimator.RESTART);
				valueAnimator.setRepeatCount(10000);
				valueAnimator.setDuration(2000);
				valueAnimator.start();
		}

		private double getFactor(int animatedAngle, int circleIndex) {
				return Math.abs(Math.cos(getRadian((int) (animatedAngle + (circleIndex * mPhaseDifference)))));
		}

		private double getRadian(int degrees) {
				return (degrees * Math.PI) / 180;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				int width = getPaddingStart() + getPaddingEnd() + calculatedViewWidth;
				int height = getPaddingTop() + getPaddingBottom() + calculatedViewHeight;
				int w = resolveSizeAndState(width, widthMeasureSpec, 0);
				int h = resolveSizeAndState(height, heightMeasureSpec, 0);
				setMeasuredDimension(w, h);
		}
}