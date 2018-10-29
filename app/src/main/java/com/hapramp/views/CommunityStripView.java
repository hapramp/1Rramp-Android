package com.hapramp.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.steem.Communities;
import com.hapramp.utils.CommunityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunityStripView extends FrameLayout {
  @BindView(R.id.club3)
  TextView club3;
  @BindView(R.id.club2)
  TextView club2;
  @BindView(R.id.club1)
  TextView club1;

  public CommunityStripView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public CommunityStripView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CommunityStripView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.community_strip_view, this);
    ButterKnife.bind(this, view);
  }

  public void setCommunities(List<String> communities) {
    try {
      List<CommunityModel> cm = new ArrayList<>();
      ArrayList<String> addedCommunity = new ArrayList<>();
      for (int i = 0; i < communities.size(); i++) {
        String title = CommunityUtils.getCommunityTitleFromTag(communities.get(i));
        if (Communities.doesCommunityExists(title) && !addedCommunity.contains(title)) {
          cm.add(new CommunityModel(
            CommunityUtils.getCommunityColorFromTitle(title), //color
            title //title ex. art
          ));
          addedCommunity.add(title);
        }
      }
      addCommunitiesToLayout(cm);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void addCommunitiesToLayout(List<CommunityModel> cms) {
    int size = cms.size();
    resetVisibility();
    if (size > 0) {
      club1.setVisibility(VISIBLE);
      club1.setText(cms.get(0).getmName().toUpperCase());
      club1.getBackground().setColorFilter(
        Color.parseColor(cms.get(0).getmColor()),
        PorterDuff.Mode.SRC_ATOP);
      if (size > 1) {
        club2.setVisibility(VISIBLE);
        club2.setText(cms.get(1).getmName().toUpperCase());
        club2.getBackground().setColorFilter(
          Color.parseColor(cms.get(1).getmColor()),
          PorterDuff.Mode.SRC_ATOP);
        if (size > 2) {
          club3.setVisibility(VISIBLE);
          club3.setText(cms.get(2).getmName().toUpperCase());
          club3.getBackground().setColorFilter(
            Color.parseColor(cms.get(2).getmColor()),
            PorterDuff.Mode.SRC_ATOP);
        }
      }
    }
  }

  private void resetVisibility() {
    club1.setVisibility(GONE);
    club2.setVisibility(GONE);
    club3.setVisibility(GONE);
  }

}
