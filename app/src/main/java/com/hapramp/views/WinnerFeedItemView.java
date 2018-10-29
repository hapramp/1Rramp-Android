package com.hapramp.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerFeedItemView extends FrameLayout {
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.featured_image_post)
  ImageView featuredImagePost;
  @BindView(R.id.post_title)
  TextView postTitle;
  @BindView(R.id.post_snippet)
  TextView postSnippet;
  @BindView(R.id.rating_desc)
  TextView ratingDesc;
  @BindView(R.id.ranking_image)
  ImageView rankingImage;
  @BindView(R.id.assignRankBtn)
  LinearLayout assignRankBtn;
  @BindView(R.id.rate_info_container)
  RelativeLayout rateInfoContainer;
  @BindView(R.id.divider)
  View divider;
  @BindView(R.id.comment_icon)
  ImageView commentIcon;
  @BindView(R.id.comment_count)
  TextView commentCount;
  @BindView(R.id.dollar_icon)
  ImageView dollarIcon;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.rank_tip)
  TextView rankTip;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  @BindView(R.id.prize_won)
  TextView prizeWon;
  private Context mContext;
  private RankableCompetitionFeedItem mData;
  private String briefPayoutValueString;

  public WinnerFeedItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.winner_feed_item_layout, this);
    ButterKnife.bind(this, view);
    attachListeners();
  }

  private void attachListeners() {
    featuredImagePost.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openPostDetailsPage();
      }
    });

    postSnippet.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openPostDetailsPage();
      }
    });
  }

  private void openPostDetailsPage() {
    Intent intent = new Intent(mContext, DetailedActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_POST_AUTHOR, mData.getUsername());
    intent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK, mData.getPermlink());
    mContext.startActivity(intent);
  }

  public WinnerFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public WinnerFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void bindData(RankableCompetitionFeedItem data) {
    this.mData = data;
    ImageHandler.loadCircularImage(mContext, feedOwnerPic,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        data.getUsername()));
    feedOwnerTitle.setText(data.getUsername());
    feedOwnerSubtitle.setText(String.format(mContext.getResources().getString(R.string.post_subtitle_format),
      MomentsUtils.getFormattedTime(data.getCreatedAt())));
    setSteemEarnings(data);
    setCommunities(data.getTags());
    if (data.getFeaturedImageLink().length() > 0) {
      featuredImagePost.setVisibility(VISIBLE);
      ImageHandler.load(mContext, featuredImagePost, data.getFeaturedImageLink());
    } else {
      featuredImagePost.setVisibility(GONE);
    }
    String bdy = data.getDescription();
    if (bdy.length() > 0) {
      postSnippet.setVisibility(VISIBLE);
      postSnippet.setText(bdy);
    } else {
      postSnippet.setVisibility(GONE);
    }
    postTitle.setText(data.getTitle());
    commentCount.setText(data.getChildrens() + "");
  }

  private void setSteemEarnings(RankableCompetitionFeedItem feed) {
    try {
      double pendingPayoutValue = Double.parseDouble(feed.getPayout().split(" ")[0]);
      if (pendingPayoutValue > 0) {
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", pendingPayoutValue);
      } else {
        //cashed out
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", pendingPayoutValue);
      }
      payoutValue.setText(briefPayoutValueString);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void setCommunities(List<String> communities) {
    communityStripeView.setCommunities(communities);
  }
}
