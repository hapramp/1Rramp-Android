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
    private boolean wantTopOffset = true;
    private int topOffset;

    public ViewItemDecoration(Drawable drawable) {
        this.mDivider = drawable;
    }

    public void setWantTopOffset(boolean want , int topOffset){
        this.topOffset = topOffset;
        this.wantTopOffset = want;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == 0 && wantTopOffset) {

            outRect.top = PixelUtils.dpToPx(104);
            outRect.bottom = mDivider.getIntrinsicHeight();

        }else{
            outRect.bottom = mDivider.getIntrinsicHeight();
        }


        if(isLastChild(view,parent) && parent.getAdapter().getItemCount()>1){
            outRect.bottom = PixelUtils.dpToPx(56);
        }

       // outRect.top = mDivider.getIntrinsicHeight();

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

    public void setTopOffset(int topOffset) {

    }
}
