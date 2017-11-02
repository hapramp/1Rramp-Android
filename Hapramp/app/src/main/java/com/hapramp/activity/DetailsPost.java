package com.hapramp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.FontManager;
import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsPost extends AppCompatActivity {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.feed_owner_pic)
    SimpleDraweeView feedOwnerPic;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
    @BindView(R.id.featured_image_post)
    SimpleDraweeView featuredImagePost;
    @BindView(R.id.post_title)
    TextView postTitle;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.post_snippet)
    TextView postSnippet;
    @BindView(R.id.shareWithFriendBtn)
    TextView shareWithFriendBtn;
    @BindView(R.id.startIcon)
    TextView startIcon;
    @BindView(R.id.likeCount)
    TextView likeCount;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.hapcoinBtn)
    TextView hapcoinBtn;
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.commentsCaption)
    TextView commentsCaption;
    @BindView(R.id.writeCommentUserAvatar)
    SimpleDraweeView writeCommentUserAvatar;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.writeCommentComment)
    RelativeLayout writeCommentComment;
    @BindView(R.id.commentsListView)
    ListView commentsListView;
    @BindView(R.id.starBtn)
    TextView starBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_post);
        ButterKnife.bind(this);
        setTypefaces();
        attachListener();
    }

    private void attachListener() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTypefaces(){
        Typeface t = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(t);
        overflowBtn.setTypeface(t);
        startIcon.setTypeface(t);
        starBtn.setTypeface(t);
        commentBtn.setTypeface(t);
        hapcoinBtn.setTypeface(t);
    }


}
