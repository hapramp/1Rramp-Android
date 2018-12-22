package com.hapramp.views.competition;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CompetitionModel;
import com.hapramp.models.DeleteCompetitionResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.CompetitionDetailsActivity;
import com.hapramp.ui.activity.ParticipateEditorActivity;
import com.hapramp.ui.activity.WinnerDeclarationActivity;
import com.hapramp.ui.activity.WinnersFeedListActivity;
import com.hapramp.utils.CountDownTimerUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PrizeMoneyFilter;
import com.hapramp.views.CommunityStripView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hapramp.ui.activity.CompetitionDetailsActivity.EXTRA_HEADER_PARCEL;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_HASHTAG;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_ID;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_TITLE;
import static com.hapramp.ui.activity.WinnerDeclarationActivity.EXTRA_COMPETITION_IMAGE_URL;
import static com.hapramp.ui.activity.WinnerDeclarationActivity.EXTRA_COMPETITION_TAGS;

public class CompetitionFeedItemView extends FrameLayout {
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
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  private Context mContext;
  private CompetitionModel mCompetition;
  private boolean mShowDeclareResultButton;
  private CompetitionItemDeleteListener deleteListener;
  private CountDownTimerUtils countDownTimerUtils;

  public CompetitionFeedItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    countDownTimerUtils = new CountDownTimerUtils();
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

    competitionTitle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToCompetitionDetailsPage();
      }
    });

    popupMenuDots.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        showPopup();
      }
    });
  }

  private void navigateToCompetitionDetailsPage() {
    Intent i = new Intent(mContext, CompetitionDetailsActivity.class);
    i.putExtra(EXTRA_HEADER_PARCEL, mCompetition);
    mContext.startActivity(i);
  }

  private void showPopup() {
    int menu_res_id;
    menu_res_id = R.menu.competition_post_menu_with_delete;
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
    PopupMenu popup = new PopupMenu(contextThemeWrapper, popupMenuDots);
    popup.getMenuInflater().inflate(menu_res_id, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
          showAlertDialogForDelete();
          return true;
        }
        return false;
      }
    });
    popup.show();
  }

  private void showAlertDialogForDelete() {
    new AlertDialog.Builder(mContext)
      .setTitle("Delete Competition")
      .setMessage("Do you want to Delete ? ")
      .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteCompetition();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void deleteCompetition() {
    RetrofitServiceGenerator.getService().deleteCompetition(mCompetition.getmId()).enqueue(new Callback<DeleteCompetitionResponse>() {
      @Override
      public void onResponse(Call<DeleteCompetitionResponse> call, Response<DeleteCompetitionResponse> response) {
        if (response.isSuccessful()) {
          if (deleteListener != null) {
            deleteListener.onCompetitionItemDeleted();
          }
        } else {
          Toast.makeText(mContext, "Failed to delete competition", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<DeleteCompetitionResponse> call, Throwable t) {
        Toast.makeText(mContext, "Failed to delete competition", Toast.LENGTH_LONG).show();
      }
    });
  }

  public CompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CompetitionFeedItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private boolean isAdminOfThisCompetition() {
    return mCompetition
      .getmAdmin()
      .getmUsername()
      .equals(HaprampPreferenceManager
        .getInstance()
        .getCurrentSteemUsername());
  }

  private boolean competitionNotStarted() {
    long now = System.currentTimeMillis();
    long startTime = MomentsUtils.getMillisFromTime(mCompetition.getmStartsAt());
    return now < startTime;
  }

  public void bindCompetitionData(CompetitionModel competition) {
    this.mCompetition = competition;

    if (isAdminOfThisCompetition() && competitionNotStarted()) {
      popupMenuDots.setVisibility(VISIBLE);
    } else {
      popupMenuDots.setVisibility(GONE);
    }

    ImageHandler.loadCircularImage(mContext, feedOwnerPic,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        competition.getmAdmin().getmUsername()));
    feedOwnerTitle.setText(competition.getmAdmin().getmUsername());
    feedOwnerSubtitle.setText(String.format(" | %s", MomentsUtils.getFormattedTime(competition.getmCreatedAt())));
    setCommunities(competition.getCommunities());
    ImageHandler.load(mContext, featuredImagePost, competition.getmImage());
    competitionTitle.setText(competition.getmTitle());
    setStartedTime();
    participantsCount.setText(String.format(Locale.US, "%d%s",
      mCompetition.getmParticipantCount(),
      mCompetition.getmParticipantCount() > 1 ? " Entries" : " Entry"));
    postSnippet.setText(competition.getmDescription());
    totalPrize.setText(PrizeMoneyFilter.getTotalPrize(competition.getPrizes()));
    invalidateActionButton();
  }

  private void setCommunities(List<CommunityModel> communities) {
    try {
      List<String> cm = new ArrayList<>();
      for (int i = 0; i < communities.size(); i++) {
        cm.add(communities.get(i).getmTag());
      }
      communityStripeView.setCommunities(cm);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
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

  /**
   * Timers are same for admin and user interface.
   * sets action button when competition is not started.
   */
  private void setWhenNotStarted(String startsIn) {
    startsIn = MomentsUtils.getFormattedTime(startsIn);
    if (actionButton != null) {
      actionButton.setEnabled(false);
      actionButton.setClickable(false);
      actionButton.setText("Starts " + startsIn);
      setStartsInTimer();
    }
  }

  /**
   * Sets different action for users and admin interface.
   *
   * @param isAdmin flag for admin.
   * @param endsAt  time when competition ends
   */
  private void setWhenRunning(boolean isAdmin, String endsAt) {
    endsAt = MomentsUtils.getFormattedTime(endsAt);
    if (isAdmin) {
      actionButton.setEnabled(false);
      actionButton.setClickable(false);
      actionButton.setText("Ends " + endsAt);
      setEndsInTimer();
    } else {
      actionButton.setEnabled(true);
      actionButton.setClickable(true);
      actionButton.setText("Participate");
      actionButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToCompetitionDetailsPage();
        }
      });
    }
  }

  /**
   * @param isAdmin flag for admin
   * @param endedAt time when competition ended
   */
  private void setWhenEnded(boolean isAdmin, String endedAt) {
    if (isAdmin) {
      actionButton.setEnabled(true);
      actionButton.setClickable(true);
      actionButton.setText("Declare Winners");
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
      actionButton.setText("Ended " + endedAt);
    }
    startsIn.setText("Ended");
  }

  /**
   * set state when winners are decided
   */
  private void setWhenWinnerAnnounced() {
    actionButton.setEnabled(true);
    actionButton.setClickable(true);
    actionButton.setText("See Winners");
    this.startsIn.setText("Ended");
    actionButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openWinnersList();
      }
    });
  }

  private void setStartsInTimer() {
    long now = System.currentTimeMillis();
    long starts = MomentsUtils.getMillisFromTime(mCompetition.getmStartsAt());
    long left = starts - now;
    countDownTimerUtils.setTimerWith(left, 1000, new CountDownTimerUtils.TimerUpdateListener() {
      @Override
      public void onFinished() {
        setStartedTime();
        invalidateActionButton();
      }

      @Override
      public void onRunningTimeUpdate(String updateTime) {
        if (actionButton != null) {
          setStartedTime();
          actionButton.setText("Starts In " + updateTime);
        }
      }
    });
    countDownTimerUtils.start();
  }

  private void setEndsInTimer() {
    long now = System.currentTimeMillis();
    long ends = MomentsUtils.getMillisFromTime(mCompetition.getmEndsAt());
    long left = ends - now;
    countDownTimerUtils.setTimerWith(left, 1000, new CountDownTimerUtils.TimerUpdateListener() {
      @Override
      public void onFinished() {
        invalidateActionButton();
      }

      @Override
      public void onRunningTimeUpdate(String updateTime) {
        if (actionButton != null) {
          if (isAdminOfThisCompetition()) {
            actionButton.setText("Ends In: " + updateTime);
          }
        }
      }
    });
    countDownTimerUtils.start();
  }

  private void setStartedTime() {
    long now = System.currentTimeMillis();
    long startsAt = MomentsUtils.getMillisFromTime(mCompetition.getmStartsAt());
    StringBuilder st = new StringBuilder();
    if (startsAt > now) {
      st.append("Starts ").append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    } else {
      st.append("Started ").append(MomentsUtils.getFormattedTime(mCompetition.getmStartsAt()));
    }
    this.startsIn.setText(st.toString());
  }

  private void openSubmissionPage() {
    Intent intent = new Intent(mContext, ParticipateEditorActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, mCompetition.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, mCompetition.getmTitle());
    intent.putExtra(EXTRA_COMPETITION_HASHTAG, mCompetition.getmParticipationHashtag());
    mContext.startActivity(intent);
    ((AppCompatActivity) mContext).overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
  }

  private void openResultDeclarationPage() {
    Intent intent = new Intent(mContext, WinnerDeclarationActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, mCompetition.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, mCompetition.getmTitle());
    intent.putExtra(EXTRA_COMPETITION_IMAGE_URL, mCompetition.getmImage());
    intent.putParcelableArrayListExtra(EXTRA_COMPETITION_TAGS, mCompetition.getCommunities());
    mContext.startActivity(intent);
  }

  private void openWinnersList() {
    Intent intent = new Intent(mContext, WinnersFeedListActivity.class);
    intent.putExtra(EXTRA_COMPETITION_ID, mCompetition.getmId());
    intent.putExtra(EXTRA_COMPETITION_TITLE, mCompetition.getmTitle());
    mContext.startActivity(intent);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    invalidateActionButton();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    countDownTimerUtils.cancel();
  }

  public void setDeleteListener(CompetitionItemDeleteListener deleteListener) {
    this.deleteListener = deleteListener;
  }

  public interface CompetitionItemDeleteListener {
    void onCompetitionItemDeleted();
  }
}
