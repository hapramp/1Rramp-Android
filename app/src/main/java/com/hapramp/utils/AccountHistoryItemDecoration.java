package com.hapramp.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AccountHistoryItemDecoration extends RecyclerView.ItemDecoration{
		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
				int px = PixelUtils.dpToPx(4);
				outRect.bottom = px;
		}
}
