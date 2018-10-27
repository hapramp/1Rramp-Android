package com.hapramp.views;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

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

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hapramp.views.JudgeSelectionView.MAX_JUDGES_ALLOWED;

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
  @BindView(R.id.judge_container)
  LinearLayout judgeContainer;
  @BindView(R.id.judge_section)
  RelativeLayout judgeSection;
  @BindView(R.id.entries_info_section)
  TextView entriesInfoSection;
  @BindView(R.id.entries_loading_container)
  RelativeLayout entriesLoadingContainer;
  @BindView(R.id.copy_hashtag_button)
  ImageView copyHashtagButton;
  private Context mContext;
  private CompetitionModel mCompetition;
  private boolean mEntriesLoaded;

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
    copyHashtagButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("hashtag", mCompetition.getmParticipationHashtag());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext, "Competition hashtag copied", Toast.LENGTH_LONG).show();
      }
    });
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
    setParticipationHashtagInfo(mCompetition.getmParticipationHashtag());
    setEntries(mCompetition.getmPostCount());
    invalidateEntriesLoadingIndicator();
    addPrizes();
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

  public void setJudges(final List<JudgeModel> judges) {
    try {
      int len = judges.size() > MAX_JUDGES_ALLOWED ? MAX_JUDGES_ALLOWED : judges.size();
      judgeContainer.removeAllViews();
      //add items
      for (int i = 0; i < len; i++) {
        final JudgeModel judgeModel = judges.get(i);
        JudgeItemView itemView = new JudgeItemView(mContext);
        itemView.setJudgeInfo(judgeModel);
        itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            showJudgeDetails(judgeModel);
          }
        });
        //add to view
        judgeContainer.addView(itemView, i);
      }
    }
    catch (Exception e) {
      Toast.makeText(mContext, "Error while selecting judges!", Toast.LENGTH_LONG).show();
    }
  }

  private void setParticipationHashtagInfo(String hashtag) {
    hashtag = "#" + hashtag;
    String part1 = "Participate using ";
    String part3 = " from any other steem platform.";
    int hashtagLen = hashtag.length();
    int part1Len = part1.length();

    int spanStart = part1Len;
    int spanEnd = part1Len + hashtagLen;
    Spannable wordtoSpan = new SpannableString(part1 + hashtag + part3);
    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3F72AF")),
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    StyleSpan bss = new StyleSpan(Typeface.BOLD);
    wordtoSpan.setSpan(bss,
      spanStart,
      spanEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    participationHashtagText.setText(wordtoSpan);
  }

  private void setEntries(int entries) {
    String en = String.valueOf(entries);
    int spanStart = 0;
    int spanEnd = en.length();
    Spannable wordtoSpan = new SpannableString(en + ((entries > 1) ? " ENTRIES" : " ENTRY"));
    wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3F72AF")),
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    entriesInfoSection.setText(wordtoSpan);
  }

  private void invalidateEntriesLoadingIndicator() {
    if (mCompetition.getmPostCount() > 0) {
      if (mEntriesLoaded) {
        entriesLoadingContainer.setVisibility(GONE);
      } else {
        entriesLoadingContainer.setVisibility(VISIBLE);
      }
    } else {
      entriesLoadingContainer.setVisibility(GONE);
    }
  }

  private void addPrizes() {
    int len = mCompetition.getPrizes().size();
    String header = "";
    for (int i = 0; i < len; i++) {
      PrizeRowItemView prizeRowItemView = new PrizeRowItemView(mContext);
      header = getPrizeHeader(i);
      prizeRowItemView.setPrizeData(header, mCompetition.getPrizes().get(i));
      prizeDescriptionContainer.addView(prizeRowItemView, i);
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

  private void showJudgeDetails(JudgeModel judgeModel) {
    try {
      JudgeProfileDialog judgeProfileDialog = new JudgeProfileDialog((AppCompatActivity) mContext);
      judgeProfileDialog.setJudgeInfo(judgeModel);
      judgeProfileDialog.show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getPrizeHeader(int position) {
    switch (position) {
      case 0:
        return "1st Prize: ";
      case 1:
        return "2nd Prize: ";
      case 2:
        return "3rd Prize: ";
      default:
        return (position + 1) + "th Prize: ";
    }
  }

  private void resetVisibility() {
    club1.setVisibility(GONE);
    club2.setVisibility(GONE);
    club3.setVisibility(GONE);
  }

  public void setEntriesLoaded(boolean loaded) {
    this.mEntriesLoaded = loaded;
    invalidateEntriesLoadingIndicator();
  }
}
