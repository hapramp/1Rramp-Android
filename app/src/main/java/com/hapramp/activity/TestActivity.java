package com.hapramp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.hapramp.R;

import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity{


    private YouTubePlayerFragment playerFragment1;
    private YouTubePlayerFragment playerFragment2;


    private YouTubePlayer mPlayer;
    private String YouTubeKey = "AIzaSyBVUoUB41eL2GS_ERrG5bAfrjr1bukCu2g";
    private java.lang.String key = "7asVllRWL74";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(TestActivity.this,TestActivity.class);
                startActivity(i);
                finish();
            }
        },40000);

    }

    private void init() {

        playerFragment1 = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment1);
        playerFragment2 = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_player_fragment2);

        playerFragment1.initialize(YouTubeKey,listener1);
        playerFragment2.initialize(YouTubeKey,listener2);

    }

    YouTubePlayer.OnInitializedListener listener1 = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(key);
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }
    };
    YouTubePlayer.OnInitializedListener listener2 = new YouTubePlayer.OnInitializedListener() {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
            youTubePlayer.loadVideo(key);
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }
    };

}
