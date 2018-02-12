package com.hapramp.views.feedlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;

import butterknife.BindView;

/**
 * Created by Ankit on 2/11/2018.
 */

public class FeedListView extends FrameLayout {


    @BindView(R.id.feed_owner_pic)
    ImageView feedOwnerPic;
    @BindView(R.id.reference_line)
    Space referenceLine;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
    @BindView(R.id.image_mock)
    FrameLayout imageMock;
    @BindView(R.id.post_title)
    TextView postTitle;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.line1)
    TextView line1;
    @BindView(R.id.line2)
    TextView line2;
    @BindView(R.id.line3)
    TextView line3;
    @BindView(R.id.line4)
    TextView line4;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.mock1)
    FrameLayout mock1;
    @BindView(R.id.feed_owner_pic1)
    ImageView feedOwnerPic1;
    @BindView(R.id.reference_line1)
    Space referenceLine1;
    @BindView(R.id.feed_owner_title1)
    TextView feedOwnerTitle1;
    @BindView(R.id.feed_owner_subtitle1)
    TextView feedOwnerSubtitle1;
    @BindView(R.id.img11)
    ImageView img11;
    @BindView(R.id.post_header_container1)
    RelativeLayout postHeaderContainer1;
    @BindView(R.id.image_mock1)
    FrameLayout imageMock1;
    @BindView(R.id.post_title1)
    TextView postTitle1;
    @BindView(R.id.tags1)
    TextView tags1;
    @BindView(R.id.line11)
    TextView line11;
    @BindView(R.id.line21)
    TextView line21;
    @BindView(R.id.line31)
    TextView line31;
    @BindView(R.id.line41)
    TextView line41;
    @BindView(R.id.post_meta_container1)
    RelativeLayout postMetaContainer1;
    @BindView(R.id.shimmer_view_container1)
    ShimmerFrameLayout shimmerViewContainer1;
    @BindView(R.id.mock2)
    FrameLayout mock2;
    @BindView(R.id.no_post_sad_icon)
    TextView noPostSadIcon;
    @BindView(R.id.failed_message_title)
    TextView failedMessageTitle;
    @BindView(R.id.failed_message_details)
    TextView failedMessageDetails;
    @BindView(R.id.failed_message_card_container)
    RelativeLayout failedMessageCardContainer;
    @BindView(R.id.sad_icon)
    TextView sadIcon;
    @BindView(R.id.nopost_message_title)
    TextView nopostMessageTitle;
    @BindView(R.id.nopost_message_details)
    TextView nopostMessageDetails;
    @BindView(R.id.nopost_message_card_container)
    RelativeLayout nopostMessageCardContainer;
    @BindView(R.id.feedRecyclerView)
    RecyclerView feedRecyclerView;
    @BindView(R.id.feedRefreshLayout)
    SwipeRefreshLayout feedRefreshLayout;
    @BindView(R.id.moveToTop)
    TextView moveToTop;
    @BindView(R.id.retryFeedLoadingBtn)
    TextView retryFeedLoadingBtn;
    private Context mContext;
    private View rootView;

    // TODO: 2/12/2018  add view setters and attach adapter

    public FeedListView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public FeedListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FeedListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.feed_list_view, this);
        attachListeners();

    }

    private void attachListeners() {

        retryFeedLoadingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(feedListViewListener!=null){
                    feedListViewListener.onRetryFeedLoading();
                }
            }
        });

        feedRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(feedListViewListener!=null){
                    feedListViewListener.onRefreshFeeds();
                }
            }
        });

    }

    // State Methods
    public void initialLoading(){
        // TODO: 2/12/2018 show shimmers and hide other views
    }

    public void initialFetched(){
        // TODO: 2/12/2018 set list items to adapter| show recyclerView  | hide other views
    }

    public void noInitialFeeds(){
        // TODO: 2/12/2018 show no posts view  | hide other views
    }

    public void failedToLoadInitials(){
        // TODO: 2/12/2018 show failed view and hide other views
    }

    public void feedRefreshing(){
        // TODO: 2/12/2018 show refreshing view
    }

    public void feedsRefreshed(){
        // TODO: 2/12/2018 set items to adapter | hide other views | disable swiperefreshing views
    }

    public void failedToRefresh(){
        // TODO: 2/12/2018 show error toast | if adapter has no posts already, then call failedToLoadInitial | diable swiperefresing views
    }

    public void loadedMoreFeeds(){
        // TODO: 2/12/2018 append items to adapter
    }

    private FeedListViewListener feedListViewListener;

    public void setFeedListViewListener(FeedListViewListener feedListViewListener) {
        this.feedListViewListener = feedListViewListener;
    }

    public interface FeedListViewListener {

        void onRetryFeedLoading();

        void onRefreshFeeds();

        void onLoadMoreFeeds();

    }

}
