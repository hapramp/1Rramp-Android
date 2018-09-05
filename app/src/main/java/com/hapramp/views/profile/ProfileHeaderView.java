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

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.datastore.callbacks.FollowInfoCallback;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.User;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.FollowListActivity;
import com.hapramp.ui.activity.ProfileEditActivity;
import com.hapramp.ui.activity.WalletActivity;
import com.hapramp.utils.CompleteFollowingHelper;
import com.hapramp.utils.FollowingsSyncUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.skills.InterestsView;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_FOLLOWERS;
import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_FOLLOWING;
import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

/**
 * Created by Ankit on 12/30/2017.
 */

public class ProfileHeaderView extends FrameLayout implements CompleteFollowingHelper.FollowingsSyncCompleteListener, UserProfileCallback, FollowInfoCallback {
  @BindView(R.id.profile_wall_pic)
  ImageView profileWallPic;
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
  @BindView(R.id.followUnfollowProgress)
  ProgressBar followUnfollowProgress;
  @BindView(R.id.bio)
  TextView bio;
  @BindView(R.id.divider_top)
  FrameLayout dividerTop;
  @BindView(R.id.post_counts)
  TextView postCounts;
  @BindView(R.id.followers_count)
  TextView followersCountTv;
  @BindView(R.id.followings_count)
  TextView followingsCountTv;
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
  @BindView(R.id.wallet_info)
  TextView walletInfoBtn;
  @BindView(R.id.profile_header_view_real)
  RelativeLayout profileHeaderViewReal;
  @BindView(R.id.profile_header_container_mock)
  RelativeLayout profileHeaderContainerMock;
  @BindView(R.id.bio1)
  View bio1;
  @BindView(R.id.divider_bottom_mock)
  FrameLayout dividerBottomMock;
  @BindView(R.id.interestCaptionMock)
  View interestCaptionMock;
  @BindView(R.id.profile_header_view_container_mock)
  RelativeLayout profileHeaderViewContainerMock;
  private Context mContext;
  private String TICK_TEXT = "\u2713";
  private String mUsername;
  private Handler mHandler;
  private boolean isFollowed;
  private String me;
  private SteemConnect steemConnect;
  private int followersCount;
  private int followingCount;
  private boolean followInfoAvailable;
  private User cachedUserProfileData;
  private DataStore dataStore;

  public ProfileHeaderView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    mContext = context;
    dataStore = new DataStore();
    View view = LayoutInflater.from(context).inflate(R.layout.profile_header_view, this);
    ButterKnife.bind(this, view);
    mHandler = new Handler();
    attachListeners();
    me = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    steemConnect = SteemConnectUtils.getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
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

    walletInfoBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mContext, WalletActivity.class);
        intent.putExtra(WalletActivity.EXTRA_USERNAME, mUsername);
        mContext.startActivity(intent);
      }
    });

    followersCountTv.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToFollowListPage();
      }
    });

    followingsCountTv.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToFollowListPage();
      }
    });
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

  private void requestFollowOnSteem() {
    showFollowProgress(true);
    new Thread() {
      @Override
      public void run() {
        steemConnect.follow(
          me,
          mUsername,
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  showFollowProgress(false);
                  userFollowedOnSteem();
                }
              });
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  showFollowProgress(false);
                  userFollowFailed();
                }
              });
            }
          }
        );
      }
    }.start();
  }

  private void requestUnFollowOnSteem() {
    showFollowProgress(true);
    new Thread() {
      @Override
      public void run() {
        steemConnect.unfollow(
          me,
          mUsername,
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  showFollowProgress(false);
                  userUnFollowedOnSteem();
                }
              });
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  showFollowProgress(false);
                  userUnfollowFailed();
                }
              });
            }
          }
        );
      }
    }.start();
  }

  private void showFollowProgress(boolean show) {
    if (show) {
      followBtn.setVisibility(GONE);
      followUnfollowProgress.setVisibility(VISIBLE);
    } else {
      followBtn.setVisibility(VISIBLE);
      followUnfollowProgress.setVisibility(GONE);
    }
  }

  private void navigateToFollowListPage() {
    if (followInfoAvailable) {
      Intent intent = new Intent(mContext, FollowListActivity.class);
      intent.putExtra(EXTRA_KEY_USERNAME, mUsername);
      intent.putExtra(EXTRA_KEY_FOLLOWING, followingCount);
      intent.putExtra(EXTRA_KEY_FOLLOWERS, followersCount);
      mContext.startActivity(intent);
    }
  }

  private void userFollowFailed() {
    showFollowProgress(false);
    setFollowState(false);
    t("Failed to follow " + mUsername);
  }

  private void userFollowedOnSteem() {
    showFollowProgress(false);
    setFollowState(true);
    syncFollowings();
    fetchFollowingInfo();
    t("You started following " + mUsername);
  }

  private void fetchFollowingInfo() {
    dataStore.requestFollowInfo(mUsername, this);
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

  private void userUnFollowedOnSteem() {
    try {
      showFollowProgress(false);
      setFollowState(false);
      syncFollowings();
      fetchFollowingInfo();
      t("You unfollowed " + mUsername);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void t(String s) {
    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
  }

  public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ProfileHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setUsername(String username) {
    this.mUsername = username;
    if (username != null) {
      fetchUserInfo();
    }
  }

  private void syncFollowings() {
    FollowingsSyncUtils.syncFollowings(mContext, this);
  }

  private void fetchUserInfo() {
    fetchFollowingInfo();
    dataStore.requestUserProfile(mUsername, this);
  }

  @Override
  public void onUserProfileFetching() {

  }

  public void setPostsCount(long count) {
    String text = count > 1 ? count + " Posts" : count + " Post";
    if (postCounts != null) {
      postCounts.setText(text);
    }
  }

  @Override
  public void onUserProfileAvailable(User user, boolean isFreshData) {
    bind(user);
    cacheUserProfile(user);
  }

  private void setCommunities(List<CommunityModel> communities) {
    interestsView.setCommunities(communities, false);
  }

  private void navigateToProfileEditActivity() {
    Intent intent = new Intent(mContext, ProfileEditActivity.class);
    mContext.startActivity(intent);
  }

  private void userUnfollowFailed() {
    try {
      showFollowProgress(false);
      setFollowState(true);
      t("Failed to unfollow " + mUsername);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void setFollowerCount(int count) {
    String followerText = count > 1 ?
      String.format(getContext().getString(R.string.profile_follower_count_text), count) :
      String.format(getContext().getString(R.string.profile_follower_count_text_singular), count);
    if (followersCountTv != null) {
      followersCountTv.setText(followerText);
    }
  }

  private void setFollowingCount(int count) {
    String followingText = count > 1 ?
      String.format(getContext().getString(R.string.profile_following_count_text_plural), count) :
      String.format(getContext().getString(R.string.profile_following_count_text_singular), count);
    if (followersCountTv != null) {
      followingsCountTv.setText(followingText);
    }
  }

  private void bind(User data) {
    if (profileHeaderViewReal != null) {
      profileHeaderViewContainerMock.setVisibility(GONE);
      profileHeaderViewReal.setVisibility(VISIBLE);
    }
    //check for null view(in case view is removed)
    if (usernameTv == null)
      return;
    if (data.getCover_image() != null) {
      String wall_pic_url = data.getCover_image().length() > 0 ? data.getCover_image() :
        mContext.getResources().getString(R.string.default_wall_pic);
      ImageHandler.load(mContext, profileWallPic, wall_pic_url);
    }
    String profile_pic = String.format(getResources().getString(R.string.steem_user_profile_pic_format_large), mUsername);
    ImageHandler.loadCircularImage(mContext, profilePic, profile_pic);
    usernameTv.setText(data.getFullname());
    hapname.setText(String.format("@%s", data.getUsername()));
    String _bio = data.getAbout();
    bio.setText(_bio);
    setPostsCount(data.getPostCount());
    if (mUsername.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      //self Profile
      followBtn.setVisibility(GONE);
      editBtn.setVisibility(VISIBLE);
      editBtn.setEnabled(true);
      editBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToProfileEditActivity();
        }
      });
      CommunityListWrapper listWrapper = new Gson().fromJson(HaprampPreferenceManager
        .getInstance().getUserSelectedCommunityAsJson(), CommunityListWrapper.class);
      if (interestsView != null) {
        interestsView.setCommunities(listWrapper.getCommunityModels(), true);
      }
    } else {
      followBtn.setVisibility(VISIBLE);
      editBtn.setVisibility(GONE);
      invalidateFollowButton();
      fetchUserCommunities();
    }
  }

  @Override
  public void onFollowInfoError(String e) {

  }

  private void invalidateFollowButton() {
    Set<String> followings = HaprampPreferenceManager.getInstance().getFollowingsSet();
    if (followings != null) {
      if (followBtn != null) {
        followBtn.setVisibility(VISIBLE);
      }
      setFollowState(followings.contains(mUsername));
    } else {
      //hide button
      if (followBtn != null) {
        followBtn.setVisibility(GONE);
      }
    }
  }


  private void cacheUserProfile(User user) {
    if (user.getUsername() == null) {
      Crashlytics.log(mUsername + ":Incorrect profile Info is cached:" + user.toString());
    }
    String json = new Gson().toJson(user);
    HaprampPreferenceManager.getInstance().saveUserProfile(mUsername, json);
  }

  @Override
  public void onSyncCompleted() {
    try {
      invalidateFollowButton();
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void fetchUserCommunities() {
    dataStore.requestUserCommunities(mUsername, new CommunitiesCallback() {
      @Override
      public void onCommunityFetching() {
      }

      @Override
      public void onCommunitiesAvailable(List<CommunityModel> communities, boolean isFreshData) {
        setCommunities(communities);
      }

      @Override
      public void onCommunitiesFetchError(String err) {
        setCommunities(null);
      }
    });
  }

  @Override
  public void onUserProfileFetchError(String err) {
  }

  @Override
  public void onFollowInfoAvailable(final int followers, final int followings) {
    this.followersCount = followers;
    this.followingCount = followings;
    followInfoAvailable = true;
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        setFollowerCount(followers);
        setFollowingCount(followings);
      }
    });
  }
}
