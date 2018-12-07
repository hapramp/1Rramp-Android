package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.MicroCommunity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MicroCommunityView extends FrameLayout {
  @BindView(R.id.viewWrapper)
  ManagedGridViewGroup parentView;
  @BindView(R.id.no_mc_msg)
  TextView noMcMsg;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  private Context mContext;
  private List<MicroCommunity> microCommunityList;

  public MicroCommunityView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_viewgroup, this);
    ButterKnife.bind(this, view);
  }

  public MicroCommunityView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public MicroCommunityView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  public void setMicroCommunityList(List<MicroCommunity> microCommunityList) {
    this.microCommunityList = microCommunityList;
    if (loadingProgressBar != null) {
      loadingProgressBar.setVisibility(GONE);
    }
    if (microCommunityList != null) {
      if (microCommunityList.size() > 0) {
        //add views
        addViews();
      } else {
        noMcMsg.setVisibility(VISIBLE);
        noMcMsg.setText("Not joined any community");
      }
    } else {
      noMcMsg.setVisibility(VISIBLE);
      noMcMsg.setText("Not joined any community");
    }
  }

  private void addViews() {
    parentView.removeAllViews();
    parentView.setChildInfo(4, microCommunityList.size());
    for (int i = 0; i < microCommunityList.size(); i++) {
      MicroCommunityItemView microCommunityItemView = new MicroCommunityItemView(mContext);
      microCommunityItemView.setMCData(microCommunityList.get(i));
      parentView.addView(microCommunityItemView, i,
        new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }
}
