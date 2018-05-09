package com.hapramp.views.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.ui.activity.CommentsActivity;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.datamodels.FeedRenderTypeModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.steem.SteemHelper;
import com.hapramp.steem.SteemReplyFetcher;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.data.ActiveVote;
import com.hapramp.steem.models.data.Content;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.ShareUtils;
import com.hapramp.views.extraa.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout implements SteemReplyFetcher.SteemReplyFetchCallback {


    public static final String TAG = PostItemView.class.getSimpleName();
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
    TextView popupMenuDots;
    @BindView(R.id.featured_image_post)
    ImageView featuredImagePost;
    @BindView(R.id.post_title)
    TextView postTitle;
    @BindView(R.id.post_snippet)
    TextView postSnippet;
    @BindView(R.id.readMoreBtn)
    TextView readMoreBtn;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.hapcoinBtn)
    TextView hapcoinBtn;
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.starView)
    StarView starView;
    @BindView(R.id.youtube_indicator)
    ImageView youtubeIndicator;
    @BindView(R.id.image_container)
    RelativeLayout imageContainer;


    private Context mContext;
    private Feed mFeed;
    private Handler mHandler;
    private SteemReplyFetcher replyFetcher;

    public PostItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        replyFetcher = new SteemReplyFetcher();
        replyFetcher.setSteemReplyFetchCallback(this);
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, this);
        ButterKnife.bind(this, view);
        hapcoinBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        commentBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        popupMenuDots.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        mHandler = new Handler();

        commentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetailsPage();
            }
        });

        featuredImagePost.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetailsPage();
            }
        });

        commentCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetailsPage();
            }
        });

        readMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetailsPage();
            }
        });

        postSnippet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDetailsPage();
            }
        });

    }

    private void navigateToDetailsPage() {

        Intent detailsIntent = new Intent(mContext, DetailedActivity.class);
        detailsIntent.putExtra(Constants.EXTRAA_KEY_POST_DATA, mFeed);
        mContext.startActivity(detailsIntent);

    }

    private void bind(final Feed feed) {
        this.mFeed = feed;
        // set basic meta-info
        feedOwnerTitle.setText(feed.author);
        feedOwnerSubtitle.setText(
                String.format(mContext.getResources().getString(R.string.post_subtitle_format),
                        MomentsUtils.getFormattedTime(feed.created)));

        Content content = feed.jsonMetadata.content;
        bindPostContent(content.data);

        if (content.type.equals(Constants.CONTENT_TYPE_POST)) {
            // hide title
            postTitle.setVisibility(GONE);
            // hide read more
            readMoreBtn.setVisibility(GONE);

        } else if (content.type.equals(Constants.CONTENT_TYPE_ARTICLE)) {
            checkEllipseAndInvalidateReadMoreButton(postSnippet, readMoreBtn);
        }

        setSteemEarnings(feed.totalPayoutValue);
        setCommunities(feed.jsonMetadata.tags);
        ImageHandler.loadCircularImage(mContext, feedOwnerPic, String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), feed.author));

        bindVotes(feed.activeVotes, feed.permlink);
        //load from cache
        setCommentCount(HaprampPreferenceManager.getInstance().getCommentCount(mFeed.permlink));
        if(ConnectionUtils.isConnected(mContext)) {
            replyFetcher.requestReplyForPost(feed.author, feed.permlink);
        }
        attachListenersOnStarView();
        attachListerOnAuthorHeader();
        attachListenerForOverlowIcon();
    }

    private void attachListenerForOverlowIcon() {
        popupMenuDots.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    private void attachListerOnAuthorHeader() {

        feedOwnerPic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUserProfile();
            }
        });

        feedOwnerTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUserProfile();
            }
        });

    }

    //==================================================================================
    // Vote part begins

    private void attachListenersOnStarView() {

        starView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isConnected(mContext)) {
                    starView.onStarIndicatorTapped();
                } else {
                    Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        starView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ConnectionUtils.isConnected(mContext)) {
                    starView.onStarIndicatorLongPressed();
                } else {
                    Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    private void bindVotes(List<ActiveVote> votes, String permlink) {

        long votePercentSum = getVotePercentSum(votes);
        boolean amIVoted = checkForMyVote(votes);
        long myVotePercent = amIVoted ? getMyVotePercent(votes) : 0;
        long totalVotes = getNonZeroVoters(votes);

        starView.setVoteState(
                new StarView.Vote(
                        amIVoted,
                        permlink,
                        myVotePercent,
                        totalVotes,
                        votePercentSum))
                .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
                    @Override
                    public void onVoted(String full_permlink, int _vote) {
                        performVoteOnSteem(_vote);
                    }

                    @Override
                    public void onVoteDeleted(String full_permlink) {
                        deleteVoteOnSteem();
                    }
                });


    }

    private long getNonZeroVoters(List<ActiveVote> votes) {
        long sum = 0;
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).percent > 0) {
                sum++;
            }
        }
        return sum;
    }

    private long getMyVotePercent(List<ActiveVote> votes) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).voter.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
                return votes.get(i).percent;
            }
        }
        return 0;
    }

    private long getVotePercentSum(List<ActiveVote> activeVotes) {
        long sum = 0;
        for (int i = 0; i < activeVotes.size(); i++) {
            sum += activeVotes.get(i).percent;
        }
        return sum;
    }

    private boolean checkForMyVote(List<ActiveVote> votes) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).voter.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()) && votes.get(i).percent > 0) {
                return true;
            }
        }
        return false;
    }

    /*
    *  author of the vote: author of the pose
    *  votePower: 0 for 1-2 ratte
    *  votePower: 100 for 3-5 rate
    * */
    private void performVoteOnSteem(final int vote) {

        starView.voteProcessing();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                starView.castedVoteTemporarily();
            }
        }, 500);

        final int votePower = vote;
        Log.d("VoteTest", "voting with percent " + votePower);
        new Thread() {

            @Override
            public void run() {
                try {

                    AccountName voteFor = new AccountName(getAuthor());
                    AccountName voter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                    SteemJ steemJ = SteemHelper.getSteemInstance();

                    steemJ.vote(voter, voteFor, new Permlink(getPermlinkAsString()), (short) votePower);
                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            l("Sending Vote to App server");
                            castingVoteSuccess();
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                }

            }
        }.start();

    }

    /*
      *  author of the vote: author of the pose
      *  cancel vote
      * */
    private void deleteVoteOnSteem() {

        Log.d("VoteTest", "Deleting vote");

        starView.voteProcessing();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                starView.deletedVoteTemporarily();
            }
        }, 500);


        new Thread() {

            @Override
            public void run() {
                try {

                    AccountName voteFor = new AccountName(getAuthor());
                    AccountName voter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                    SteemJ steemJ = SteemHelper.getSteemInstance();
                    steemJ.cancelVote(voter, voteFor, new Permlink(getPermlinkAsString()));
                    l("Deleted Vote on Steem");
                    Log.d("VoteTest", "Deleted Vote");

                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            voteDeleteSuccess();
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());

                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                }

            }
        }.start();

    }

    private void castingVoteFailed() {
        if (starView != null) {
            starView.failedToCastVote();
        }
    }

    private void castingVoteSuccess() {
        if (starView != null) {
            starView.castedVoteSuccessfully();
        }
    }

    private void voteDeleteFailed() {
        if (starView != null) {
            starView.failedToDeleteVoteFromServer();
        }
    }

    private void voteDeleteSuccess() {
        if (starView != null) {
            starView.deletedVoteSuccessfully();
        }
    }

    private void l(String msg) {
        Log.d(TAG, msg);
    }


    private Runnable steemCastingVoteExceptionRunnable = new Runnable() {
        @Override
        public void run() {
            castingVoteFailed();
        }
    };

    private Runnable steemCancellingVoteExceptionRunnable = new Runnable() {
        @Override
        public void run() {
            voteDeleteFailed();
        }
    };

    ///================================================================================
    // Ends Vote part


    private String extractTitleForArticle(List<FeedDataItemModel> data) {
        //return first h1 type text
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedDataConstants.ContentType.H1)) {
                return data.get(i).content;
            }

            //check also for h2
            if (data.get(i).type.equals(FeedDataConstants.ContentType.H2)) {
                return data.get(i).content;
            }

        }
        return "";
    }

    private String extractTextSnippetForPost(List<FeedDataItemModel> data) {

        //return first text type
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedDataConstants.ContentType.TEXT)) {
                return data.get(i).content;
            }
        }
        return "";

    }

    private String extractImageUrlForPost(List<FeedDataItemModel> data) {

        //return first image type
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedDataConstants.ContentType.IMAGE)) {
                return data.get(i).content;
            }
        }
        return "";

    }

    //=====================
    // CONTENT TYPE BINDING
    //=====================

    private void bindPostContent(List<FeedDataItemModel> data) {

        //reset the visibility
        if(featuredImagePost!=null){
            featuredImagePost.setVisibility(GONE);
        }

        //scan for feed content
        FeedRenderTypeModel feedRenderTypeModel = scanFeedContentsForRendering(data);
        //check for image
        if (feedRenderTypeModel.isFirstMediaImage) {
            //make visible and bind image
            if (featuredImagePost != null) {
                youtubeIndicator.setVisibility(GONE);
                featuredImagePost.setVisibility(VISIBLE);
                ImageHandler.load(mContext, featuredImagePost, feedRenderTypeModel.firstImageUrl);
            }
        }

        if (feedRenderTypeModel.isFirstMediaVideo) {
            //make visible and bind image
            if (featuredImagePost != null) {
                youtubeIndicator.setVisibility(VISIBLE);
                featuredImagePost.setVisibility(VISIBLE);
                ImageHandler.load(mContext, featuredImagePost, feedRenderTypeModel.firstVideoUrl);
            }

        }

        if (feedRenderTypeModel.isTitleSet) {
            //set title
            if (postTitle != null) {
                youtubeIndicator.setVisibility(GONE);
                postTitle.setVisibility(VISIBLE);
                postTitle.setText(feedRenderTypeModel.title);
            }
        }

        if (feedRenderTypeModel.hasContent) {
            //bind content
            if (postSnippet != null) {
                youtubeIndicator.setVisibility(GONE);
                postSnippet.setVisibility(VISIBLE);
                postSnippet.setText(feedRenderTypeModel.text);
            }
        }

    }

    private FeedRenderTypeModel scanFeedContentsForRendering(List<FeedDataItemModel> data) {

        FeedRenderTypeModel feedRenderTypeModel = new FeedRenderTypeModel();

        //iterate through all the content
        for (int i = 0; i < data.size(); i++) {

            //for image
            if (data.get(i).type.equals(FeedDataConstants.ContentType.IMAGE)) {

                //neither video or image is detected prior
                if (!feedRenderTypeModel.isFirstMediaImage && !feedRenderTypeModel.isFirstMediaVideo) {
                    //set media
                    feedRenderTypeModel.setFirstImageUrl(data.get(i).content);
                    feedRenderTypeModel.setFirstMediaImage(true);
                }

            }

            //for youtube
            if (data.get(i).type.equals(FeedDataConstants.ContentType.YOUTUBE)) {

                //neither video or image is detected prior
                if (!feedRenderTypeModel.isFirstMediaImage && !feedRenderTypeModel.isFirstMediaVideo) {
                    //set media
                    feedRenderTypeModel.setFirstVideoId(data.get(i).content);
                    feedRenderTypeModel.setFirstMediaVideo(true);
                }

            }

            //accumulate text
            if (data.get(i).type.equals(FeedDataConstants.ContentType.TEXT) || data.get(i).type.equals(FeedDataConstants.ContentType.H2) || data.get(i).type.equals(FeedDataConstants.ContentType.H3)) {

                feedRenderTypeModel
                        .appendText(data.get(i).getContent());

            }

            //check for heading
            if (data.get(i).type.equals(FeedDataConstants.ContentType.H1)) {

                if (!feedRenderTypeModel.isTitleSet)
                    feedRenderTypeModel.setTitle(data.get(i).getContent());

            }

        }

        Log.d("PostItemView", "scanned Model : " + feedRenderTypeModel.toString());
        return feedRenderTypeModel;

    }

    public String getAuthor() {
        return mFeed.author;
    }

    public String getPermlinkAsString() {
        return mFeed.permlink;
    }

    public String getFullPermlinkAsString() {
        return String.format("%1$s/%2$s", getAuthor(), getPermlinkAsString());
    }

    private boolean isContentEllipsised(TextView textView) {

        Layout layout = textView.getLayout();
        // Log.d("PostItemView", "layout:" + layout);
        if (layout != null) {
            int lineCount = layout.getLineCount();
            //  Log.d("PostItemView", "lines:" + lineCount);
            for (int i = 0; i < lineCount; i++) {
                //  Log.d("PostItemView", "Ellipse Count" + layout.getEllipsisCount(i) + " MaxLines:" + TextViewCompat.getMaxLines(textView));
                if (layout.getEllipsisCount(i) > 0) {
                    return true;
                }
            }
        }
        return false;

    }

    private void checkEllipseAndInvalidateReadMoreButton(final TextView target, final TextView readMoreBtn) {

        ViewTreeObserver vto = target.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isContentEllipsised(target)) {
                    readMoreBtn.setVisibility(VISIBLE);
                } else {
                    readMoreBtn.setVisibility(GONE);
                }
            }
        });
    }

    private void setSteemEarnings(String payout) {
        //double
        hapcoinsCount.setText(String.format(getResources().getString(R.string.hapcoins_format), payout.substring(0, payout.indexOf(' '))));
    }

    private void setCommentCount(int count) {

        commentCount.setText(String.format(getResources().getString(R.string.comment_format), count));
    }

    private void setCommunities(List<String> communities) {
        // community name + community color
        List<CommunityModel> cm = new ArrayList<>();
        for (int i = 0; i < communities.size(); i++) {
            if (Communities.doesCommunityExists(communities.get(i))) {
                cm.add(new CommunityModel("", "", communities.get(i),
                        HaprampPreferenceManager.getInstance().getCommunityColorFromTag(communities.get(i)),
                        HaprampPreferenceManager.getInstance().getCommunityNameFromTag(communities.get(i)),
                        0
                ));
            }
        }

        addCommunitiesToLayout(cm);

    }

    private void addCommunitiesToLayout(List<CommunityModel> cms) {

        int size = cms.size();
        resetVisibility();
        if (size > 0) {
            //first skill
            club1.setVisibility(VISIBLE);

            club1.setText(cms.get(0).getmName().toUpperCase());
            club1.getBackground().setColorFilter(
                    Color.parseColor(cms.get(0).getmColor()),
                    PorterDuff.Mode.SRC_ATOP);

            if (size > 1) {
                // second skills
                club2.setVisibility(VISIBLE);

                club2.setText(cms.get(1).getmName().toUpperCase());
                club2.getBackground().setColorFilter(
                        Color.parseColor(cms.get(1).getmColor()),
                        PorterDuff.Mode.SRC_ATOP);

                if (size > 2) {
                    // third skills
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

    private void navigateToCommentCreateActivity(int postId) {

        Intent intent = new Intent(mContext, CommentsActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_POST_ID, String.valueOf(postId));
        mContext.startActivity(intent);

    }

    private void navigateToUserProfile() {

        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, mFeed.author);
        mContext.startActivity(intent);

    }

    public void setPostData(Feed postData) {
        bind(postData);
    }

    private void showPopup(){

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
        PopupMenu popup = new PopupMenu(contextThemeWrapper,popupMenuDots);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_post, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShareUtils.shareMixedContent(mContext,mFeed);
                return true;
            }
        });

        popup.show();

    }

    @Override
    public void onReplyFetching() {

    }

    @Override
    public void onReplyFetched(List<SteemCommentModel> replies) {

        HaprampPreferenceManager.getInstance().setCommentCount(mFeed.permlink,replies.size());

        setCommentCount(replies.size());
    }

    @Override
    public void onReplyFetchError() {

    }

}

