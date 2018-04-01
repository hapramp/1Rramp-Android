package com.hapramp.views.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.text.Html;
import android.text.Layout;
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
import com.hapramp.models.CommunityModel;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.extraa.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout {


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

            // hide title
            postTitle.setVisibility(GONE);
            // hide read more
            readMoreBtn.setVisibility(GONE);

            // render the content
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

}

