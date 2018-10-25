package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CompetitionEntriesFetchCallback;
import com.hapramp.models.CompetitionWinnerModel;
import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.RankableCompetitionItemRecyclerAdapter;
import com.hapramp.views.competition.RankableCompetitionFeedItemView;
import com.hapramp.views.competition.WinnerItemView;
import com.hapramp.views.competition.WinnerPlaceholderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerDeclarationActivity extends AppCompatActivity implements RankableCompetitionFeedItemView.RankableCompetitionItemListener, CompetitionEntriesFetchCallback {
  public static final String EXTRA_COMPETITION_ID = "competition_id";
  public static final String EXTRA_COMPETITION_TITLE = "competition_title";
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
  private RankableCompetitionItemRecyclerAdapter rankableCompetitionItemRecyclerAdapter;
  private BottomSheetBehavior<RelativeLayout> sheetBehavior;
  //rank->Winner
  private Map<Integer, CompetitionWinnerModel> shortlistedWinnersMap;
  private RankableCompetitionFeedItem rankAssigneeContext;
  private DataStore dataStore;
  private String mCompetitionId;
  private String mCompetitionTitle;

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
    dataStore = new DataStore();
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
      mCompetitionId = intent.getStringExtra(EXTRA_COMPETITION_ID);
      mCompetitionTitle = intent.getStringExtra(EXTRA_COMPETITION_TITLE);
    }
  }

  private void bindSampleData() {
    ArrayList<RankableCompetitionFeedItem> items = new ArrayList<>();
    RankableCompetitionFeedItem si;
    String[] imgs = {"https://www.india-forums.com/tellybuzz/images/uploads/F77_Drashti-Dhami-2.jpg",
      "https://relendo-sharetribe-production.s3.amazonaws.com/images/listing_images/images/29818/big/phantom-4-pro.jpg",
      "http://www.hotgossips.in/wp-content/uploads/kajal-agarwal.jpg",
      "https://content2.jdmagicbox.com/events/A405481/A405481_list_20160525102916.jpg",
      "https://d775ypbe1855i.cloudfront.net/large/70/100RedRosesinaVase.jpg"
    };
    String[] users = {"bxute", "bxute", "the-dragon", "ansarimofid", "rajatdangi"};
    String[] titles = {"Dhrishti Dhami", "PHANTOM 4PRO VISIONARY INTELLIGENCE ELEVATED IMAGINATION", "Kajal agrawal", "Aditi Sharma", "Roses"};
    for (int i = 0; i < imgs.length; i++) {
      si = new RankableCompetitionFeedItem();
      si.setItemId("ID" + i);
      si.setUsername(users[i]);
      si.setCreatedAt("2018-10-10T12:14:39");
      ArrayList<String> tags = new ArrayList<>();
      tags.add("hapramp-dance");
      tags.add("hapramp-travel");
      si.setTags(tags);
      si.setFeaturedImageLink(imgs[i]);
      si.setTitle(titles[i]);
      si.setDescription("Kajal Aggarwal (born 19 June 1985) is an Indian film actress and model. She has established a career in the Telugu and Tamil film industries and has been nominated for four Filmfare Awards South.[4][5][6] In addition to acting, Kajal participates in stage shows and is a celebrity endorser for brands and products.");
      si.setChildrens(8);
      si.setPayout("100 SBD");
      items.add(si);
    }
    rankableCompetitionItemRecyclerAdapter.setRankableCompetitionFeedItems(items);
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
    ArrayList<RankableCompetitionFeedItem> rankableItems = new ArrayList<>();
    for (int i = 0; i < entries.size(); i++) {
      rankableItems.add(new RankableCompetitionFeedItem(entries.get(i)));
    }
    rankableCompetitionItemRecyclerAdapter.setRankableCompetitionFeedItems(rankableItems);
  }

  @Override
  public void onCompetitionsEntriesFetchError() {

  }
}
