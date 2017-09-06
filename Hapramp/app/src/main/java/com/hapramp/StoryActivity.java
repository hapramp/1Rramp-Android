package com.hapramp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryActivity extends AppCompatActivity {

    @InjectView(R.id.closeBtn)
    TextView closeBtn;
    @InjectView(R.id.popupMenu)
    TextView popupMenu;
    @InjectView(R.id.topBar)
    RelativeLayout topBar;
    @InjectView(R.id.author_pic)
    SimpleDraweeView authorPic;
    @InjectView(R.id.author_name)
    TextView authorName;
    @InjectView(R.id.auther_username_with_ago)
    TextView autherUsernameWithAgo;
    @InjectView(R.id.story_featured_pic)
    SimpleDraweeView storyFeaturedPic;
    @InjectView(R.id.likeBtn)
    TextView likeBtn;
    @InjectView(R.id.likeCount)
    TextView likeCount;
    @InjectView(R.id.commentBtn)
    TextView commentBtn;
    @InjectView(R.id.commentCount)
    TextView commentCount;
    @InjectView(R.id.shareBtn)
    TextView shareBtn;
    @InjectView(R.id.shareCount)
    TextView shareCount;
    @InjectView(R.id.story_content)
    TextView storyContent;
    @InjectView(R.id.tags)
    TextView tags;

    Typeface materialTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.inject(this);
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
