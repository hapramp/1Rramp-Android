package com.hapramp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.utils.FontManager;
import com.hapramp.R;

import butterknife.ButterKnife;
import butterknife.BindView;

public class StoryActivity extends AppCompatActivity {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.popupMenu)
    TextView popupMenu;
    @BindView(R.id.topBar)
    RelativeLayout topBar;
    @BindView(R.id.author_pic)
    SimpleDraweeView authorPic;
    @BindView(R.id.author_name)
    TextView authorName;
    @BindView(R.id.auther_username_with_ago)
    TextView autherUsernameWithAgo;
    @BindView(R.id.story_featured_pic)
    SimpleDraweeView storyFeaturedPic;
    @BindView(R.id.likeBtn)
    TextView likeBtn;
    @BindView(R.id.likeCount)
    TextView likeCount;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.shareBtn)
    TextView shareBtn;
    @BindView(R.id.shareCount)
    TextView shareCount;
    @BindView(R.id.story_content)
    TextView storyContent;
    @BindView(R.id.tags)
    TextView tags;

    Typeface materialTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);
        setTypeFaces();
        bindData();
    }

    private void setTypeFaces(){
        materialTypeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(materialTypeface);
        popupMenu.setTypeface(materialTypeface);
        likeBtn.setTypeface(materialTypeface);
        commentBtn.setTypeface(materialTypeface);
        shareBtn.setTypeface(materialTypeface);
    }

    private void bindData(){

        String featuredImage = "http://media0-starag.startv.in/r1/thumbs/PCTV/81/81/PCTV-81-hd.jpg";
        String userImage = "http://dekhnews.com/wp-content/uploads/2016/08/Diya-Aur-Baati-Hum-Episode-Written-Updates-2.jpg";
        authorPic.setImageURI(userImage);
        storyFeaturedPic.setImageURI(featuredImage);

    }
}
