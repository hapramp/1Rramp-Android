package com.hapramp.views.skills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.utils.ImageHandler;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CommunityTabItemView extends FrameLayout {

  private final Context mContext;
  ImageView skillsBgImage;
  TextView skillTitle;
  FrameLayout selectorOverlay;
  FrameLayout selectionTabIndicator;
  private CommunityModel community;
  private int index;
  private TouchListener touchListener;

  public void setSelected(boolean isSelected) {
    setSelection(isSelected);
  }

  private void setSelection(boolean selected) {
    if (selected) {
      selectionTabIndicator.setVisibility(VISIBLE);
      selectorOverlay.setVisibility(GONE);
    } else {
      selectionTabIndicator.setVisibility(GONE);
      selectorOverlay.setVisibility(VISIBLE);
    }
  }

  public CommunityTabItemView(@NonNull Context context) {
    super(context);
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.category_selector_item, this);
    skillsBgImage = view.findViewById(R.id.skills_bg_image);
    skillTitle = view.findViewById(R.id.skill_title);
    selectorOverlay = view.findViewById(R.id.selector_overlay);
    selectionTabIndicator = view.findViewById(R.id.selection_tab_indicator);
    view.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (touchListener != null) {
          touchListener.onTouched(index);
        }
      }
    });
  }

  public void setCommunity(CommunityModel model) {
    this.community = model;
    skillTitle.setText(model.getmName());
    ImageHandler.loadCircularImage(mContext, skillsBgImage, community.getmImageUri());
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
