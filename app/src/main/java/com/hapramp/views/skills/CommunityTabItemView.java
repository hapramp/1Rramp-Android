package com.hapramp.views.skills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.utils.CommunityUtils;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CommunityTabItemView extends FrameLayout {
  private final Context mContext;
  ImageView communityIcon;
  FrameLayout communityBackground;
  TextView communityTitle;
  private CommunityModel community;
  private int index;
  private TouchListener touchListener;

  public void setSelected(boolean isSelected) {
    setSelection(isSelected);
  }

  public CommunityTabItemView(@NonNull Context context) {
    super(context);
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.category_selector_item, this);
    communityIcon = view.findViewById(R.id.community_icon);
    communityTitle = view.findViewById(R.id.community_title);
    communityBackground = view.findViewById(R.id.community_background);
    view.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (touchListener != null) {
          touchListener.onTouched(index);
        }
      }
    });
  }

  private void setSelection(boolean selected) {
    if (selected) {
      communityBackground.setBackgroundResource(CommunityUtils.getFilledBackground(community.getmTag()));
    } else {
      communityBackground.setBackgroundResource(CommunityUtils.getBorder(community.getCommunityId()));
    }
  }

  public void setCommunity(CommunityModel model) {
    this.community = model;
    communityTitle.setText(model.getmName());
    communityIcon.setImageResource(CommunityUtils.getCommunityIcon(community.getmTag()));
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getCommunityTag() {
    return community.getmTag();
  }

  public void setTouchListener(TouchListener touchListener) {
    this.touchListener = touchListener;
  }

  public interface TouchListener {
    void onTouched(int index);
  }
}
