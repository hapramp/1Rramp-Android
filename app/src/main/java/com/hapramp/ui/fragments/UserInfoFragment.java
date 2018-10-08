package com.hapramp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hapramp.utils.CompleteFollowingHelper;
import com.hapramp.utils.FollowingsSyncUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ReputationCalc;
import com.hapramp.views.skills.InterestsView;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_FOLLOWERS;
import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_FOLLOWING;
import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

public class UserInfoFragment extends Fragment implements FollowInfoCallback, UserProfileCallback, CompleteFollowingHelper.FollowingsSyncCompleteListener {
  Unbinder unbinder;
  @BindView(R.id.profile_wall_pic)
  ImageView profileWallPic;
  @BindView(R.id.profile_pic)
  ImageView profilePic;
  @BindView(R.id.profile_header_container)
  RelativeLayout profileHeaderContainer;
  @BindView(R.id.usernameTv)
  TextView usernameTv;
  @BindView(R.id.reputation)
  TextView reputation;
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
  @BindView(R.id.profile_header_view_real)
  RelativeLayout profileHeaderViewReal;
  @BindView(R.id.profile_header_container_mock)
  RelativeLayout profileHeaderContainerMock;
  @BindView(R.id.divider_bottom_mock)
  FrameLayout dividerBottomMock;
  @BindView(R.id.interestCaptionMock)
  View interestCaptionMock;
  @BindView(R.id.profile_header_view_container_mock)
  RelativeLayout profileHeaderViewContainerMock;
  @BindView(R.id.bio1)
  View bio1;

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
  private DataStore dataStore;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.user_info_fragment, null);
    unbinder = ButterKnife.bind(this, view);
    init(mContext);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void init(Context context) {
    mContext = context;
    dataStore = new DataStore();
    mHandler = new Handler();
    attachListeners();
    me = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    steemConnect = SteemConnectUtils.getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    if (mUsername != null) {
      fetchUserInfo();
    }
  }


  private void attachListeners() {
    followBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isFollowed) {
          confirmUnfollowAction();
        } else {
          requestFollowOnSteem();
        }
      }
    });

    followersCountTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToFollowListPage();
      }
    });

    followingsCountTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToFollowListPage();
      }
    });
  }

  private void fetchUserInfo() {
    fetchFollowingInfo();
    dataStore.requestUserProfile(mUsername, this);
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

  private void navigateToFollowListPage() {
    if (followInfoAvailable) {
      Intent intent = new Intent(mContext, FollowListActivity.class);
      intent.putExtra(EXTRA_KEY_USERNAME, mUsername);
      intent.putExtra(EXTRA_KEY_FOLLOWING, followingCount);
      intent.putExtra(EXTRA_KEY_FOLLOWERS, followersCount);
      mContext.startActivity(intent);
    }
  }

  private void fetchFollowingInfo() {
    dataStore.requestFollowInfo(mUsername, this);
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
    try { // to avoid dead view access
      if (show) {
        followBtn.setVisibility(GONE);
        followUnfollowProgress.setVisibility(VISIBLE);
      } else {
        followBtn.setVisibility(VISIBLE);
        followUnfollowProgress.setVisibility(GONE);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void userFollowedOnSteem() {
    showFollowProgress(false);
    setFollowState(true);
    syncFollowings();
    fetchFollowingInfo();
    t("You started following " + mUsername);
  }

  private void userFollowFailed() {
    showFollowProgress(false);
    setFollowState(false);
    t("Failed to follow " + mUsername);
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

  private void setFollowState(boolean state) {
    try { // to avoid dead view access
      if (state) {
        followBtn.setText(TICK_TEXT + " Following");
        isFollowed = true;
      } else {
        followBtn.setText("Follow");
        isFollowed = false;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void syncFollowings() {
    FollowingsSyncUtils.syncFollowings(mContext, this);
  }

  private void t(String s) {
    Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
  }

  public void setUsername(String username) {
    this.mUsername = username;
  }

  @Override
  public void onUserProfileFetching() {

  }

  @Override
  public void onUserProfileAvailable(User user, boolean isFreshData) {
    bind(user);
    cacheUserProfile(user);
  }

  private void bind(User data) {
    if (profileHeaderViewReal != null) {
      profileHeaderViewContainerMock.setVisibility(GONE);
      profileHeaderViewReal.setVisibility(VISIBLE);
    }
    try {
      String wallPicUrl = mContext.getResources().getString(R.string.default_wall_pic);
      if (data.getCover_image() != null) {
        if (data.getCover_image().length() > 0) {
          wallPicUrl = data.getCover_image();
        }
      }
      ImageHandler.loadUnOverridden(mContext, profileWallPic, wallPicUrl);
      String profileImageUrl = String.format(getResources().getString(R.string.steem_user_profile_pic_format_large), mUsername);
      if (data.getProfile_image() == null) {
        if (data.getProfile_image().length() > 0) {
          profileImageUrl = data.getProfile_image();
        }
      }
      ImageHandler.loadCircularImage(mContext, profilePic, profileImageUrl);
      usernameTv.setText(data.getUsername());
      reputation.setText(String.format(Locale.US, " (%.2f)", ReputationCalc.calculateReputation(data.getReputation())));
      String _bio = data.getAbout();
      bio.setText(_bio);
      setPostsCount(data.getPostCount());
      if (mUsername.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
        //self Profile
        followBtn.setVisibility(GONE);
        editBtn.setVisibility(VISIBLE);
        editBtn.setEnabled(true);
        editBtn.setOnClickListener(new View.OnClickListener() {
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
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void cacheUserProfile(User user) {
    if (user.getUsername() == null) {
      Crashlytics.log(mUsername + ":Incorrect profile Info is cached:" + user.toString());
    }
    String json = new Gson().toJson(user);
    HaprampPreferenceManager.getInstance().saveUserProfile(mUsername, json);
  }

  public void setPostsCount(long count) {
    String text = count > 1 ? count + " Posts" : count + " Post";
    if (postCounts != null) {
      postCounts.setText(text);
    }
  }

  private void navigateToProfileEditActivity() {
    Intent intent = new Intent(mContext, ProfileEditActivity.class);
    mContext.startActivity(intent);
  }

  private void invalidateFollowButton() {
    try {
      Set<String> followings = HaprampPreferenceManager.getInstance().getFollowingsSet();
      if (followings != null) {
        if (followBtn != null) {
          followBtn.setVisibility(VISIBLE);
        }
        setFollowState(followings.contains(mUsername));
      } else {
        if (followBtn != null) {
          followBtn.setVisibility(GONE);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
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

  private void setCommunities(List<CommunityModel> communities) {
    if (interestsView != null) {
      interestsView.setCommunities(communities, false);
    }
  }

  @Override
  public void onUserProfileFetchError(String err) {
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

  private void setFollowerCount(int count) {
    String followerText = count > 1 ?
      String.format(getContext().getString(R.string.profile_follower_count_text), count) :
      String.format(getContext().getString(R.string.profile_follower_count_text_singular), count);
    if (followersCountTv != null) {
      followersCountTv.setText(followerText);
    }
  }

  private void setFollowingCount(int count) {
    String followingText = String.format(getContext().getString(R.string.profile_following_count_text_singular), count);
    if (followersCountTv != null) {
      followingsCountTv.setText(followingText);
    }
  }

  @Override
  public void onFollowInfoError(String e) {

  }
}
