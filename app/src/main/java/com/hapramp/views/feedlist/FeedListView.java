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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.ui.adapters.HomeFeedsAdapter;
import com.hapramp.steem.models.Feed;
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


    private int topOffset;
    private int bottomOffset;
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
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.mock1)
    FrameLayout mock1;
    @BindView(R.id.mockContainer)
    RelativeLayout mockContainer;
    @BindView(R.id.sad_icon)
    TextView sadIcon;
    @BindView(R.id.failed_message_title)
    TextView failedMessageTitle;
    @BindView(R.id.failed_message_details)
    TextView failedMessageDetails;
    @BindView(R.id.failed_message_card_container)
    RelativeLayout failedMessageCardContainer;
    @BindView(R.id.retryFeedLoadingBtn)
    TextView retryFeedLoadingBtn;
    @BindView(R.id.failedToLoadViewContainer)
    RelativeLayout failedToLoadViewContainer;
    @BindView(R.id.no_post_sad_icon)
    TextView noPostSadIcon;
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
    private boolean wantBottomSpace = true;
    private boolean wantTopSpace = true;
    private Context mContext;
    private View rootView;
    private HomeFeedsAdapter homeFeedsAdapter;
    private LinearLayoutManager layoutManager;
    private ViewItemDecoration viewItemDecoration;
    private SpaceDecorator spaceDecorator;
    private int y;
    public static final String TAG = FeedListView.class.getSimpleName();
    // TODO: 2/12/2018  add view setters and attach adapter

    public FeedListView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public FeedListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FeedListView, 0, 0);
        try {
            wantTopSpace = typedArray.getBoolean(R.styleable.FeedListView_wantTopSpaceOffset, false);
            wantBottomSpace = typedArray.getBoolean(R.styleable.FeedListView_wantBottomSpaceOffset, false);
        } finally {
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

        } finally {
            typedArray.recycle();
        }
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.feed_list_view, this);
        ButterKnife.bind(this, rootView);
        attachListeners();

        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(wantTopSpace , topOffset);
        spaceDecorator = new SpaceDecorator();

        layoutManager = new LinearLayoutManager(mContext);
        feedRecyclerView.setLayoutManager(layoutManager);
        homeFeedsAdapter = new HomeFeedsAdapter(context, feedRecyclerView);

        homeFeedsAdapter.setOnLoadMoreListener(this);

        feedRecyclerView.addItemDecoration(spaceDecorator);
        feedRecyclerView.addItemDecoration(viewItemDecoration);

        feedRecyclerView.setAdapter(homeFeedsAdapter);
        feedRecyclerView.setNestedScrollingEnabled(false);

        feedRefreshLayout.setProgressViewOffset(false, PixelUtils.dpToPx(72), PixelUtils.dpToPx(120));
        feedRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorPrimary));
        sadIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        noPostSadIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    //for setting shimmer offset
    public void setTopMarginForShimmer(int dp) {

        mockContainer.setPadding(0, PixelUtils.dpToPx(dp), 0, 0);

    }

    public void setTopMarginForRecyclerView(int dp) {
        feedRecyclerView.setPadding(0, PixelUtils.dpToPx(dp), 0, 0);
    }

    private void attachListeners() {

        retryFeedLoadingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedListViewListener != null) {
                    Log.d("RetryButton","clicked");
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
        l("initialLoading");
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

    public void cachedFeedFetched(List<Feed> cachedFeeds) {
        l("CachedFeedFetched");
        // TODO: 2/12/2018 set list items to adapter | show recyclerView  | hide other views
        //show recycler view
        setFeedRecyclerViewVisibility(true);
        //hide failed view
        setFailedToLoadViewVisibility(false);
        //hide no feed loaded
        setNoFeedLoadedViewVisibility(false);
        // hide shimmer
        setLoadingShimmerVisibility(false);

        homeFeedsAdapter.setFeeds(cachedFeeds);

    }

    public void noCachedFeeds() {
        l("NoCachedFeeds");
        // TODO: 2/12/2018 show no posts view  | hide other views
        //hide recycler view
        setFeedRecyclerViewVisibility(false);
        //hide failed view
        setFailedToLoadViewVisibility(false);
        //show no feed loaded
        setNoFeedLoadedViewVisibility(true);
        //hide shimmer
        setLoadingShimmerVisibility(false);

        nopostMessageTitle.setText("No Cache");
        nopostMessageDetails.setText("No Previous Cached Feeds, Fetching From Server");
        // show refreshing
        showRefreshingLayout(true);

    }

    public void onNoDataAvailable(){

        //hide recycler view
        setFeedRecyclerViewVisibility(false);
        //hide failed view
        setFailedToLoadViewVisibility(false);
        //show no feed loaded
        setNoFeedLoadedViewVisibility(true);
        //hide shimmer
        setLoadingShimmerVisibility(false);
        // show refreshing
        showRefreshingLayout(false);

    }

    public void feedRefreshing(boolean isManualRefresh) {
        l("feedRefreshing");
        if(isManualRefresh) {
            showRefreshingLayout(true);
        }else{

        }

    }

    public void feedsRefreshed(List<Feed> refreshedFeeds) {
        // TODO: 2/12/2018 set items to adapter | hide other views | disable swiperefreshing views
        l("FeedRefreshed {posts - " + refreshedFeeds.size());
        if (refreshedFeeds.size() > 0) {

            //hide recycler view
            setFeedRecyclerViewVisibility(true);
            //hide failed view
            setFailedToLoadViewVisibility(false);
            //show no feed loaded
            setNoFeedLoadedViewVisibility(false);
            //hide shimmer
            setLoadingShimmerVisibility(false);

            homeFeedsAdapter.setFeeds(refreshedFeeds);
            showRefreshingLayout(false);

        } else {
            //hide recycler view
            setFeedRecyclerViewVisibility(false);
            //hide failed view
            setFailedToLoadViewVisibility(false);
            //show no feed loaded
            setNoFeedLoadedViewVisibility(true);
            //hide shimmer
            setLoadingShimmerVisibility(false);

            showRefreshingLayout(false);
            nopostMessageTitle.setText("No Feeds");
            nopostMessageDetails.setText("No Feed Available in This Section");

        }

    }

    public void failedToRefresh(String msg) {

        l("failedToRefresh");

        if (homeFeedsAdapter.getFeedsCount() == 0) {
            //hide recycler view
            setFeedRecyclerViewVisibility(false);
            //hide failed view
            setFailedToLoadViewVisibility(true);
            //show no feed loaded
            setNoFeedLoadedViewVisibility(false);
            //hide shimmer
            setLoadingShimmerVisibility(false);

            showRefreshingLayout(false);

            failedMessageTitle.setText("Failed To Load Feeds");
            failedMessageDetails.setText("We are having issue loading feeds");

        } else {
            // hide the progress bar
            showRefreshingLayout(false);

        }

    }

    public void loadedMoreFeeds(List<Feed> moreFeeds) {

        l("loadMoreFeeds");
        // TODO: 2/12/2018 append items to adapter
        if (feedRecyclerView.getVisibility() != VISIBLE)
            setFeedRecyclerViewVisibility(true);

        homeFeedsAdapter.appendFeeds(moreFeeds);

    }

    public void failedToFetchAppendable() {

    }
    // view controllers
    private void setFeedRecyclerViewVisibility(boolean show) {

        setViewVisibility(show, feedRecyclerView);

    }

    private void setLoadingShimmerVisibility(boolean show) {

        Log.d("FeedLoadingShimmer","shown "+show);
        setViewVisibility(show, mockContainer);
        shimmerViewContainer.startShimmerAnimation();

    }

    private void setFailedToLoadViewVisibility(boolean show) {

        setViewVisibility(show, failedToLoadViewContainer);

    }

    private void setNoFeedLoadedViewVisibility(boolean show) {

        setViewVisibility(show, noPostLoadedViewContainer);

    }

    private void showRefreshingLayout(boolean show) {

        l("showRefreshingLayout " + show);

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

    private void setViewVisibility(boolean show, View view) {

        if (show) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }

    }

    private FeedListViewListener feedListViewListener;

    public void setFeedListViewListener(FeedListViewListener feedListViewListener) {
        this.feedListViewListener = feedListViewListener;
    }

    @Override
    public void onLoadMore() {
        l("onLoadMore()");
        if (feedListViewListener != null) {
            feedListViewListener.onLoadMoreFeeds();
        }
    }

    private void l(String msg) {
        Log.i("HomeFeedTest", " > [" + TAG + "]  " + msg);
    }

    public void setHasMoreToLoad(boolean hasMoreToLoad) {
        homeFeedsAdapter.setHasMoreToLoad(hasMoreToLoad);
    }

    public interface FeedListViewListener {

        void onRetryFeedLoading();

        void onRefreshFeeds();

        void onLoadMoreFeeds();

        void onHideCommunityList();

        void onShowCommunityList();

    }

}
