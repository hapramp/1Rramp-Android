package com.hapramp.views.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.activity.CommentsActivity;
import com.hapramp.activity.DetailedActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ContentCommentModel;
import com.hapramp.steem.FeedData;
import com.hapramp.steem.SteemHelper;
import com.hapramp.steem.SteemReplyFetcher;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.data.ActiveVote;
import com.hapramp.steem.models.data.Content;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
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
    @BindView(R.id.reference_line)
    Space referenceLine;
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
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
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
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.popupMenuDots)
    TextView popupMenuDots;
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

        // classify the type of content
        Content content = feed.jsonMetadata.content;

        //load featured image and snippet for article | post
        String image_url = extractImageUrlForPost(content.data);
        String text = extractTextSnippetForPost(content.data);

        postSnippet.setText(text);
        ImageHandler.load(mContext, featuredImagePost, image_url);

        if (content.type.equals(Constants.CONTENT_TYPE_POST)) {
            // hide title
            postTitle.setVisibility(GONE);
            // hide read more
            readMoreBtn.setVisibility(GONE);

        } else if (content.type.equals(Constants.CONTENT_TYPE_ARTICLE)) {
            // show title
            String title = extractTitleForArticle(content.data);
            postTitle.setVisibility(VISIBLE);
            postTitle.setText(title);

            if (isContentEllipsised(postSnippet)) {
                // show read more button
                readMoreBtn.setVisibility(VISIBLE);
            } else {
                //hide read more button
                readMoreBtn.setVisibility(GONE);
            }

        }

        setSteemEarnings(feed.totalPayoutValue);
        setCommunities(feed.jsonMetadata.tags);
        Log.d("PostItemView", "requesting image for " + feed.author);
        Profile _p = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserProfile(feed.author), Profile.class);
        Log.d("PostItemView", "Got: " + HaprampPreferenceManager.getInstance().getUserProfile(feed.author));
        if (_p != null) {
            Log.d("PostItemView", "loading image from " + _p.getProfileImage());
            ImageHandler.loadCircularImage(mContext, feedOwnerPic, _p.getProfileImage());
        }

        bindVotes(feed.activeVotes, feed.permlink);
        replyFetcher.requestReplyForPost(feed.author,feed.permlink);

        starView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isConnected(mContext)) {
                    starView.onStarIndicatorTapped();
                }else{
                    Toast.makeText(mContext,"Check Network Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        starView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ConnectionUtils.isConnected(mContext)) {
                    starView.onStarIndicatorLongPressed();
                }else{
                    Toast.makeText(mContext,"Check Network Connection",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });



    }

    private long getVotePercentSum(List<ActiveVote> activeVotes) {
        long sum = 0;
        for (int i = 0; i < activeVotes.size(); i++) {
            sum += activeVotes.get(i).percent;
        }
        return sum;
    }

    private String extractTitleForArticle(List<FeedDataItemModel> data) {
        //return first h1 type text
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedData.ContentType.H1)) {
                return data.get(i).content;
            }

            //check also for h2
            if (data.get(i).type.equals(FeedData.ContentType.H2)) {
                return data.get(i).content;
            }

        }
        return "";
    }

    private String extractTextSnippetForPost(List<FeedDataItemModel> data) {

        //return first text type
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedData.ContentType.TEXT)) {
                return data.get(i).content;
            }
        }
        return "";

    }

    private String extractImageUrlForPost(List<FeedDataItemModel> data) {

        //return first image type
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type.equals(FeedData.ContentType.IMAGE)) {
                return data.get(i).content;
            }
        }
        return "";

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
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                int ellipsisCount = layout.getEllipsisCount(lines - 1);
                if (ellipsisCount > 0) {
                    return true;
                }
            }
        }
        return false;

    }

    private void setSteemEarnings(String payout) {
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

            club1.setText(cms.get(0).getmName());
            club1.getBackground().setColorFilter(
                    Color.parseColor(cms.get(0).getmColor()),
                    PorterDuff.Mode.SRC_ATOP);

            if (size > 1) {
                // second skills
                club2.setVisibility(VISIBLE);

                club2.setText(cms.get(1).getmName());
                club2.getBackground().setColorFilter(
                        Color.parseColor(cms.get(1).getmColor()),
                        PorterDuff.Mode.SRC_ATOP);

                if (size > 2) {
                    // third skills
                    club3.setVisibility(VISIBLE);

                    club3.setText(cms.get(2).getmName());
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

    private void navigateToUserProfile(int userId) {
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_USER_ID, String.valueOf(userId));
        mContext.startActivity(intent);
    }

    public void setPostData(Feed postData) {
        bind(postData);
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
                    l("Voted on Steem!");
                    Log.d("VoteTest", "voted " + votePower);
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
        Toast.makeText(mContext, "FAILED : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void castingVoteSuccess() {
        if (starView != null) {
            starView.castedVoteSuccessfully();
        }
        Toast.makeText(mContext, "SUCCESS : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteFailed() {
        if (starView != null) {
            starView.failedToDeleteVoteFromServer();
        }
        Toast.makeText(mContext, "FAILED : Vote Delete", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteSuccess() {
        if (starView != null) {
            starView.deletedVoteSuccessfully();
        }
        Toast.makeText(mContext, "SUCCESS : Vote Deleted", Toast.LENGTH_LONG).show();
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


    @Override
    public void onReplyFetching() {

    }

    @Override
    public void onReplyFetched(List<ContentCommentModel> replies) {
        setCommentCount(replies.size());
    }

    @Override
    public void onReplyFetchError() {

    }
}

