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
import com.hapramp.activity.CommentsActivity;
import com.hapramp.activity.DetailedActivity;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.OnPostDeleteCallback;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.extraa.StarView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout{


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
    @BindView(R.id.starView)
    StarView starView;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.popupMenuDots)
    TextView popupMenuDots;
    private Context mContext;

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

    }

    private void bind(final Feed feed) {
        // set basic meta-info
        //ImageHandler.loadCircularImage(mContext, feedOwnerPic, post.user.image_uri);
        feedOwnerTitle.setText(feed.author);
        feedOwnerSubtitle.setText(
                String.format(mContext.getResources().getString(R.string.post_subtitle_format),
                        MomentsUtils.getFormattedTime(feed.created)));

        // classify the type of content
        Feed.Content content = feed.jsonMetadata.content;
        if (content.type.equals(Constants.CONTENT_TYPE_POST)) {

            for (int i = 0; i < content.data.size(); i++) {
                if (content.data.get(i).type.equals(ContentTypes.DataType.IMAGE)) {
                    ImageHandler.load(mContext, featuredImagePost, content.data.get(i).content);
                }
                if (content.data.get(i).type.equals(ContentTypes.DataType.TEXT)) {
                    postSnippet.setText(content.data.get(i).content);
                    break;
                }
            }

        } else if (feed.jsonMetadata.content.type.equals(Constants.CONTENT_TYPE_ARTICLE)) {
            for (int i = 0; i < content.data.size(); i++) {
                if (content.data.get(i).type.equals(ContentTypes.DataType.TEXT)) {
                    postSnippet.setText(content.data.get(i).content);
                    break;
                }
            }
        }
        //   if (post.post_type == Constants.CONTENT_TYPE_ARTICLE) {

        // set Title
        //     postTitle.setVisibility(View.VISIBLE);
        //todo post title required
        //postTitle.setText(post);

        //   readMoreBtn.setVisibility(View.VISIBLE);

//            readMoreBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    navigateToDetailedPage(post);
//                }
//            });


//        } else {
//            //(post.post_type==Constants.CONTENT_TYPE_POST)
//            // turn off post title
//            postTitle.setVisibility(GONE);
//            readMoreBtn.setVisibility(GONE);
//
//        }

        // check for image
//        if(post.media_uri!=null) {
//
//            if (post.media_uri.length() == 0) {
//                featuredImagePost.setVisibility(GONE);
//            } else {
//
//                featuredImagePost.layout(0, 0, 0, 0);
//                ImageHandler.load(mContext, featuredImagePost, post.media_uri);
//                featuredImagePost.setVisibility(View.VISIBLE);
//
//            }
//        }else{
//            featuredImagePost.setVisibility(GONE);
//        }
//
//
//        setHapcoins(post.hapcoins);
//        setCommentCount(post.comment_count);
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
//        // initialize the starview
//        starView.setVoteState(
//                new StarView.Vote(
//                        post.is_voted,
//                        post.id,
//                        post.current_vote,
//                        post.vote_count,
//                        post.vote_sum))
//                .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
//                    @Override
//                    public void onVoted(int postId, int _vote) {
//                        performVote(postId, _vote);
//                    }
//
//                    @Override
//                    public void onVoteDeleted(int postId) {
//                        deleteVote(postId);
//                    }
//                });
//
//        setSkills(post.skills);
//
//        feedOwnerPic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToUserProfile(post.user.id);
//            }
//        });
//
//        starView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                starView.onStarIndicatorTapped();
//            }
//        });

    }

    private void setHapcoins(float hapcoins) {
        hapcoinsCount.setText(String.format(getResources().getString(R.string.hapcoins_format), hapcoins));
    }

    private void setCommentCount(int count) {
        commentCount.setText(String.format(getResources().getString(R.string.comment_format), count));
    }

    private void setSkills(List<PostResponse.Skills> skills) {

        int size = skills.size();
        resetVisibility();
        if (size > 0) {
            //first skill
            club1.setVisibility(VISIBLE);
            club1.setText(SkillsUtils.getSkillTitleFromId(skills.get(0).id));
            club1.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(0).id), PorterDuff.Mode.SRC_ATOP);
            if (size > 1) {
                // second skills
                club2.setVisibility(VISIBLE);
                club2.setText(SkillsUtils.getSkillTitleFromId(skills.get(1).id));
                club2.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(1).id), PorterDuff.Mode.SRC_ATOP);
                if (size > 2) {
                    // third skills
                    club3.setVisibility(VISIBLE);
                    club3.setText(SkillsUtils.getSkillTitleFromId(skills.get(2).id));
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

}

