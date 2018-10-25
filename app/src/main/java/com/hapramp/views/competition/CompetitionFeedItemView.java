package com.hapramp.views.competition;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CompetitionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.ui.activity.CompetitionDetailsActivity;
import com.hapramp.ui.activity.ParticipateEditorActivity;
import com.hapramp.utils.CommunityUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.CompetitionDetailsActivity.EXTRA_HEADER_PARCEL;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_HASHTAG;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_ID;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_TITLE;

public class CompetitionFeedItemView extends FrameLayout {
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
  @BindView(R.id.competition_meta_container)
  LinearLayout competitionMetaContainer;
  @BindView(R.id.post_snippet)
  TextView postSnippet;
  @BindView(R.id.actionButton)
  TextView actionButton;
  @BindView(R.id.totalPrize)
  TextView totalPrize;
  @BindView(R.id.startsIn)
  TextView startsIn;
  @BindView(R.id.participantsCount)
  TextView participantsCount;
  private Context mContext;
  private CompetitionModel mCompetition;
  private boolean mShowDeclareResultButton;

  public CompetitionFeedItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.competition_feed_item_view, this);
    ButterKnife.bind(this, view);
    attachListeners();
  }

  private void attachListeners() {
    featuredImagePost.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToCompetitionDetailsPage();
      }
    });
    postSnippet.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToCompetitionDetailsPage();
      }
    });
  }

  private void navigateToCompetitionDetailsPage() {
    Intent i = new Intent(mContext, CompetitionDetailsActivity.class);
    i.putExtra(EXTRA_HEADER_PARCEL, mCompetition);
    mContext.startActivity(i);
  }

  public CompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void bindCompetitionData(CompetitionModel competition) {
    this.mCompetition = competition;
    ImageHandler.loadCircularImage(mContext, feedOwnerPic,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        competition.getmAdmin().getmUsername()));
    feedOwnerTitle.setText(competition.getmAdmin().getmUsername());
    feedOwnerSubtitle.setText(String.format(" | %s", MomentsUtils.getFormattedTime(competition.getmCreatedAt())));
    setCommunities(competition.getCommunities());
    ImageHandler.load(mContext, featuredImagePost, competition.getmImage());
    competitionTitle.setText(competition.getmTitle());
    startsIn.setText(String.format(getStartedTime()));
    participantsCount.setText(String.format(Locale.US, "%d Participants", mCompetition.getmParticipantCount()));
    postSnippet.setText(competition.getmDescription());
    invalidateActionButton();
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

  private String getStartedTime() {
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

  private void invalidateActionButton() {
    String start_time = mCompetition.getmStartsAt();
    String end_time = mCompetition.getmEndsAt();
    long now = System.currentTimeMillis();
    long endsAt = MomentsUtils.getMillisFromTime(end_time);
    long startsAt = MomentsUtils.getMillisFromTime(start_time);
    if (!mCompetition.isWinners_announced()) {
      //not started
      if (now < startsAt) {
        setWhenNotStarted(start_time);
      }//running/live
      else if (now > startsAt && now < endsAt) {
        setWhenRunning(isAdminOfThisCompetition(), end_time);
      }
      //ended
      else if (now > endsAt) {
        setWhenEnded(isAdminOfThisCompetition(), end_time);
      }
    } else {
      setWhenWinnerAnnounced();
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

  /**
   * sets action button when competition is not started.
   */
  private void setWhenNotStarted(String startsIn) {
    startsIn = MomentsUtils.getFormattedTime(startsIn);
    if (actionButton != null) {
      actionButton.setEnabled(false);
      actionButton.setClickable(false);
      actionButton.setText("Starts " + startsIn);
    }
  }

  /**
   * @param isAdmin flag for admin.
   * @param endsAt  time when competition ends
   */
  private void setWhenRunning(boolean isAdmin, String endsAt) {
    endsAt = MomentsUtils.getFormattedTime(endsAt);
    if (isAdmin) {
      actionButton.setEnabled(false);
      actionButton.setClickable(false);
      actionButton.setText("Ends " + endsAt);
    } else {
      actionButton.setEnabled(true);
      actionButton.setClickable(true);
      actionButton.setText("Participate");
      actionButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          openSubmissionPage();
        }
      });
    }
  }

  private boolean isAdminOfThisCompetition() {
    return mCompetition
      .getmAdmin()
      .getmUsername()
      .equals(HaprampPreferenceManager
        .getInstance()
        .getCurrentSteemUsername());
  }

  /**
   * @param isAdmin flag for admin
   * @param endedAt time when competition ended
   */
  private void setWhenEnded(boolean isAdmin, String endedAt) {
    if (isAdmin) {
      actionButton.setEnabled(true);
      actionButton.setClickable(true);
      actionButton.setText("Declare Results");
      actionButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          openResultDeclarationPage();
        }
      });
    } else {
      endedAt = MomentsUtils.getFormattedTime(endedAt);
      actionButton.setEnabled(false);
      actionButton.setClickable(false);
      actionButton.setText("Closed " + endedAt);
    }
  }

  /**
   * set state when winners are decided
   */
  private void setWhenWinnerAnnounced() {
    actionButton.setEnabled(true);
    actionButton.setClickable(true);
    actionButton.setText("View Winners");
    actionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openWinnersList();
      }
    });
  }

  private void resetVisibility() {
    club1.setVisibility(GONE);
    club2.setVisibility(GONE);
    club3.setVisibility(GONE);
  }

  private void openSubmissionPage() {
    Intent intent = new Intent(mContext, ParticipateEditorActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, mCompetition.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, mCompetition.getmTitle());
    intent.putExtra(EXTRA_COMPETITION_HASHTAG, "oneramp-2434");
    mContext.startActivity(intent);
  }

  // TODO: 25/10/18 add implementation
  private void openResultDeclarationPage() {

  }

  //todo: add implementation
  private void openWinnersList() {

  }
}
