package com.hapramp.views.competition;

import android.content.Context;
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
import com.hapramp.steem.models.Voter;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.views.CommunityStripView;
import com.hapramp.views.VoterPeekView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RankableCompetitionFeedItemView extends FrameLayout {
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.popupMenuDots)
  ImageView popupMenuDots;
  @BindView(R.id.featured_image_post)
  ImageView featuredImagePost;
  @BindView(R.id.post_title)
  TextView postTitle;
  @BindView(R.id.post_snippet)
  TextView postSnippet;
  @BindView(R.id.rating_desc)
  TextView ratingDesc;
  @BindView(R.id.voters_peek_view)
  VoterPeekView votersPeekView;
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
  @BindView(R.id.item_rank)
  TextView itemRank;
  @BindView(R.id.assignRankBtn)
  LinearLayout assignRankBtn;
  @BindView(R.id.ranking_image)
  ImageView rankingImage;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  private Context mContext;
  private RankableCompetitionItemListener rankableCompetitionItemListener;
  private RankableCompetitionFeedItem mData;
  private String briefPayoutValueString;

  public RankableCompetitionFeedItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.competition_feed_item_rankable, this);
    ButterKnife.bind(this, view);
    attachListeners();
  }

  private void attachListeners() {
    assignRankBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (rankableCompetitionItemListener != null) {
          if (mData.getRank() == 0) {
            rankableCompetitionItemListener.onAssignRankClicked(mData);
          }
        }
      }
    });
  }

  public RankableCompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public RankableCompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    if (data.getRank() == 0) {
      rankingImage.setImageResource(R.drawable.ranking);
      itemRank.setText("Assign Rank");
      itemRank.setTextColor(Color.parseColor("#8a000000"));
    } else {
      rankingImage.setImageResource(R.drawable.ranking_filled);
      itemRank.setText("Ranked: " + data.getRank());
      itemRank.setTextColor(Color.parseColor("#3F72AF"));
    }
    updateVotersPeekView(data.getVoters());
  }

  private void setSteemEarnings(RankableCompetitionFeedItem feed) {
    try {
      double payout = feed.getPayout();
      briefPayoutValueString = String.format(Locale.US, "%1$.3f", payout);
      payoutValue.setText(briefPayoutValueString);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void setCommunities(List<String> communities) {
    communityStripeView.setCommunities(communities);
  }

  private void updateVotersPeekView(List<Voter> voters) {
    if (votersPeekView != null) {
      votersPeekView.setVoters(voters);
    }
  }

  public void setRankableCompetitionItemListener(RankableCompetitionItemListener rankableCompetitionItemListener) {
    this.rankableCompetitionItemListener = rankableCompetitionItemListener;
  }

  public interface RankableCompetitionItemListener {
    void onAssignRankClicked(RankableCompetitionFeedItem item);
  }
}
