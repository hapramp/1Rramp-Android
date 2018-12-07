package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.views.skills.CommunityTabItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunityFilterView extends FrameLayout {
  @BindView(R.id.container)
  LinearLayout container;
  @BindView(R.id.micro_community_container_view)
  MicroCommunityContainerView microCommunityContainerView;
  private Context mContext;
  private CommunityFilterCallback communityFilterCallback;
  private int childs;

  public CommunityFilterView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.community_filter_view, this);
    ButterKnife.bind(this, view);
  }

  public CommunityFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void addCommunities(final ArrayList<CommunityModel> communityModels) {
    CommunityTabItemView itemView;
    childs = communityModels.size();
    for (int i = 0; i < childs; i++) {
      itemView = new CommunityTabItemView(mContext);
      itemView.setTouchListener(new CommunityTabItemView.TouchListener() {
        @Override
        public void onTouched(int index) {
          setSelection(index);
          microCommunityContainerView.setParentCommunityId(communityModels.get(index).getCommunityId());
        }
      });
      itemView.setCommunity(communityModels.get(i));
      itemView.setIndex(i);
      if (i == 0) {
        itemView.setSelected(true);
      } else {
        itemView.setSelected(false);
      }
      container.addView(itemView, i);
    }
  }

  private void setSelection(int index) {
    CommunityTabItemView cv;
    for (int i = 0; i < childs; i++) {
      cv = ((CommunityTabItemView) container.getChildAt(i));
      if (i == index) {
        if (cv != null) {
          cv.setSelected(true);
          if (communityFilterCallback != null) {
            communityFilterCallback.onCommunitySelected(cv.getCommunityTag());
          }
        }
      } else {
        if (cv != null) {
          cv.setSelected(false);
        }
      }
    }
  }

  public void onHiding(){
      microCommunityContainerView.setVisibility(GONE);
  }

  public void onShowing(){
    microCommunityContainerView.setVisibility(VISIBLE);
  }

  public void setCommunityFilterCallback(CommunityFilterCallback communityFilterCallback) {
    this.communityFilterCallback = communityFilterCallback;
  }

  public interface CommunityFilterCallback {
    void onCommunitySelected(String tag);
  }
}
