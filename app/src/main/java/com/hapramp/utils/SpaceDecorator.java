package com.hapramp.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ankit on 1/14/2018.
 */

public class SpaceDecorator extends RecyclerView.ItemDecoration {

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
    if (isLastChild(view, parent) && parent.getAdapter().getItemCount() > 1) {
      outRect.bottom = PixelUtils.dpToPx(56);
    }

  }

  private boolean isLastChild(View v, RecyclerView parent) {


    return (parent.getAdapter().getItemCount() - 1) == parent.getChildAdapterPosition(v);

  }
}
