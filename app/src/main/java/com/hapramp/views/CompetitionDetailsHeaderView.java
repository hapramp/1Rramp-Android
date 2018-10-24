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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CompetitionModel;
import com.hapramp.steem.Communities;
import com.hapramp.utils.CommunityUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompetitionDetailsHeaderView extends FrameLayout {
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.club3)
  TextView club3;
  @BindView(R.id.club2)
  TextView club2;
  @BindView(R.id.club1)
  TextView club1;
  @BindView(R.id.popupMenuDots)
  ImageView popupMenuDots;
  @BindView(R.id.featured_image_post)
  ImageView featuredImagePost;
  @BindView(R.id.competition_title)
  TextView competitionTitle;
  @BindView(R.id.prize_icon)
  ImageView prizeIcon;
  @BindView(R.id.prize_description_container)
  LinearLayout prizeDescriptionContainer;
  @BindView(R.id.time_icon)
  ImageView timeIcon;
  @BindView(R.id.time_description_container)
  LinearLayout timeDescriptionContainer;
  @BindView(R.id.competition_meta_container)
  RelativeLayout competitionMetaContainer;
  @BindView(R.id.description_caption)
  TextView descriptionCaption;
  @BindView(R.id.post_snippet)
  TextView postSnippet;
  @BindView(R.id.rules_caption)
  TextView rulesCaption;
  @BindView(R.id.competition_rules)
  TextView competitionRules;
  @BindView(R.id.startsTime)
  TextView startsTime;
  @BindView(R.id.endTime)
  TextView endTime;
  private Context mContext;
  private CompetitionModel mCompetition;

  public CompetitionDetailsHeaderView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.competition_detail_header_view, this);
    ButterKnife.bind(this, view);
    attachListeners();
  }

  private void attachListeners() {

  }

  public CompetitionDetailsHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CompetitionDetailsHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void bindCompetitionHeaderData(CompetitionModel competition) {
    this.mCompetition = competition;
    ImageHandler.loadCircularImage(mContext, feedOwnerPic,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        competition.getmAdmin().getmUsername()));
    feedOwnerTitle.setText(competition.getmAdmin().getmUsername());
    feedOwnerSubtitle.setText(String.format(" | %s", MomentsUtils.getFormattedTime(competition.getmCreatedAt())));
    setCommunities(competition.getCommunities());
    ImageHandler.load(mContext, featuredImagePost, competition.getmImage());
    competitionTitle.setText(competition.getmTitle());
    startsTime.setText(getStartTime());
    endTime.setText(getEndTime());
    postSnippet.setText(competition.getmDescription());
    competitionRules.setText(competition.getmRules());
  }

  private void setCommunities(List<CommunityModel> communities) {
    List<CommunityModel> cm = new ArrayList<>();
    ArrayList<String> addedCommunity = new ArrayList<>();
    for (int i = 0; i < communities.size(); i++) {
      String title = CommunityUtils.getCommunityTitleFromName(communities.get(i).getmName());
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

  private String getStartTime() {
    long now = System.currentTimeMillis();
    long startsAt = MomentsUtils.getMillisFromTime(mCompetition.getmStartsAt());
    StringBuilder st = new StringBuilder();
    if (startsAt > now) {
      st.append("Starts ").append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    } else {
      st.append("Started ").append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    }
    return st.toString();
  }

  private String getEndTime() {
    long now = System.currentTimeMillis();
    long ends = MomentsUtils.getMillisFromTime(mCompetition.getmEndsAt());
    StringBuilder st = new StringBuilder();
    if (ends > now) {
      st.append("Ends ").append(MomentsUtils.getFormattedTime(mCompetition.getmEndsAt()));
    } else {
      st.append("Ended ").append(MomentsUtils.getFormattedTime(mCompetition.getmEndsAt()));
    }
    return st.toString();
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
