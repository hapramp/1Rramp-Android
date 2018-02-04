package com.hapramp.utils;

import android.content.ReceiverCallNotAllowedException;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ankit on 2/3/2018.
 */

public class FeaturedImageItemDecorator extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int px = PixelUtils.dpToPx(8);
        outRect.bottom = px;
        outRect.left = px;
        outRect.top = px;

    }
}
