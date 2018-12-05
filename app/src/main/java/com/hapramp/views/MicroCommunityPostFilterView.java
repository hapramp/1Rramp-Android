package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.ui.fragments.CommunityPostFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MicroCommunityPostFilterView extends FrameLayout {
  @BindView(R.id.trendingItem)
  TextView trendingItem;
  @BindView(R.id.newItem)
  TextView newItem;
  @BindView(R.id.hotItem)
  TextView hotItem;
  private Context mContext;
  private TextView lastSelection;
  private FilterItemSelectionCallback filterItemSelectionCallback;

  public MicroCommunityPostFilterView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_post_filter_tab, this);
    ButterKnife.bind(this, view);
    //default selection: Trending
    lastSelection = trendingItem;
    trendingItem.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (lastSelection == trendingItem)
          return;
        select(trendingItem);
        deSelect(lastSelection);
        lastSelection = trendingItem;
        sendSelection(CommunityPostFragment.ORDER_TRENDING);
      }
    });
    newItem.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (lastSelection == newItem)
          return;
        select(newItem);
        deSelect(lastSelection);
        lastSelection = newItem;
        sendSelection(CommunityPostFragment.ORDER_NEW);
      }
    });

    hotItem.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (lastSelection == hotItem)
          return;
        select(hotItem);
        deSelect(lastSelection);
        lastSelection = hotItem;
        sendSelection(CommunityPostFragment.ORDER_HOT);
      }
    });
  }

  private void select(TextView tv) {
    tv.setBackgroundResource(R.drawable.light_black_button_bg);
    //text color to primary color
    tv.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
  }

  private void deSelect(TextView tv) {
    tv.setBackgroundResource(0);
    tv.setTextColor(mContext.getResources().getColor(R.color.Black54));
  }

  private void sendSelection(String item) {
    if (filterItemSelectionCallback != null) {
      filterItemSelectionCallback.onItemSelected(item);
    }
  }

  public MicroCommunityPostFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public MicroCommunityPostFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  public void setFilterItemSelectionCallback(FilterItemSelectionCallback filterItemSelectionCallback) {
    this.filterItemSelectionCallback = filterItemSelectionCallback;
  }

  public interface FilterItemSelectionCallback {
    void onItemSelected(String order);
  }
}
