package com.hapramp.views.feedlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.HomeFeedsAdapter;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.PixelUtils;
import com.hapramp.utils.SpaceDecorator;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/11/2018.
 */

public class FeedListView extends FrameLayout implements HomeFeedsAdapter.OnLoadMoreListener {
  public static final String TAG = FeedListView.class.getSimpleName();
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.reference_line)
  Space referenceLine;
  @BindView(R.id.feed_owner_title)
  View feedOwnerTitle;
  @BindView(R.id.date_mock)
  View dateMock;
  @BindView(R.id.com1)
  View com1;
  @BindView(R.id.com2)
  View com2;
  @BindView(R.id.com3)
  View com3;
  @BindView(R.id.post_header_container)
  RelativeLayout postHeaderContainer;
  @BindView(R.id.image_mock)
  FrameLayout imageMock;
  @BindView(R.id.post_title)
  View postTitle;
  @BindView(R.id.line1)
  View line1;
  @BindView(R.id.line2)
  View line2;
  @BindView(R.id.line3)
  View line3;
  @BindView(R.id.line4)
  View line4;
  @BindView(R.id.post_meta_container)
  RelativeLayout postMetaContainer;
  @BindView(R.id.star_mock)
  View starMock;
  @BindView(R.id.star_count)
  View starCount;
  @BindView(R.id.comment_mock)
  View commentMock;
  @BindView(R.id.comment_count_mock)
  View commentCountMock;
  @BindView(R.id.comment_mock_container)
  LinearLayout commentMockContainer;
  @BindView(R.id.hapcoin_mock)
  View hapcoinMock;
  @BindView(R.id.comment_count_mock1)
  View commentCountMock1;
  @BindView(R.id.mock1)
  FrameLayout mock1;
  @BindView(R.id.mockContainer)
  RelativeLayout mockContainer;
  @BindView(R.id.failed_message_details)
  TextView failedMessageDetails;
  @BindView(R.id.failed_message_card_container)
  RelativeLayout failedMessageCardContainer;
  @BindView(R.id.retryFeedLoadingBtn)
  TextView retryFeedLoadingBtn;
  @BindView(R.id.failedToLoadViewContainer)
  RelativeLayout failedToLoadViewContainer;
  @BindView(R.id.nopost_message_title)
  TextView nopostMessageTitle;
  @BindView(R.id.nopost_message_details)
  TextView nopostMessageDetails;
  @BindView(R.id.nopost_message_card_container)
  RelativeLayout nopostMessageCardContainer;
  @BindView(R.id.noPostLoadedViewContainer)
  RelativeLayout noPostLoadedViewContainer;
  @BindView(R.id.feedRecyclerView)
  RecyclerView feedRecyclerView;
  @BindView(R.id.feedRefreshLayout)
  SwipeRefreshLayout feedRefreshLayout;
  @BindView(R.id.moveToTop)
  TextView moveToTop;
  private int topOffset;
  private int bottomOffset;
  private boolean wantBottomSpace = true;
  private boolean wantTopSpace = true;
  private Context mContext;
  private View rootView;
  private HomeFeedsAdapter homeFeedsAdapter;
  private LinearLayoutManager layoutManager;
  private ViewItemDecoration viewItemDecoration;
  private SpaceDecorator spaceDecorator;
  private int y;
  private FeedListViewListener feedListViewListener;
  private String noDataMessage = "No Feed Available in This Section";
  private String noDataTitle;

  public FeedListView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.feed_list_view, this);
    ButterKnife.bind(this, rootView);
    attachListeners();
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
    viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(wantTopSpace, topOffset);
    spaceDecorator = new SpaceDecorator();
    layoutManager = new LinearLayoutManager(mContext);
    feedRecyclerView.setLayoutManager(layoutManager);
    homeFeedsAdapter = new HomeFeedsAdapter(context);
    homeFeedsAdapter.setOnLoadMoreListener(this);
    feedRecyclerView.addItemDecoration(spaceDecorator);
    feedRecyclerView.addItemDecoration(viewItemDecoration);
    feedRecyclerView.setAdapter(homeFeedsAdapter);
    feedRecyclerView.setNestedScrollingEnabled(false);
    feedRefreshLayout.setProgressViewOffset(false, PixelUtils.dpToPx(72), PixelUtils.dpToPx(120));
    feedRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorPrimary));
  }

  private void attachListeners() {

    retryFeedLoadingBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (feedListViewListener != null) {
          feedListViewListener.onRetryFeedLoading();
          initialLoading();
        }
      }
    });

    feedRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        if (feedListViewListener != null) {
          feedListViewListener.onRefreshFeeds();
        }
      }
    });

    feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
          if (feedListViewListener != null) {
            if (y > 0) {
              feedListViewListener.onHideCommunityList();
            } else {
              feedListViewListener.onShowCommunityList();
            }
          }
        }
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        y = dy;
      }
    });

  }

  // State Methods
  public void initialLoading() {
    //hide recycler view
    setFeedRecyclerViewVisibility(false);
    //hide failed view
    setFailedToLoadViewVisibility(false);
    //hide no feed loaded
    setNoFeedLoadedViewVisibility(false);
    // show shimmer
    setLoadingShimmerVisibility(true);
    //reset list items
    homeFeedsAdapter.resetList();
  }

  // view controllers
  private void setFeedRecyclerViewVisibility(boolean show) {
    setViewVisibility(show, feedRecyclerView);
  }

  private void setFailedToLoadViewVisibility(boolean show) {
    setViewVisibility(show, failedToLoadViewContainer);
  }

  private void setNoFeedLoadedViewVisibility(boolean show) {
    setViewVisibility(show, noPostLoadedViewContainer);
  }

  private void setLoadingShimmerVisibility(boolean show) {
    setViewVisibility(show, mockContainer);
  }

  private void setViewVisibility(boolean show, View view) {
    if (show) {
      view.setVisibility(VISIBLE);
    } else {
      view.setVisibility(GONE);
    }
  }

  public FeedListView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FeedListView, 0, 0);
    try {
      wantTopSpace = typedArray.getBoolean(R.styleable.FeedListView_wantTopSpaceOffset, false);
      wantBottomSpace = typedArray.getBoolean(R.styleable.FeedListView_wantBottomSpaceOffset, false);
      topOffset = typedArray.getInt(R.styleable.FeedListView_topOffset, 108);
      bottomOffset = typedArray.getInt(R.styleable.FeedListView_bottomOffset, 0);
    }
    finally {
      typedArray.recycle();
    }
    init(context);
  }

  public FeedListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FeedListView, 0, 0);
    try {
      wantTopSpace = typedArray.getBoolean(R.styleable.FeedListView_wantTopSpaceOffset, false);
      wantBottomSpace = typedArray.getBoolean(R.styleable.FeedListView_wantBottomSpaceOffset, false);
      topOffset = typedArray.getInt(R.styleable.FeedListView_topOffset, 108);
      bottomOffset = typedArray.getInt(R.styleable.FeedListView_bottomOffset, 0);
    }
    finally {
      typedArray.recycle();
    }
    init(context);
  }

  public void setMessageWhenNoData(String title, String msg) {
    this.noDataTitle = title;
    this.noDataMessage = msg;
  }

  //for setting shimmer offset
  public void setTopMarginForShimmer(int dp) {
    mockContainer.setPadding(0, PixelUtils.dpToPx(dp), 0, 0);
  }

  public void setTopMarginForRecyclerView(int dp) {
    feedRecyclerView.setPadding(0, PixelUtils.dpToPx(dp), 0, 0);
  }

  public void feedsRefreshed(List<Feed> refreshedFeeds) {
    if (refreshedFeeds.size() > 0) {
      setFeedRecyclerViewVisibility(true);
      setFailedToLoadViewVisibility(false);
      setNoFeedLoadedViewVisibility(false);
      setLoadingShimmerVisibility(false);
      homeFeedsAdapter.setFeeds(refreshedFeeds);
      showRefreshingLayout(false);
    } else {
      setFeedRecyclerViewVisibility(false);
      setFailedToLoadViewVisibility(false);
      setNoFeedLoadedViewVisibility(true);
      setLoadingShimmerVisibility(false);
      showRefreshingLayout(false);
      failedToLoadViewContainer.setClickable(false);
      noPostLoadedViewContainer.setClickable(true);
      nopostMessageTitle.setText(noDataTitle);
      nopostMessageDetails.setText(noDataMessage);
    }
  }

  private void showRefreshingLayout(boolean show) {
    if (show) {
      if (!feedRefreshLayout.isRefreshing()) {
        feedRefreshLayout.post(new Runnable() {
          @Override
          public void run() {
            feedRefreshLayout.setEnabled(false);
            feedRefreshLayout.setRefreshing(true);
          }
        });
      }
    } else {
      feedRefreshLayout.setRefreshing(false);
      feedRefreshLayout.setEnabled(true);
    }
  }

  public void setClickListenerOnErrorPanelMessage(OnClickListener clickListener) {
    if (noPostLoadedViewContainer != null) {
      noPostLoadedViewContainer.setOnClickListener(clickListener);
    }
  }

  public void failedToRefresh(String msg) {
    if (homeFeedsAdapter.getFeedsCount() == 0) {
      setFeedRecyclerViewVisibility(false);
      setFailedToLoadViewVisibility(true);
      setNoFeedLoadedViewVisibility(false);
      setLoadingShimmerVisibility(false);
      showRefreshingLayout(false);
      failedToLoadViewContainer.setClickable(true);
      noPostLoadedViewContainer.setClickable(false);
      failedMessageDetails.setText("Please check your Internet connection.");
    } else {
      showRefreshingLayout(false);
    }
  }

  public void loadedMoreFeeds(List<Feed> moreFeeds) {
    if (feedRecyclerView.getVisibility() != VISIBLE) {
      setFeedRecyclerViewVisibility(true);
    }
    homeFeedsAdapter.appendFeeds(moreFeeds);
  }

  public void setFeedListViewListener(FeedListViewListener feedListViewListener) {
    this.feedListViewListener = feedListViewListener;
  }

  @Override
  public void onLoadMore() {
    if (feedListViewListener != null) {
      feedListViewListener.onLoadMoreFeeds();
    }
  }

  public interface FeedListViewListener {

    void onRetryFeedLoading();

    void onRefreshFeeds();

    void onLoadMoreFeeds();

    void onHideCommunityList();

    void onShowCommunityList();
  }
}
