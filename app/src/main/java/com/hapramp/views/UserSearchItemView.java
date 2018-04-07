package com.hapramp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.activity.ProfileActivity;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.FollowApiObjectWrapper;
import com.hapramp.steem.SteemHelper;
import com.hapramp.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 4/6/2018.
 */

public class UserSearchItemView extends FrameLayout {

    private boolean followed = false;

    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.followUnfollowBtn)
    TextView followUnfollowBtn;
    @BindView(R.id.followUnfollowProgress)
    ProgressBar followUnfollowProgress;
    private Context mContext;

    private Handler mHandler;
    private String mUsername;

    public UserSearchItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UserSearchItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserSearchItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.user_suggestions_item_row, this);
        ButterKnife.bind(this, view);
        mHandler = new Handler();
        attachListeners();

    }

    private void attachListeners() {

        followUnfollowBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowed()) {
                    confirmUnfollowAction();
                } else {
                    requestFollowOnSteem();
                }
            }
        });

        content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProfileActivity.class);
                i.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME,getUsername());
                mContext.startActivity(i);
            }
        });

    }

    private void requestFollowOnSteem() {

        showProgress(true);

        new Thread() {
            @Override
            public void run() {
                SteemJ steemJ = SteemHelper.getSteemInstance();
                try {
                    steemJ.follow(new AccountName(getUsername()));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            userFollowedOnSteem();
                        }
                    });
                } catch (SteemCommunicationException e) {
                    userFollowFailed();
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    userFollowFailed();
                    e.printStackTrace();
                } catch (SteemInvalidTransactionException e) {
                    userFollowFailed();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void requestUnFollowOnSteem() {

        showProgress(true);

        new Thread() {
            @Override
            public void run() {
                SteemJ steemJ = SteemHelper.getSteemInstance();
                try {
                    steemJ.unfollow(new AccountName(getUsername()));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            userUnFollowedOnSteem();
                        }
                    });
                } catch (SteemCommunicationException e) {
                    userUnfollowFailed();
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    userUnfollowFailed();
                    e.printStackTrace();
                } catch (SteemInvalidTransactionException e) {
                    userUnfollowFailed();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //set username
    public void setUsername(String username) {

        this.mUsername = username;
        // set text
        content.setText(username);

        setFollowButtonState();

    }

    private void setFollowButtonState() {

        // hide follow button for self account
        if(mUsername.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())){
            followUnfollowBtn.setVisibility(GONE);
            return;
        }

        FollowApiObjectWrapper followApiObjectWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserFollowingsAsJson(), FollowApiObjectWrapper.class);
        List<FollowApiObject> followApiObjects = followApiObjectWrapper.getFollowings();

        for (int i = 0; i < followApiObjects.size(); i++) {
            String _followedUser = followApiObjects.get(i).getFollowing().getName();
            Log.d("UserSearchItem","matching "+getUsername()+" vs "+_followedUser);

            if (_followedUser.equals(getUsername())) {
                setFollowedState();
                return;
            }

        }

        setUnFollowedState();

    }

    private void setFollowedState() {
        //user is followed, so now Unfollow
        followUnfollowBtn.setText("UnFollow");
        followUnfollowBtn.setBackgroundResource(R.drawable.unfollow_btn_bg);
        followed = true;

    }

    private void setUnFollowedState() {
        //user is followed, so now Unfollow
        followUnfollowBtn.setText("Follow");
        followUnfollowBtn.setBackgroundResource(R.drawable.follow_btn_bg);
        followed = false;
    }


    private void confirmUnfollowAction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("Unfollow")
                .setMessage("Do you want to Unfollow "+getUsername()+" ?")
                .setPositiveButton("UnFollow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       requestUnFollowOnSteem();
                    }
                })
                .setNegativeButton("No", null);

        builder.show();

    }


    private void userFollowedOnSteem() {
        showProgress(false);
        setFollowedState();
        fetchFollowingsAndCache();
        t("You started following "+getUsername());
    }

    private void userFollowFailed() {
        showProgress(false);
        setUnFollowedState();
        t("Failed to follow "+getUsername());
    }

    private void userUnFollowedOnSteem() {
        showProgress(false);
        setUnFollowedState();
        fetchFollowingsAndCache();
        t("You unfollowed "+getUsername());
    }

    private void userUnfollowFailed() {
        showProgress(false);
        setFollowedState();
        t("Failed to unfollow "+getUsername());
    }


    private void showProgress(boolean show){

        if(show){
            //hide button
            followUnfollowBtn.setVisibility(GONE);
            followUnfollowProgress.setVisibility(VISIBLE);
        }else{
            //show button
            followUnfollowBtn.setVisibility(VISIBLE);
            followUnfollowProgress.setVisibility(GONE);
        }

    }

    private void fetchFollowingsAndCache() {

        final String follower = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        final String startFollower = "";
        final FollowType followType = FollowType.BLOG;
        final short limit = 1000;

        new Thread() {
            @Override
            public void run() {
                try {

                    SteemJ steemJ = new SteemJ();
                    List<FollowApiObject> followApiObjects = steemJ.getFollowing(new AccountName(follower), new AccountName(startFollower), followType, limit);
                    //Log.d(TAG,"Followings : "+followApiObjects.toString());
                    HaprampPreferenceManager.getInstance().saveCurrentUserFollowingsAsJson(new Gson().toJson(new FollowApiObjectWrapper(followApiObjects)));

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //check for following
    private boolean isFollowed() {
        return this.followed;
    }

    private String getUsername() {
        return this.mUsername;
    }

    private void t(String s){
        Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
    }

}
