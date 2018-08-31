package com.hapramp.views.skills;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;

/**
 * Created by Ankit on 6/22/2017.
 */

public class CommunityItemView extends FrameLayout {

  ImageView communityIv;
  TextView skillSelectionOverlay;
  TextView communityItemTitle;
  private Context mContext;
  private CommunityModel mCommunity;

  public CommunityItemView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.community_selection_item_view, this);
    communityIv = view.findViewById(R.id.skills_bg_image);
    skillSelectionOverlay = view.findViewById(R.id.skill_selection_overlay);
    communityItemTitle = view.findViewById(R.id.skill_title);
    skillSelectionOverlay.setTypeface(new FontManager().getTypeFace(FontManager.FONT_MATERIAL));
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
      skillSelectionOverlay.setVisibility(VISIBLE);
    } else {
      skillSelectionOverlay.setVisibility(GONE);
    }
  }

  public String getCommunityTitle() {
    return mCommunity.getmName();
  }

  private void setCommunityTitle(String title) {
    communityItemTitle.setText(title);
  }

  public int getCommunityId() {
    return mCommunity.getmId();
  }

  public void setCommunityDetails(CommunityModel communityModel) {
    this.mCommunity = communityModel;
    // set Image
    setCommunityImage(mCommunity.getmImageUri());
    // set Overlay Color
    setCommunityOverlayColor(mCommunity.getmColor());
    // setTitle
    setCommunityTitle(mCommunity.getmName());
  }

  private void setCommunityImage(String imageUri) {
    ImageHandler.loadCircularImage(mContext, communityIv, imageUri);
  }

  private void setCommunityOverlayColor(String color) {
    setOverlayColor(color);
  }

  private void setOverlayColor(String color) {
    GradientDrawable background = (GradientDrawable) skillSelectionOverlay.getBackground();
    background.setColor(Color.parseColor(color));
  }


}
