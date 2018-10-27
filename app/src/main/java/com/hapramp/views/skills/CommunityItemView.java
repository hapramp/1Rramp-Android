package com.hapramp.views.skills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.utils.CommunityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 6/22/2017.
 */

public class CommunityItemView extends FrameLayout {
  ImageView communityIcon;
  FrameLayout communityBackground;
  TextView communityTitle;
  private Context mContext;
  private CommunityModel mCommunity;

  public CommunityItemView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.community_selection_item_view, this);
    communityIcon = view.findViewById(R.id.community_icon);
    communityBackground = view.findViewById(R.id.community_background);
    communityTitle = view.findViewById(R.id.community_title);
  }

  public CommunityItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CommunityItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setSelection(boolean selected) {
    if (selected) {
      communityBackground.setBackgroundResource(CommunityUtils.getFilledBackground(mCommunity.getCommunityId()));
    } else {
      communityBackground.setBackgroundResource(CommunityUtils.getBorder(mCommunity.getCommunityId()));
    }
  }

  public int getCommunityId() {
    return mCommunity.getCommunityId();
  }

  public void setCommunityDetails(CommunityModel communityModel) {
    this.mCommunity = communityModel;
    setCommunityImage(CommunityUtils.getCommunityIcon(communityModel.getCommunityId()));
    setCommunityTitle(mCommunity.getmName());
  }

  private void setCommunityImage(int resId) {
    try {
      communityIcon.setImageResource(resId);
    }catch (Exception e){

    }
  }

  private void setCommunityTitle(String title) {
    communityTitle.setText(title);
  }
}
