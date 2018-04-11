package com.hapramp.views.types;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.hapramp.R;

/**
 * Created by Ankit on 4/9/2018.
 */

public class YoutubeVideoTypeView extends FrameLayout implements YouTubePlayer.OnInitializedListener {

    private Context mContext;
    private YouTubePlayerFragment playerFragment;
    private YouTubePlayer mPlayer;
    private String YouTubeKey = "AIzaSyBVUoUB41eL2GS_ERrG5bAfrjr1bukCu2g";

    private String key = "";

    public YoutubeVideoTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public YoutubeVideoTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YoutubeVideoTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.youtube_view_layout, this);

        playerFragment = (YouTubePlayerFragment) ((AppCompatActivity) mContext).getFragmentManager().findFragmentById(R.id.youtube_player_fragment);

        playerFragment.initialize(YouTubeKey, this);
    }

    public void setVideoKey(String key) {
        this.key = key;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        mPlayer = player;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
       // mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        if (!wasRestored) {
            //player.cueVideo(key);
            mPlayer.loadVideo(key);
        } else {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        mPlayer = null;
    }

}
