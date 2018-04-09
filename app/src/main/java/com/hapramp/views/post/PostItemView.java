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
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.VoteModel;
import com.hapramp.models.VoteStatus;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.SteemHelper;
import com.hapramp.steem.models.Feed;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout {


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

    }

    private void navigateToDetailsPage() {
        Intent detailsIntent = new Intent(mContext, DetailedActivity.class);
        detailsIntent.putExtra(Constants.EXTRAA_KEY_POST_DATA,mFeed);
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
        Feed.Content content = feed.jsonMetadata.content;
        if (content.type.equals(Constants.CONTENT_TYPE_POST)) {

            // hide title
            postTitle.setVisibility(GONE);
            // hide read more
            readMoreBtn.setVisibility(GONE);

            // render the content
            for (int i = 0; i < content.data.size(); i++) {
                if (content.data.get(i).type.equals(ContentTypes.DataType.IMAGE)) {
                    featuredImagePost.layout(0,0,0,0);
                    ImageHandler.load(mContext, featuredImagePost, content.data.get(i).content);
                }
                if (content.data.get(i).type.equals(ContentTypes.DataType.TEXT)) {
                    postSnippet.setText(content.data.get(i).content);
                    break;
                }
            }

        } else if (feed.jsonMetadata.content.type.equals(Constants.CONTENT_TYPE_ARTICLE)) {
            // show title
            postTitle.setVisibility(VISIBLE);
            postTitle.setText(feed.getTitle());
            // show read more if content length is more
            for (int i = 0; i < content.data.size(); i++) {
                if (content.data.get(i).type.equals(ContentTypes.DataType.TEXT)) {
                    postSnippet.setText(content.data.get(i).content);
                    break;
                }
            }

            if (isContentEllipsised(postSnippet)) {
                // show read more button
                readMoreBtn.setVisibility(VISIBLE);
            } else {
                //hide read more button
                readMoreBtn.setVisibility(GONE);
            }

        }

        setHapcoins(feed.totalPayoutValue);
        setCommunities(feed.jsonMetadata.tags);
        Log.d("PostItemView", "requesting image for " + feed.author);
        Profile _p = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserProfile(feed.getAuthor()), Profile.class);
        Log.d("PostItemView", "Got: " + HaprampPreferenceManager.getInstance().getUserProfile(feed.getAuthor()));
        if (_p != null) {
            Log.d("PostItemView", "loading image from " + _p.getProfileImage());
            ImageHandler.loadCircularImage(mContext, feedOwnerPic, _p.getProfileImage());
        }

        fetchVotes(String.format("%1$s/%2$s", feed.getAuthor(), feed.getPermlink()));

//
//        commentBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                navigateToCommentCreateActivity(post.id);
//
//            }
//        });
//
//        commentCount.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToCommentCreateActivity(post.id);
//            }
//        });
//
//        postSnippet.setText(Html.fromHtml(post.content));
//
//
//        setSkills(post.skills);
//
//        postCreatorPic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToUserProfile(post.user.id);
//            }
//        });
//
        starView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isConnected(mContext)) {
                    starView.onStarIndicatorTapped();
                }
            }
        });

    }

    public String getAuthor() {
        return mFeed.getAuthor();
    }

    public String getPermlinkAsString() {
        return mFeed.getPermlink();
    }

    public String getFullPermlinkAsString(){
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

    private void setHapcoins(String payout) {
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

    private void fetchVotes(final String full_permlink) {

        starView.onVoteLoading();

        RetrofitServiceGenerator.getService().getPostVotes(full_permlink).enqueue(new Callback<List<VoteModel>>() {
            @Override
            public void onResponse(Call<List<VoteModel>> call, Response<List<VoteModel>> response) {
                if (response.isSuccessful()) {
                    bindVotes(response.body(), full_permlink);
                    starView.onVoteLoaded();
                } else {
                    starView.onVoteLoadingFailed();
                }
            }

            @Override
            public void onFailure(Call<List<VoteModel>> call, Throwable t) {
                starView.onVoteLoadingFailed();
            }
        });

    }

    private float getVoteSum(List<VoteModel> votes) {
        float sum = 0;
        for (int i = 0; i < votes.size(); i++) {
            sum += votes.get(i).vote;
        }
        return sum;
    }

    private VoteModel getVoteForCurrentUser(List<VoteModel> votes) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).getUsernam().equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
                return votes.get(i);
            }
        }
        return null;
    }

    private void bindVotes(List<VoteModel> votes, String permlink) {

        // initialize the starview
        VoteModel myVote = getVoteForCurrentUser(votes);
        boolean isVoted = (myVote != null);
        float voteSum = getVoteSum(votes);

        starView.setVoteState(
                new StarView.Vote(
                        isVoted,
                        permlink,
                        isVoted ? myVote.vote : 0,
                        votes.size(),
                        voteSum))
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


    /*
    *  author of the vote: author of the pose
    *  votePower: 0 for 1-2 ratte
    *  votePower: 100 for 3-5 rate
    * */
    private void performVoteOnSteem(final int vote) {
        starView.voteProcessing();

        final int votePower = vote < 3 ? 1 : 100;

        new Thread() {

            @Override
            public void run() {
                try {

                    AccountName voteFor = new AccountName(getAuthor());
                    AccountName voter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                    SteemJ steemJ = SteemHelper.getSteemInstance();

                    steemJ.vote(voter, voteFor, new Permlink(getPermlinkAsString()), (short) votePower);
                    l("Voted on Steem!");
                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            l("Sending Vote to App server");
                            performVoteOnAppServer(getFullPermlinkAsString(),vote);
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
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
                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            l("Deleting vote on app server");
                            deleteVoteOnAppServer(getFullPermlinkAsString());
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                }

            }
        }.start();

    }

    private void performVoteOnAppServer(String full_permlink, int vote) {

        RetrofitServiceGenerator.getService().castVote(full_permlink, new VoteRequestBody(vote)).enqueue(new Callback<VoteStatus>() {
            @Override
            public void onResponse(Call<VoteStatus> call, Response<VoteStatus> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("ok")) {
                        castingVoteSuccess();
                    }
                } else {
                    castingVoteFailed();
                }
            }

            @Override
            public void onFailure(Call<VoteStatus> call, Throwable t) {
                castingVoteFailed();
            }
        });
    }

    private void deleteVoteOnAppServer(String full_permlink) {
        RetrofitServiceGenerator.getService().deleteVote(full_permlink).enqueue(new Callback<VoteStatus>() {
            @Override
            public void onResponse(Call<VoteStatus> call, Response<VoteStatus> response) {
                if (response.isSuccessful()) {
                    voteDeleteSuccess();
                } else {
                    voteDeleteFailed();
                }
            }

            @Override
            public void onFailure(Call<VoteStatus> call, Throwable t) {

            }
        });

    }

    private void castingVoteFailed() {
        starView.failedToCastVote();
        Toast.makeText(mContext, "FAILED : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void castingVoteSuccess() {
        starView.castedVoteSuccessfully();
        Toast.makeText(mContext, "SUCCESS : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteFailed() {
        starView.failedToDeleteVoteFromServer();
        Toast.makeText(mContext, "FAILED : Vote Delete", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteSuccess() {
        starView.deletedVoteSuccessfully();
        Toast.makeText(mContext, "SUCCESS : Vote Deleted", Toast.LENGTH_LONG).show();
    }

    private void l(String msg){
        Log.d(TAG,msg);
    }

}

