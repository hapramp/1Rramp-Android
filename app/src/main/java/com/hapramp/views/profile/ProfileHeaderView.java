package com.hapramp.views.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.activity.ProfileEditActivity;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.FollowApiObjectWrapper;
import com.hapramp.steem.SteemHelper;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.skills.InterestsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.apis.follow.model.FollowCountApiObject;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 12/30/2017.
 */

public class ProfileHeaderView extends FrameLayout {

    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.usernameTv)
    TextView usernameTv;
    @BindView(R.id.hapname)
    TextView hapname;
    @BindView(R.id.profile_user_name_container)
    RelativeLayout profileUserNameContainer;
    @BindView(R.id.edit_btn)
    TextView editBtn;
    @BindView(R.id.follow_btn)
    TextView followBtn;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.divider_top)
    FrameLayout dividerTop;
    @BindView(R.id.post_counts)
    TextView postCounts;
    @BindView(R.id.followers_count)
    TextView followersCount;
    @BindView(R.id.followings_count)
    TextView followingsCount;
    @BindView(R.id.post_stats)
    LinearLayout postStats;
    @BindView(R.id.divider_bottom)
    FrameLayout dividerBottom;
    @BindView(R.id.interestCaption)
    TextView interestCaption;
    @BindView(R.id.interestsView)
    InterestsView interestsView;
    @BindView(R.id.postsCaption)
    TextView postsCaption;
    @BindView(R.id.profile_wall_pic)
    ImageView profileWallPic;
    @BindView(R.id.followUnfollowProgress)
    ProgressBar followUnfollowProgress;

    private Context mContext;
    private String TICK_TEXT = "\u2713";
    private String mUsername;
    private Handler mHandler;
    private boolean loaded;
    private boolean isFollowed;

    public ProfileHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.profile_header_view, this);
        ButterKnife.bind(this, view);
        mHandler = new Handler();
        attachListeners();

    }

    private void attachListeners() {

        followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowed) {
                    confirmUnfollowAction();
                } else {
                    requestFollowOnSteem();
                }
            }
        });

    }

    public void setUsername(String username) {
        this.mUsername = username;
        if (username != null) {
            if (!loaded) {
                fetchUserInfo();
                fetchFollowInfo();
            }
        }
    }

    private void fetchUserInfo() {

        String current_user_api_url = String.format(
                mContext.getResources().getString(R.string.steem_user_api),
                mUsername);

        RetrofitServiceGenerator.getService()
                .getSteemUser(current_user_api_url)
                .enqueue(new Callback<SteemUser>() {
                    @Override
                    public void onResponse(Call<SteemUser> call, Response<SteemUser> response) {
                        //populate User Info
                        if (response.isSuccessful()) {
                            bind(response.body());
                        } else {
                            failedToFetchSteemInfo();
                        }
                    }

                    @Override
                    public void onFailure(Call<SteemUser> call, Throwable t) {
                        failedToFetchSteemInfo();
                    }
                });

    }

    private void failedToFetchSteemInfo() {

    }

    private void bind(SteemUser data) {

        //check for null view(incase view is removed)
        if (usernameTv == null)
            return;



        loaded = true;
        String profile_pic = data.getUser().getJsonMetadata().getProfile().getProfileImage() != null ?
                data.getUser().getJsonMetadata().getProfile().getProfileImage()
                :
                mContext.getResources().getString(R.string.default_user_pic);

        String wall_pic_url = data.getUser().getJsonMetadata().getProfile().getCover_image() != null ?
                data.getUser().getJsonMetadata().getProfile().getCover_image()
                :
                mContext.getResources().getString(R.string.default_wall_pic);

        String _bio = data.getUser().getJsonMetadata().getProfile().getAbout() != null ? data.getUser().getJsonMetadata().getProfile().getAbout() : "";

        ImageHandler.loadCircularImage(mContext, profilePic, profile_pic);
        ImageHandler.load(mContext, profileWallPic, wall_pic_url);


        usernameTv.setText(data.getUser().getJsonMetadata().getProfile().getName());
        hapname.setText(String.format("@%s", data.getUser().getName()));

        bio.setText(_bio);

        setPostsCount(data.getUser().getPostCount());

        if(mUsername.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())){
            //self Profile
            followBtn.setVisibility(GONE);
            CommunityListWrapper listWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
            interestsView.setCommunities(listWrapper.getCommunityModels());

        }else{

            followBtn.setVisibility(VISIBLE);
            // set follow or unfollow button
            invalidateFollowButton();
            interestsView.setCommunities(new ArrayList<CommunityModel>());

        }


    }

    private void invalidateFollowButton() {

        FollowApiObjectWrapper followApiObjectWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserFollowingsAsJson(), FollowApiObjectWrapper.class);
        List<FollowApiObject> followApiObjects = followApiObjectWrapper.getFollowings();

        for (int i = 0; i < followApiObjects.size(); i++) {
            String _followedUser = followApiObjects.get(i).getFollowing().getName();
            if (_followedUser.equals(mUsername)) {
                setFollowState(true);
                return;
            }
        }
        setFollowState(false);
    }

    private void fetchFollowInfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    SteemJ steemJ = new SteemJ();
                    AccountName accountName = new AccountName(mUsername);
                    final FollowCountApiObject followCountApiObject = steemJ.getFollowCount(accountName);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            followersCount.setText(String.format(mContext.getResources().getString(R.string.profile_followers_caption), followCountApiObject.getFollowerCount()));
                            followingsCount.setText(String.format(mContext.getResources().getString(R.string.profile_following_count_caption), followCountApiObject.getFollowingCount()));
                        }
                    });
                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void requestFollowOnSteem() {

        showFollowProgress(true);

        new Thread() {
            @Override
            public void run() {
                SteemJ steemJ = SteemHelper.getSteemInstance();
                try {
                    steemJ.follow(new AccountName(mUsername));
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

    private void confirmUnfollowAction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("Unfollow")
                .setMessage("Do you want to Unfollow " + mUsername + " ?")
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
        showFollowProgress(false);
        setFollowState(true);
        fetchFollowingsAndCache();
        t("You started following " + mUsername);
    }

    private void userFollowFailed() {
        showFollowProgress(false);
        setFollowState(false);
        t("Failed to follow " + mUsername);
    }

    private void userUnFollowedOnSteem() {
        showFollowProgress(false);
        setFollowState(false);
        fetchFollowingsAndCache();
        t("You unfollowed " + mUsername);
    }

    private void userUnfollowFailed() {
        showFollowProgress(false);
        setFollowState(true);
        t("Failed to unfollow " + mUsername);
    }

    private void showFollowProgress(boolean show) {

        if (show) {
            //hide button
            followBtn.setVisibility(GONE);
            followUnfollowProgress.setVisibility(VISIBLE);
        } else {
            //show button
            followBtn.setVisibility(VISIBLE);
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

    private void t(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
    }

    private void requestUnFollowOnSteem() {

        showFollowProgress(true);

        new Thread() {
            @Override
            public void run() {
                SteemJ steemJ = SteemHelper.getSteemInstance();
                try {
                    steemJ.unfollow(new AccountName(mUsername));
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

    private void navigateToProfileEditActivity() {

        Intent intent = new Intent(mContext, ProfileEditActivity.class);
        mContext.startActivity(intent);

    }

    private void setFollowState(boolean state) {

        if (state) {
            followBtn.setText(TICK_TEXT + " Following");
            isFollowed = true;
        } else {
            followBtn.setText("Follow");
            isFollowed = false;
        }

    }

    public void setPostsCount(long count) {
        postCounts.setText(String.format(mContext.getResources().getString(R.string.profile_posts_count_caption), count));
    }

}
