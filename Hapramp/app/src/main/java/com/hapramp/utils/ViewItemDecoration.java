package com.hapramp.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ankit on 12/13/2017.
 */

public class ViewItemDecoration extends RecyclerView.ItemDecoration {


    private Drawable mDivider;
    public ViewItemDecoration(Drawable drawable) {
        this.mDivider = drawable;
    }

//    public void setWantBottom(boolean wantBottom) {
//        this.wantBottom = wantBottom;
//    }
//
//    public void setWantLeft(boolean wantLeft) {
//        this.wantLeft = wantLeft;
//    }
//
//    public void setWantRight(boolean wantRight) {
//        this.wantRight = wantRight;
//    }
//
//    public void setMoreBottomSpace(int bottomSpace, boolean moreBottomSpace) {
//
//        this.moreBottomSpace = moreBottomSpace;
//        this.bottomSpace = bottomSpace;
//
//    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//        // we can assign space according to requirement
//
//        if (wantLeft)
//            outRect.left = space;
//        if (wantRight)
//            outRect.right = space;
//        if (wantBottom)
//            outRect.bottom = space;
//
//        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = space;
//        }
//
//        if (moreBottomSpace && isLastChild(view, parent)) {
//            outRect.bottom = bottomSpace;
//        } else {
//            outRect.bottom = space;
//        }

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }

        outRect.top = mDivider.getIntrinsicHeight();

    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }

    private boolean isLastChild(View v, RecyclerView parent) {

        return (parent.getAdapter().getItemCount() - 1) == parent.getChildAdapterPosition(v);

    }

}
