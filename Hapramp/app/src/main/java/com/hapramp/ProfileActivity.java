package com.hapramp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.BindView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @BindView(R.id.follow_btn)
    TextView followBtn;
    @BindView(R.id.post_counts)
    TextView postCounts;
    @BindView(R.id.followers_count)
    TextView followersCount;
    @BindView(R.id.followings_count)
    TextView followingsCount;
    @BindView(R.id.post_stats)
    RelativeLayout postStats;
    @BindView(R.id.divider)
    FrameLayout divider;
    @BindView(R.id.badge_containers)
    LinearLayout badgeContainers;
    @BindView(R.id.recentPostsCaption)
    TextView recentPostsCaption;
    @BindView(R.id.profile_pic)
    SimpleDraweeView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        profilePic.setImageURI("https://1.bp.blogspot.com/-h1S4DMAES4c/WO95GGRlOwI/AAAAAAAAIxg/A1lqYwPgS34sX1Q9mr3ZugAIGVrnqJkcwCLcB/s1600/sanchita-banerjee-wiki-biography-filmography-mt-wiki.jpg");

    }
}
