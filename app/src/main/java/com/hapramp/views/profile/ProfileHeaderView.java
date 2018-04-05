package com.hapramp.views.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.activity.ProfileEditActivity;
import com.hapramp.api.DataServer;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.interfaces.FollowUserCallback;
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.requests.FollowRequestBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.skills.InterestsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.follow.model.FollowCountApiObject;
import eu.bittrade.libs.steemj.base.models.Account;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 12/30/2017.
 */

public class ProfileHeaderView extends FrameLayout implements FollowUserCallback {

    @BindView(R.id.profile_pic)
    ImageView profilePic;
    @BindView(R.id.profile_header_container)
    RelativeLayout profileHeaderContainer;
    @BindView(R.id.username)
    TextView username;
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

    private Context mContext;
    private String TICK_TEXT = "\u2713";
    private ProfileHeaderModel profileData;
    private boolean isFollowed = false;
    private String mUsername;
    private Handler mHandler;
    private boolean loaded;

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
    }

    public void setUsername(String username) {
        this.mUsername = username;
        if (username != null) {
            if(!loaded) {
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
        if(username==null)
            return;

        loaded = true;
        ImageHandler.loadCircularImage(mContext, profilePic, data.getUser().getJsonMetadata().getProfile().getProfileImage());

        String wall_pic_url = data.getUser().getJsonMetadata().getProfile().getCover_image() != null ?
                data.getUser().getJsonMetadata().getProfile().getCover_image()
                :
                mContext.getResources().getString(R.string.default_wall_pic);

        ImageHandler.load(mContext, profileWallPic, wall_pic_url);

        username.setText(data.getUser().getJsonMetadata().getProfile().getName());
        hapname.setText(String.format("@%s", data.getUser().getName()));
        bio.setText(data.getUser().getJsonMetadata().getProfile().getAbout());
        setPostsCount(data.getUser().getPostCount());
        CommunityListWrapper listWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
        interestsView.setCommunities(listWrapper.getCommunityModels());

//        if (true) {
//
//            followBtn.setVisibility(GONE);
//            editBtn.setVisibility(View.VISIBLE);
//            editBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    navigateToProfileEditActivity();
//                }
//            });
//
//        } else {
//
//            editBtn.setVisibility(GONE);
//            followBtn.setVisibility(View.VISIBLE);
//            followBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    setUserFollow(!isFollowed);
//                }
//            });
//        }

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

    private void navigateToProfileEditActivity() {

        Intent intent = new Intent(mContext, ProfileEditActivity.class);
        mContext.startActivity(intent);

    }

    @Override
    public void onUserFollowSet(boolean state) {
        setFollowState(state);
    }

    @Override
    public void onUserFollowSetFailed(boolean state) {
        setFollowState(state);
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
        postCounts.setText(String.format(mContext.getResources().getString(R.string.profile_posts_count_caption),count));
    }
}
