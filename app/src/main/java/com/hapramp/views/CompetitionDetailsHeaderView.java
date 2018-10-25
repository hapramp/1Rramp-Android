package com.hapramp.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import com.hapramp.models.JudgeModel;
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
  @BindView(R.id.participation_hashtag_text)
  TextView participationHashtagText;
  @BindView(R.id.judge_label)
  TextView judgeLabel;
  @BindView(R.id.judge1_image)
  ImageView judge1Image;
  @BindView(R.id.judge1_name)
  TextView judge1Name;
  @BindView(R.id.judge1)
  RelativeLayout judge1;
  @BindView(R.id.judge2_image)
  ImageView judge2Image;
  @BindView(R.id.judge2_name)
  TextView judge2Name;
  @BindView(R.id.judge2)
  RelativeLayout judge2;
  @BindView(R.id.judge_container)
  LinearLayout judgeContainer;
  @BindView(R.id.judge_section)
  RelativeLayout judgeSection;
  @BindView(R.id.entries_info_section)
  TextView entriesInfoSection;
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
    setJudges(competition.getmJudges());
    setParticipationHashtagInfo("#oneramp-2343");
    setEntries(0);
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
      st.append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    } else {
      st.append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    }
    return st.toString();
  }

  private String getEndTime() {
    long now = System.currentTimeMillis();
    long ends = MomentsUtils.getMillisFromTime(mCompetition.getmEndsAt());
    StringBuilder st = new StringBuilder();
    if (ends > now) {
      st.append(MomentsUtils.getFormattedTime(mCompetition.getmEndsAt()));
    } else {
      st.append(MomentsUtils.getFormattedTime(mCompetition.getmEndsAt()));
    }
    return st.toString();
  }

  private void setJudges(List<JudgeModel> judges) {
    if (judges.size() == 0) {
      judgeSection.setVisibility(GONE);
    } else {
      judgeSection.setVisibility(VISIBLE);
      int len = judges.size() > 2 ? 2 : judges.size();
      judge1.setVisibility(GONE);
      judge2.setVisibility(GONE);
      for (int i = 0; i < len; i++) {
        if (i == 0) {
          judge1.setVisibility(VISIBLE);
          ImageHandler.loadCircularImage(mContext, judge1Image,
            String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
              judges.get(i).getmUsername()));
          judge1Name.setText(judges.get(i).getmUsername());
        } else if (i == 1) {
          judge2.setVisibility(VISIBLE);
          ImageHandler.loadCircularImage(mContext, judge2Image,
            String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
              judges.get(i).getmUsername()));
          judge2Name.setText(judges.get(i).getmUsername());
        }
      }
    }
  }

  private void setParticipationHashtagInfo(String hashtag) {
    String part1 = "Participate using ";
    String part3 = " from any other platform.";
    int hashtagLen = hashtag.length();
    int part1Len = part1.length();

    int spanStart = part1Len;
    int spanEnd = part1Len + hashtagLen;
    Spannable wordtoSpan = new SpannableString(part1 + hashtag + part3);
    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3F72AF")),
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    wordtoSpan.setSpan(bss,
      spanStart,
      spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    participationHashtagText.setText(wordtoSpan);
  }

  private void setEntries(int entries) {
    String en = String.valueOf(entries);
    int spanStart = 0;
    int spanEnd = en.length();
    Spannable wordtoSpan = new SpannableString(en + " ENTRIES");
    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3F72AF")),
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    entriesInfoSection.setText(wordtoSpan);
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
