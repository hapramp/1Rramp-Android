package com.hapramp.views.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.activity.CommentEditorActivity;
import com.hapramp.activity.DetailedActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.extraa.StarView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout implements VoteDeleteCallback, VotePostCallback {


    @BindView(R.id.feed_owner_pic)
    ImageView feedOwnerPic;
    @BindView(R.id.reference_line)
    Space referenceLine;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
    @BindView(R.id.featured_image_post)
    ImageView featuredImagePost;
    @BindView(R.id.post_title)
    TextView postTitle;
    @BindView(R.id.tags)
    TextView tags;
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
    @BindView(R.id.post_overflow_icon)
    TextView postOverflowIcon;
    @BindView(R.id.starView)
    StarView starView;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.club1)
    TextView club1;
    @BindView(R.id.club2)
    TextView club2;
    @BindView(R.id.club3)
    TextView club3;
    private Context mContext;
    private PostResponse.Results postData;

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
    }

    private void bind(final PostResponse.Results post) {
        // set basic meta-info
        ImageHandler.loadCircularImage(mContext, feedOwnerPic, post.user.image_uri);
        feedOwnerTitle.setText(post.user.full_name);
        feedOwnerSubtitle.setText(post.user.username);

        // classify the type of content
        if (post.post_type == Constants.CONTENT_TYPE_ARTICLE) {

            // set Title
            postTitle.setVisibility(View.VISIBLE);
            //todo post title required
            //postTitle.setText(post);

            readMoreBtn.setVisibility(View.VISIBLE);

            readMoreBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToDetailedPage(post);
                }
            });


        } else {
            //(post.post_type==Constants.CONTENT_TYPE_POST)
            // turn off post title
            postTitle.setVisibility(GONE);
            readMoreBtn.setVisibility(GONE);

        }

        // check for image
        if (post.media_uri.length() == 0) {
            featuredImagePost.setVisibility(GONE);
        } else {
            ImageHandler.load(mContext, featuredImagePost, post.media_uri);
            featuredImagePost.setVisibility(View.VISIBLE);
        }

        hapcoinsCount.setText(String.format(Locale.US, "%1.3f", post.hapcoins));
        String _comment_info = post.comment_count > 1 ? String.valueOf(post.comment_count).concat(" comments") : String.valueOf(post.comment_count).concat(" comment");
        commentCount.setText(_comment_info);

        commentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToCommentCreateActivity(post.content, post.id, post.user.username, post.media_uri);

            }
        });

        postSnippet.setText(Html.fromHtml(post.content));

        // initialize the starview
        starView.setVoteState(
                new StarView.Vote(
                        post.is_voted,
                        post.id,
                        post.current_vote,
                        post.vote_count,
                        post.vote_sum))
                .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
                    @Override
                    public void onVoted(int postId, int _vote) {
                        performVote(postId, _vote);
                    }

                    @Override
                    public void onVoteDeleted(int postId) {
                        deleteVote(postId);
                    }
                });

        setSkills(post.skills);

        postHeaderContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUserProfile(post.user.id);
            }
        });

        starView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                starView.onStarIndicatorTapped();
            }
        });

    }

    private void setSkills(List<PostResponse.Skills> skills) {

        int size = skills.size();
        resetVisibility();
        if(size>0){
            //first skill
            club1.setVisibility(VISIBLE);
            club1.setText(SkillsUtils.getSkillCharacter(skills.get(0).id));
            club1.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(0).id), PorterDuff.Mode.SRC_ATOP);
            if(size>1){
                // second skills
                club2.setVisibility(VISIBLE);
                club2.setText(SkillsUtils.getSkillCharacter(skills.get(1).id));
                club2.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(1).id), PorterDuff.Mode.SRC_ATOP);
                if(size>2){
                    // third skills
                    club3.setVisibility(VISIBLE);
                    club3.setText(SkillsUtils.getSkillCharacter(skills.get(2).id));
                    club3.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(2).id), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

    }

    private void resetVisibility() {

        club1.setVisibility(GONE);
        club2.setVisibility(GONE);
        club3.setVisibility(GONE);

    }

    private void navigateToDetailedPage(PostResponse.Results post) {

        Intent intent = new Intent(mContext, DetailedActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_IS_VOTED, post.is_voted);
        intent.putExtra(Constants.EXTRAA_KEY_VOTE, post.current_vote);
        intent.putExtra(Constants.EXTRAA_KEY_USERNAME, post.user.username);
        intent.putExtra(Constants.EXTRAA_KEY_MEDIA_URL, post.media_uri);
        intent.putExtra(Constants.EXTRAA_KEY_CONTENT, post.content);
        intent.putExtra(Constants.EXTRAA_KEY_POST_ID, String.valueOf(post.id));
        intent.putExtra(Constants.EXTRAA_KEY_USER_DP_URL, post.user.image_uri);
        intent.putExtra(Constants.EXTRAA_KEY_TOTAL_VOTE_SUM, String.valueOf(post.vote_sum));
        intent.putExtra(Constants.EXTRAA_KEY_TOTAL_USER_VOTED, String.valueOf(post.vote_count));
        intent.putExtra(Constants.EXTRAA_KEY_HAPCOINS, String.valueOf(post.hapcoins));

        mContext.startActivity(intent);

    }

    private void navigateToCommentCreateActivity(String contextText, int postId, String author, String mediaUri) {

        Intent i = new Intent(mContext, CommentEditorActivity.class);
        i.putExtra(Constants.EXTRAA_KEY_CONTEXT_TEXT, contextText);
        i.putExtra(Constants.EXTRAA_KEY_POST_ID, String.valueOf(postId));
        i.putExtra(Constants.EXTRAA_KEY_AUTHOR, author);
        i.putExtra(Constants.EXTRAA_KEY_MEDIA_URL, mediaUri);

        mContext.startActivity(i);

    }

    private void deleteVote(int postId) {
        DataServer.deleteVote(postId, this);
    }

    private void performVote(int postId, int vote) {
        DataServer.votePost(String.valueOf(postId), new VoteRequestBody(vote), this);
    }

    private void navigateToUserProfile(int userId) {

        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_USER_ID, String.valueOf(userId));
        mContext.startActivity(intent);

    }

    public void setPostData(PostResponse.Results postData) {
        this.postData = postData;
        bind(postData);
    }

    @Override
    public void onVoteDeleted(PostResponse.Results updatedPost) {
        // update the hapcoins
        hapcoinsCount.setText(String.format(Locale.US, "%1.3f", updatedPost.hapcoins));
    }

    @Override
    public void onVoteDeleteError() {
        // do nothing for now :)
    }

    @Override
    public void onPostVoted(PostResponse.Results updatedPost) {
        // update the hapcoins
        hapcoinsCount.setText(String.format(Locale.US, "%1.3f", updatedPost.hapcoins));
    }

    @Override
    public void onPostVoteError() {
        // do nothing for now :)
    }
}

