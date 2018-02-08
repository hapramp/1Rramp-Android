package com.hapramp.activity;

import android.os.Bundle;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.views.comments.CommentView;
import com.hapramp.views.extraa.StarView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends AppCompatActivity {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
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
    @BindView(R.id.featureImage)
    ImageView featureImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.shareBtn)
    TextView shareBtn;
    @BindView(R.id.commentsViewContainer)
    LinearLayout commentsViewContainer;
    @BindView(R.id.commentCreaterAvatarMock)
    ImageView commentCreaterAvatarMock;
    @BindView(R.id.commentInputBoxMock)
    EditText commentInputBoxMock;
    @BindView(R.id.sendButtonMock)
    TextView sendButtonMock;
    @BindView(R.id.scroller)
    ScrollView scroller;
    @BindView(R.id.shadow)
    ImageView shadow;
    @BindView(R.id.commentCreaterAvatar)
    ImageView commentCreaterAvatar;
    @BindView(R.id.commentInputBox)
    EditText commentInputBox;
    @BindView(R.id.sendButton)
    TextView sendButton;
    @BindView(R.id.commentInputContainer)
    RelativeLayout commentInputContainer;
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
    @BindView(R.id.postMetaContainer)
    RelativeLayout postMetaContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);
        sendButton.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        sendButtonMock.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        addComment();

    }

    private void attachListeners(){

       commentInputBoxMock.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               commentInputContainer.setVisibility(View.VISIBLE);
           }
       });

    }

    private void addComment(){

        for(int i=0;i<3;i++){
            CommentView view = new CommentView(this);
            commentsViewContainer.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }
}