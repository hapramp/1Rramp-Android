package com.hapramp.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.MicroCommunity;
import com.hapramp.ui.activity.MicroCommunityActivity;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MicroCommunityItemView extends FrameLayout {
  @BindView(R.id.mc_image)
  ImageView mcImage;
  @BindView(R.id.mc_image_container)
  FrameLayout mcImageContainer;
  @BindView(R.id.mc_title)
  TextView mcTitle;
  @BindView(R.id.main_container)
  RelativeLayout mainContainer;
  private Context mContext;

  public MicroCommunityItemView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.mc_item_view, this);
    ButterKnife.bind(this, view);
  }

  public MicroCommunityItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public MicroCommunityItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  public void setMCData(final MicroCommunity mcData) {
    if (mcData != null) {
      ImageHandler.loadCircularImage(mContext, mcImage, mcData.getImageUrl());
      mcTitle.setText(mcData.getTag());
      mainContainer.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          openCommunityDetailsPage(mcData);
        }
      });
    }
  }

  private void openCommunityDetailsPage(MicroCommunity microCommunity) {
    Intent intent = new Intent(mContext, MicroCommunityActivity.class);
    intent.putExtra(MicroCommunityActivity.EXTRA_COMMUNITY, microCommunity);
    mContext.startActivity(intent);
  }
}
