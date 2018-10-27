package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CompetitionEntriesFetchCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CompetitionEntryResponse;
import com.hapramp.models.CompetitionWinnerModel;
import com.hapramp.models.FormattedBodyResponse;
import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.models.WinnersRankBody;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.RankableCompetitionItemRecyclerAdapter;
import com.hapramp.utils.ErrorUtils;
import com.hapramp.utils.PostHashTagPreprocessor;
import com.hapramp.views.competition.RankableCompetitionFeedItemView;
import com.hapramp.views.competition.WinnerItemView;
import com.hapramp.views.competition.WinnerPlaceholderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnerDeclarationActivity extends AppCompatActivity implements RankableCompetitionFeedItemView.RankableCompetitionItemListener, CompetitionEntriesFetchCallback, SteemPostCreator.SteemPostCreatorCallback {
  public static final String EXTRA_COMPETITION_ID = "competition_id";
  public static final String EXTRA_COMPETITION_TITLE = "competition_title";
  public static final String EXTRA_COMPETITION_TAGS = "competition_tags";
  public static final String EXTRA_COMPETITION_IMAGE_URL = "competition_image_url";

  private final int MAX_WINNERS_ALLOWED = 3;
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.overflowBtn)
  ImageView overflowBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.competition_winner_selection_list)
  RecyclerView competitionWinnerSelectionList;
  @BindView(R.id.bottom_sheet)
  RelativeLayout bottomSheet;
  @BindView(R.id.shadow_tip)
  FrameLayout shadowTip;
  @BindView(R.id.declared_winners_icon)
  ImageView declaredWinnersIcon;
  @BindView(R.id.declared_winners)
  TextView declaredWinners;
  @BindView(R.id.publish_competition_result_btn)
  TextView publishCompetitionResultBtn;
  @BindView(R.id.bottom_sheet_header)
  RelativeLayout bottomSheetHeader;
  @BindView(R.id.winner_list_container)
  LinearLayout winnerListContainer;
  @BindView(R.id.bottom_sheet_content)
  RelativeLayout bottomSheetContent;
  @BindView(R.id.shortlist_winners_message_panel)
  TextView shortlistWinnersMessagePanel;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  @BindView(R.id.declared_winners_label)
  TextView declaredWinnersLabel;
  private RankableCompetitionItemRecyclerAdapter rankableCompetitionItemRecyclerAdapter;
  private BottomSheetBehavior<RelativeLayout> sheetBehavior;
  //rank->Winner
  private Map<Integer, CompetitionWinnerModel> shortlistedWinnersMap;
  private RankableCompetitionFeedItem rankAssigneeContext;
  private DataStore dataStore;
  private String mCompetitionId;
  private String mCompetitionTitle;
  private String mCompetitionImage;
  private ProgressDialog progressDialog;
  private ArrayList<CommunityModel> mCompetitionHashtags;
  private SteemPostCreator steemPostCreator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_winner_declaration);
    ButterKnife.bind(this);
    initializeObjects();
    collectExtras();
    attachListeners();
    fetchEntries();
    //bindSampleData();
  }

  private void initializeObjects() {
    progressDialog = new ProgressDialog(this);
    dataStore = new DataStore();
    steemPostCreator = new SteemPostCreator();
    steemPostCreator.setSteemPostCreatorCallback(this);
    rankableCompetitionItemRecyclerAdapter = new RankableCompetitionItemRecyclerAdapter(this);
    competitionWinnerSelectionList.setLayoutManager(new LinearLayoutManager(this));
    rankableCompetitionItemRecyclerAdapter.setRankableCompetitionItemListener(this);
    competitionWinnerSelectionList.setAdapter(rankableCompetitionItemRecyclerAdapter);
    sheetBehavior = BottomSheetBehavior.from(bottomSheet);
    sheetBehavior.setHideable(false);
    shortlistedWinnersMap = new HashMap<>();
    invalidateWinnerList();
  }

  private void attachListeners() {
    publishCompetitionResultBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAlertDialogForResultPublish();
      }
    });
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    competitionWinnerSelectionList.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (sheetBehavior != null) {
          if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
          }
        }
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });

    sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
          case BottomSheetBehavior.STATE_HIDDEN:
            break;
          case BottomSheetBehavior.STATE_EXPANDED: {
            //Toast.makeText(WinnerDeclarationActivity.this,"Expanded",Toast.LENGTH_LONG).show();
            //btnBottomSheet.setText("Close Sheet");
          }
          break;
          case BottomSheetBehavior.STATE_COLLAPSED: {
            //Toast.makeText(WinnerDeclarationActivity.this,"Collapsed",Toast.LENGTH_LONG).show();
            //btnBottomSheet.setText("Expand Sheet");
          }
          break;
          case BottomSheetBehavior.STATE_DRAGGING:
            break;
          case BottomSheetBehavior.STATE_SETTLING:
            break;
        }
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {

      }
    });
  }

  private void updateServerWithRanks() {
    showProgressDialog(true, "Updating ranks...");
    RetrofitServiceGenerator.getService().updateServerWithRanks(mCompetitionId, getWinnerRankBody()).enqueue(new Callback<CompetitionEntryResponse>() {
      @Override
      public void onResponse(Call<CompetitionEntryResponse> call, Response<CompetitionEntryResponse> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful()) {
          onRankUpdatedToServer();
        } else {
          try {
            onRankUpdateFailed(ErrorUtils.parseError(response).getmMessage());
          }
          catch (Exception e) {
            onRankUpdateFailed("");
          }
        }
      }

      @Override
      public void onFailure(Call<CompetitionEntryResponse> call, Throwable t) {
        showProgressDialog(false, "");
        onRankUpdateFailed(t.toString());
      }
    });
  }

  private void onRankUpdateFailed(String msg) {
    Toast.makeText(this, "Failed to update ranks, Try again!: " + msg, Toast.LENGTH_LONG).show();
  }

  private void onResultAnnounceFailed(String msg) {
    Toast.makeText(this, "Failed to announce result, Try again!: " + msg, Toast.LENGTH_LONG).show();
  }

  private WinnersRankBody getWinnerRankBody() {
    Iterator<Map.Entry<Integer, CompetitionWinnerModel>> ranksMap = shortlistedWinnersMap.entrySet().iterator();
    List<WinnersRankBody.Ranks> ranks = new ArrayList<>();
    while (ranksMap.hasNext()) {
      Map.Entry<Integer, CompetitionWinnerModel> entry = ranksMap.next();
      ranks.add(new WinnersRankBody.Ranks(String.format("%s/%s", entry.getValue().getUsername(), entry.getValue().getPermlink()),
        entry.getKey()));
    }
    WinnersRankBody winnersRankBody = new WinnersRankBody();
    winnersRankBody.setmRanks(ranks);
    return winnersRankBody;
  }

  private void announceResult() {
    showProgressDialog(true, "Announcing results...");
    RetrofitServiceGenerator.getService().announceResults(mCompetitionId).enqueue(new Callback<CompetitionEntryResponse>() {
      @Override
      public void onResponse(Call<CompetitionEntryResponse> call, Response<CompetitionEntryResponse> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful()) {
          onResultAnnouncedOnServer();
        } else {
          onResultAnnounceFailed("");
        }
      }

      @Override
      public void onFailure(Call<CompetitionEntryResponse> call, Throwable t) {
        showProgressDialog(false, "");
        onResultAnnounceFailed("");
      }
    });
  }

  private void onRankUpdatedToServer() {
    announceResult();
  }

  private void onResultAnnouncedOnServer() {
    showProgressDialog(true, "Preparing your blog...");
    RetrofitServiceGenerator.getService().requestWinnersPostBody(mCompetitionId).enqueue(new Callback<FormattedBodyResponse>() {
      @Override
      public void onResponse(Call<FormattedBodyResponse> call, Response<FormattedBodyResponse> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful()) {
          postWinnersBlogOnSteem(response.body().getmBody());
        } else {
          onFailedToFetchBody();
        }
      }

      @Override
      public void onFailure(Call<FormattedBodyResponse> call, Throwable t) {
        showProgressDialog(false, "");
        onFailedToFetchBody();
      }
    });
  }

  private void onFailedToFetchBody() {
    Toast.makeText(this, "Failed to prepare winners announcement blog", Toast.LENGTH_LONG).show();
  }

  private void postWinnersBlogOnSteem(String body) {
    String postPermlink = PermlinkGenerator.getPermlink("winner-list-" + mCompetitionTitle + "-" + mCompetitionId);
    ArrayList<String> tags = getHashTags();
    List<String> images = new ArrayList<>();
    images.add(mCompetitionImage);
    tags = PostHashTagPreprocessor.processHashtags(tags);
    showProgressDialog(true, "Publishing Winners blog...");
    steemPostCreator.createPost(body, "", images, tags, postPermlink);
  }

  private ArrayList<String> getHashTags() {
    ArrayList<String> tags = new ArrayList<>();
    for (int i = 0; i < mCompetitionHashtags.size(); i++) {
      tags.add(mCompetitionHashtags.get(i).getmTag());
    }
    return tags;
  }

  private void showAlertDialogForResultPublish() {
    new AlertDialog.Builder(this)
      .setTitle("Publish Results")
      .setMessage("Are you sure ? ")
      .setPositiveButton("Yes, Publish", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          updateServerWithRanks();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void fetchEntries() {
    if (mCompetitionId != null) {
      dataStore.requestCompetitionEntries(mCompetitionId, this);
    } else {
      showErrorMessage();
    }
  }

  private void showErrorMessage() {

  }

  private void collectExtras() {
    Intent intent = getIntent();
    if (intent != null) {
      mCompetitionHashtags = intent.getParcelableArrayListExtra(EXTRA_COMPETITION_TAGS);
      mCompetitionId = intent.getStringExtra(EXTRA_COMPETITION_ID);
      mCompetitionTitle = intent.getStringExtra(EXTRA_COMPETITION_TITLE);
      mCompetitionImage = intent.getStringExtra(EXTRA_COMPETITION_IMAGE_URL);
    }
  }

  private void invalidateWinnerList() {
    winnerListContainer.removeAllViews();
    int count = 0;
    for (int rank = 1; rank <= MAX_WINNERS_ALLOWED; rank++) {
      if (shortlistedWinnersMap.containsKey(rank)) {
        count++;
        addWinnerView(shortlistedWinnersMap.get(rank));
      } else {
        addPlaceHolderView(rank);
      }
    }
    declaredWinners.setText(String.valueOf(count));
    if (count == 0) {
      declaredWinnersIcon.setImageResource(R.drawable.ranking);
      publishCompetitionResultBtn.setEnabled(false);
    } else {
      declaredWinnersIcon.setImageResource(R.drawable.ranking_filled);
      publishCompetitionResultBtn.setEnabled(true);
    }
  }

  private void addWinnerView(CompetitionWinnerModel winnerModel) {
    WinnerItemView itemView = new WinnerItemView(this);
    itemView.setWinnerItemViewCallback(new WinnerItemView.WinnerItemViewCallback() {
      @Override
      public void onItemRemoveClicked(int rank) {
        removeFromWinnerList(rank);
      }
    });
    itemView.setWinnerData(winnerModel);
    if (winnerListContainer != null) {
      winnerListContainer.addView(itemView);
    }
  }

  private void addPlaceHolderView(int rank) {
    WinnerPlaceholderView winnerPlaceholderView = new WinnerPlaceholderView(this);
    winnerPlaceholderView.setPlaceholderRank(rank);
    winnerPlaceholderView.setWinnerPlaceholderCallback(new WinnerPlaceholderView.WinnerPlaceholderCallback() {
      @Override
      public void onPlaceholderClicked(int rank) {
        if (rankAssigneeContext != null) {
          assigneRank(rank, rankAssigneeContext);
          invalidateWinnerList();
          updateRanksInListView();
          rankAssigneeContext = null;
        } else {
          Toast.makeText(WinnerDeclarationActivity.this, "Select an item to assign rank.", Toast.LENGTH_LONG).show();
        }
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      }
    });
    if (winnerListContainer != null) {
      winnerListContainer.addView(winnerPlaceholderView);
    }
  }

  /**
   * This method removes a feed at given rank
   * update the list
   * update the winner view
   *
   * @param rank rank at which feed is removed.
   */
  private void removeFromWinnerList(int rank) {
    if (shortlistedWinnersMap.containsKey(rank)) {
      shortlistedWinnersMap.remove(rank);
      invalidateWinnerList();
      updateRanksInListView();
    }
  }

  /**
   * This method adds a rank v/s feed entry in shortlisted map.
   *
   * @param rank     rank to assign
   * @param feedItem feed on which rank will be assigned
   */
  private void assigneRank(int rank, RankableCompetitionFeedItem feedItem) {
    CompetitionWinnerModel competitionWinnerModel = new CompetitionWinnerModel();
    competitionWinnerModel.setRank(rank);
    competitionWinnerModel.setPermlink(feedItem.getPermlink());
    competitionWinnerModel.setId(feedItem.getItemId());
    competitionWinnerModel.setImageUrl(feedItem.getFeaturedImageLink());
    competitionWinnerModel.setTitle(feedItem.getTitle());
    competitionWinnerModel.setUsername(feedItem.getUsername());
    shortlistedWinnersMap.put(rank, competitionWinnerModel);
  }

  private void updateRanksInListView() {
    Map<String, Integer> rankMap = new HashMap<>();
    for (int rank = 1; rank <= MAX_WINNERS_ALLOWED; rank++) {
      if (shortlistedWinnersMap.containsKey(rank)) {
        rankMap.put(shortlistedWinnersMap.get(rank).getId(), rank);
      }
    }
    rankableCompetitionItemRecyclerAdapter.setRankList(rankMap);
  }

  @Override
  public void onAssignRankClicked(RankableCompetitionFeedItem item) {
    rankAssigneeContext = item;
    if (sheetBehavior != null) {
      sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
  }

  @Override
  public void onCompetitionsEntriesAvailable(List<Feed> entries) {
    marshellAndSetEntriesToList(entries);
  }

  private void marshellAndSetEntriesToList(List<Feed> entries) {
    showProgressBar(false);
    if (entries.size() > 0) {
      ArrayList<RankableCompetitionFeedItem> rankableItems = new ArrayList<>();
      for (int i = 0; i < entries.size(); i++) {
        rankableItems.add(new RankableCompetitionFeedItem(entries.get(i)));
      }
      showMessagePanel(false, "");
      rankableCompetitionItemRecyclerAdapter.setRankableCompetitionFeedItems(rankableItems);
    } else {
      showMessagePanel(true, "No entries available!");
    }
  }

  private void showProgressBar(boolean show) {
    if (loadingProgressBar != null) {
      if (show) {
        loadingProgressBar.setVisibility(View.VISIBLE);
      } else {
        loadingProgressBar.setVisibility(View.GONE);
      }
    }
  }

  private void showMessagePanel(boolean show, String msg) {
    if (shortlistWinnersMessagePanel != null) {
      if (show) {
        shortlistWinnersMessagePanel.setVisibility(View.VISIBLE);
        shortlistWinnersMessagePanel.setText(msg);
      } else {
        shortlistWinnersMessagePanel.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onCompetitionsEntriesFetchError() {
    showProgressBar(false);
    showMessagePanel(true, "Failed to fetch entries");
  }

  @Override
  public void onPostCreatedOnSteem() {
    showProgressDialog(false, "");
    Toast.makeText(this, "Winners blog posted!", Toast.LENGTH_LONG).show();
    finish();
  }

  private void showProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    }
  }

  @Override
  public void onPostCreationFailedOnSteem(String msg) {
    showProgressDialog(false, "");
    Toast.makeText(this, "Failed to post winners blog", Toast.LENGTH_LONG).show();
  }
}
