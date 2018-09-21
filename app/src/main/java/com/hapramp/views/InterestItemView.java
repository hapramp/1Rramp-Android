package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.utils.CommunityUtils;

public class InterestItemView extends FrameLayout {
  ImageView interestIcon;
  FrameLayout interestBackground;
  TextView interestTitle;
  private Context mContext;
  private CommunityModel mCommunity;

  public InterestItemView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.interest_item_view, this);
    interestIcon = view.findViewById(R.id.interest_icon);
    interestBackground = view.findViewById(R.id.interest_background);
    interestTitle = view.findViewById(R.id.interest_title);
  }

  public InterestItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public InterestItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setSelection(boolean selected) {
    if (selected) {
      interestBackground.setBackgroundResource(CommunityUtils.getFilledBackground(mCommunity.getCommunityId()));
    } else {
      interestBackground.setBackgroundResource(CommunityUtils.getBorder(mCommunity.getCommunityId()));
    }
  }

  public int getCommunityId() {
    return mCommunity.getCommunityId();
  }

  public void setInterestDetails(CommunityModel communityModel) {
    this.mCommunity = communityModel;
    setCommunityImage(CommunityUtils.getCommunityIcon(communityModel.getCommunityId()));
    setInterestTitle(mCommunity.getmName());
  }

  private void setCommunityImage(int resId) {
    interestIcon.setImageResource(resId);
  }

  private void setInterestTitle(String title) {
    interestTitle.setText(title);
  }
}
