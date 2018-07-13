package com.hapramp.views.post;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
	* Created by Ankit on 12/25/2017.
	*/

public class WrapViewGroup extends ViewGroup {
		private int mAvailableWidth;

		public WrapViewGroup(Context context) {
				super(context);
				init(context);
		}

		public WrapViewGroup(Context context, AttributeSet attrs) {
				super(context, attrs);
				init(context);
		}

		public WrapViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
				super(context, attrs, defStyleAttr);
				init(context);
		}

		private void init(Context context) {
				final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				Point deviceDisplay = new Point();
				display.getSize(deviceDisplay);
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
				final int count = getChildCount();
				int curWidth, curHeight, curLeft, curTop, maxHeight;
				final int childLeft = this.getPaddingLeft();
				final int childTop = this.getPaddingTop();
				final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
				final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
				final int childWidth = childRight - childLeft;
				final int childHeight = childBottom - childTop;
				maxHeight = 0;
				curLeft = childLeft;
				curTop = childTop;
				for (int i = 0; i < count; i++) {
						View child = getChildAt(i);
						if (child.getVisibility() == GONE)
								return;
						child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
						curWidth = child.getMeasuredWidth();
						curHeight = child.getMeasuredHeight();
						if ((curLeft + curWidth) >= childRight) {
								curLeft = childLeft;
								curTop += maxHeight;
								maxHeight = 0;
						}
						child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
						if (maxHeight < curHeight) {
								maxHeight = curHeight;
						}
						curLeft += curWidth;
				}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
				int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
				int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
				int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
				mAvailableWidth = widthSpecSize - getPaddingLeft() - getPaddingRight();
				measureChildren(widthMeasureSpec, heightMeasureSpec);
				int childCount = getChildCount();
				int tmpWidth = 0;
				int measureHeight = 0;
				int currentChildHeight;

				for (int i = 0; i < childCount; i++) {
						View child = getChildAt(i);
						currentChildHeight = child.getMeasuredHeight();
						if (i == 0) {
								measureHeight = currentChildHeight;
						}
						tmpWidth += child.getMeasuredWidth() + child.getPaddingStart() + child.getPaddingEnd();
						if (tmpWidth > mAvailableWidth) {
								measureHeight += currentChildHeight + child.getPaddingTop() + child.getPaddingBottom();
								tmpWidth = child.getMeasuredWidth() + child.getPaddingStart() + child.getPaddingEnd();
						}
				}
				if (childCount == 0) {
						setMeasuredDimension(0, 0);
				} else if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
						setMeasuredDimension(widthSpecSize, measureHeight + getPaddingTop() + getPaddingBottom());
				} else {
						setMeasuredDimension(widthSpecSize, heightSpecSize);
				}
		}
}