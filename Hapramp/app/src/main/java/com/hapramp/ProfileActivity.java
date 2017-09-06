package com.hapramp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends AppCompatActivity {

    @InjectView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.hapname)
    TextView hapname;
    @InjectView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @InjectView(R.id.follow_btn)
    TextView followBtn;
    @InjectView(R.id.post_counts)
    TextView postCounts;
    @InjectView(R.id.followers_count)
    TextView followersCount;
    @InjectView(R.id.followings_count)
    TextView followingsCount;
    @InjectView(R.id.post_stats)
    RelativeLayout postStats;
    @InjectView(R.id.divider)
    FrameLayout divider;
    @InjectView(R.id.badge_containers)
    LinearLayout badgeContainers;
    @InjectView(R.id.recentPostsCaption)
    TextView recentPostsCaption;
    @InjectView(R.id.profile_pic)
    SimpleDraweeView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.inject(this);
        profilePic.setImageURI("https://1.bp.blogspot.com/-h1S4DMAES4c/WO95GGRlOwI/AAAAAAAAIxg/A1lqYwPgS34sX1Q9mr3ZugAIGVrnqJkcwCLcB/s1600/sanchita-banerjee-wiki-biography-filmography-mt-wiki.jpg");

    }
}
