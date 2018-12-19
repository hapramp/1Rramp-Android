package com.hapramp.views.post;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.SinglePostCallback;
import com.hapramp.interfaces.RebloggedUserFetchCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.SteemRePoster;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.CommentsActivity;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.ui.activity.RebloggedListActivity;
import com.hapramp.ui.activity.VotersListActivity;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PostMenu;
import com.hapramp.utils.ShareUtils;
import com.hapramp.views.CommunityStripView;
import com.hapramp.views.VoterPeekView;
import com.hapramp.views.extraa.StarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.utils.VoteUtils.checkForMyVote;
import static com.hapramp.utils.VoteUtils.getMyVotePercent;
import static com.hapramp.utils.VoteUtils.getNonZeroVoters;

/**
 * Created by Ankit on 12/30/2017.
 */

public class PostItemView extends FrameLayout implements RebloggedUserFetchCallback {
  public static final String TAG = PostItemView.class.getSimpleName();
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.popupMenuDots)
  ImageView popupMenuDots;
  @BindView(R.id.featured_image_post)
  ImageView featuredImagePost;
  @BindView(R.id.image_container)
  RelativeLayout imageContainer;
  @BindView(R.id.post_title)
  TextView postTitle;
  @BindView(R.id.post_snippet)
  TextView postSnippet;
  @BindView(R.id.starView)
  StarView starView;
  @BindView(R.id.divider)
  View divider;
  @BindView(R.id.comment_icon)
  ImageView commentIcon;
  @BindView(R.id.comment_count)
  TextView commentCount;
  @BindView(R.id.dollar_icon)
  ImageView dollarIcon;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.rating_desc)
  TextView ratingDesc;
  @BindView(R.id.voters_peek_view)
  VoterPeekView votersPeekView;
  @BindView(R.id.resteemed_icon)
  ImageView resteemedIcon;
  @BindView(R.id.repost_count)
  TextView repostCount;
  @BindView(R.id.repost_container)
  LinearLayout repostContainer;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  @BindView(R.id.rate_info_container)
  RelativeLayout rateInfoContainer;
  @BindView(R.id.comment_container)
  LinearLayout commentContainer;

  private SteemRePoster steemRePoster;
  private Context mContext;
  private Feed mFeed;
  private Handler mHandler;
  private SteemConnect steemConnect;
  private DataStore dataStore;
  private String briefPayoutValueString = "$";
  private String mCurrentLoggedInUser = "";

  private Runnable steemCastingVoteExceptionRunnable = new Runnable() {
    @Override
    public void run() {
      castingVoteFailed();
    }
  };
  private Runnable steemCancellingVoteExceptionRunnable = new Runnable() {
    @Override
    public void run() {
      voteDeleteFailed();
    }
  };
  private PostActionListener postActionListener;
  private int mItemIndex;
  private ArrayList<String> mRebloggers;
  private ProgressDialog progressDialog;

  public PostItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    mRebloggers = new ArrayList<>();
    progressDialog = new ProgressDialog(mContext);
    mCurrentLoggedInUser = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, this);
    ButterKnife.bind(this, view);
    mHandler = new Handler();
    dataStore = new DataStore();
    attachListeners();
    steemConnect = SteemConnectUtils.getSteemConnectInstance(HaprampPreferenceManager
      .getInstance().getSC2AccessToken());
  }

  private void attachListeners() {
    postTitle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToDetailsPage();
      }
    });
    repostContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openRebloggedUserListPage(mRebloggers);
      }
    });
    commentContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToCommentsPage();
      }
    });
    featuredImagePost.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToDetailsPage();
      }
    });
    commentCount.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
       navigateToCommentsPage();
      }
    });
    postSnippet.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToDetailsPage();
      }
    });

    votersPeekView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        openVotersList();
      }
    });
  }

  private void navigateToDetailsPage() {
    Intent detailsIntent = new Intent(mContext, DetailedActivity.class);
    detailsIntent.putExtra(Constants.EXTRAA_KEY_POST_DATA, mFeed);
    detailsIntent.putStringArrayListExtra(Constants.EXTRA_KEY_REBLOGGERS,mRebloggers);
    mContext.startActivity(detailsIntent);
  }

  private void openRebloggedUserListPage(ArrayList<String> users) {
    Intent intent = new Intent(mContext, RebloggedListActivity.class);
    intent.putStringArrayListExtra(RebloggedListActivity.EXTRA_USER_LIST, users);
    intent.putExtra(RebloggedListActivity.EXTRA_AUTHOR, mFeed.getAuthor());
    intent.putExtra(RebloggedListActivity.EXTRA_PERMLINK, mFeed.getPermlink());
    mContext.startActivity(intent);
  }

  private void openVotersList() {
    Intent intent = new Intent(mContext, VotersListActivity.class);
    intent.putParcelableArrayListExtra(VotersListActivity.EXTRA_USER_LIST, mFeed.getVoters());
    mContext.startActivity(intent);
  }

  public PostItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PostItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void bind(final Feed feed) {
    this.mFeed = feed;
    fetchRebloggedUsers();
    postSnippet.setVisibility(VISIBLE);
    feedOwnerTitle.setText(feed.getAuthor());
    feedOwnerSubtitle.setText(String.format(mContext.getResources().getString(R.string.post_subtitle_format), MomentsUtils.getFormattedTime(feed.getCreatedAt())));
    setSteemEarnings(feed);
    setCommunities(feed.getTags());
    String bdy = feed.getCleanedBody();
    if (bdy.length() > 0) {
      postSnippet.setVisibility(VISIBLE);
      postSnippet.setText(bdy);
    } else {
      postSnippet.setVisibility(GONE);
    }
    if (feed.getTitle().length() > 0) {
      postTitle.setVisibility(VISIBLE);
      postTitle.setText(feed.getTitle());
    } else {
      postTitle.setVisibility(GONE);
    }
    if (feed.getFeaturedImageUrl().length() > 0) {
      featuredImagePost.setVisibility(VISIBLE);
      ImageHandler.load(mContext, featuredImagePost, feed.getFeaturedImageUrl());
    } else {
      featuredImagePost.setVisibility(GONE);
    }
    ImageHandler.loadCircularImage(mContext, feedOwnerPic,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        feed.getAuthor()));
    updateVotersPeekView(feed.getActiveVoters());
    bindVotes(feed.getVoters(), feed.getPermlink());
    setCommentCount(feed.getChildren());
    attachListerOnAuthorHeader();
    attachListenerForOverlowIcon();
  }

  private void attachListenerForOverlowIcon() {
    popupMenuDots.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        showPopup();
      }
    });
  }

  private void attachListerOnAuthorHeader() {
    feedOwnerPic.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToUserProfile();
      }
    });
    feedOwnerTitle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToUserProfile();
      }
    });
  }

  private void setSteemEarnings(Feed feed) {
    try {
      double pendingPayoutValue = Double.parseDouble(feed.getPendingPayoutValue().split(" ")[0]);
      double totalPayoutValue = Double.parseDouble(feed.getTotalPayoutValue().split(" ")[0]);
      double curatorPayoutValue = Double.parseDouble(feed.getCuratorPayoutValue().split(" ")[0]);
      double maxAcceptedValue = Double.parseDouble(feed.getMaxAcceptedPayoutValue().split(" ")[0]);
      if (pendingPayoutValue > 0) {
        payoutValue.setVisibility(VISIBLE);
        dollarIcon.setVisibility(VISIBLE);
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", pendingPayoutValue);
        payoutValue.setText(briefPayoutValueString);
      } else if ((totalPayoutValue + curatorPayoutValue) > 0) {
        //cashed out
        payoutValue.setVisibility(VISIBLE);
        dollarIcon.setVisibility(VISIBLE);
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", (totalPayoutValue + curatorPayoutValue));
        payoutValue.setText(briefPayoutValueString);
      } else {
        payoutValue.setVisibility(GONE);
        dollarIcon.setVisibility(GONE);
      }

      //format payout string
      if(maxAcceptedValue==0){
        payoutValue.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
      }else{
        payoutValue.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
      }
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void bindVotes(List<Voter> votes, String permlink) {
    starView.setData(votes,permlink)
      .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
        @Override
        public void onVoted(String full_permlink, int _vote) {
          performVoteOnSteem(_vote);
        }

        @Override
        public void onVoteDeleted(String full_permlink) {
          deleteVoteOnSteem();
        }

        @Override
        public void onVoteDescription(String msg) {
          ratingDesc.setText(Html.fromHtml(msg));
        }
      });
    ratingDesc.setText(Html.fromHtml(starView.getVoteDescription()));
  }

  private void updateVotersPeekView(List<Voter> voters) {
    if (votersPeekView != null) {
      votersPeekView.setVoters(voters);
    }
  }

  /*
   *  author of the vote: author of the post
   *  cancel vote
   * */
  private void deleteVoteOnSteem() {
    starView.voteProcessing();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        starView.deletedVoteTemporarily();
      }
    }, 500);
    new Thread() {
      @Override
      public void run() {
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername(), getAuthor(), getPermlinkAsString(), String.valueOf(0), new SteemConnectCallback() {
          @Override
          public void onResponse(String s) {
            updatePostFromBlockchain();
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                voteDeleteSuccess();
              }
            });
          }

          @Override
          public void onError(SteemConnectException e) {
            mHandler.post(steemCancellingVoteExceptionRunnable);
          }
        });
      }
    }.start();
  }

  private void updatePostFromBlockchain() {
    dataStore.requestSingleFeed(mFeed.getAuthor(), mFeed.getPermlink(), new SinglePostCallback() {
      @Override
      public void onPostFetched(Feed feed) {
        if (feed != null) {
          bind(feed);
        }
      }

      @Override
      public void onPostFetchError(String err) {
      }
    });
  }

  private void castingVoteFailed() {
    if (starView != null) {
      starView.failedToCastVote();
    }
  }

  private void fetchRebloggedUsers() {
    String reqTag = getRebloggedUserRequestTag();
    if (ConnectionUtils.isConnected(mContext)) {
      dataStore.fetchRebloggedUsers(reqTag, mFeed.getAuthor(), mFeed.getPermlink(), this);
    }
  }

  private void castingVoteSuccess() {
    if (starView != null) {
      starView.castedVoteSuccessfully();
    }
  }

  private void voteDeleteFailed() {
    if (starView != null) {
      starView.failedToDeleteVoteFromServer();
    }
  }

  private void voteDeleteSuccess() {
    if (starView != null) {
      starView.deletedVoteSuccessfully();
    }
  }

  private void performVoteOnSteem(final int vote) {
    starView.voteProcessing();
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_VOTE);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        starView.castedVoteTemporarily();
      }
    }, 500);

    new Thread() {
      @Override
      public void run() {
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
          getAuthor(), getPermlinkAsString(), String.valueOf(vote), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              updatePostFromBlockchain();
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  castingVoteSuccess();
                }
              });
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(steemCastingVoteExceptionRunnable);
            }
          });
      }
    }.start();
  }

  public String getAuthor() {
    return mFeed.getAuthor();
  }

  public String getPermlinkAsString() {
    return mFeed.getPermlink();
  }

  private boolean isContentEllipsised(TextView textView) {
    Layout layout = textView.getLayout();
    if (layout != null) {
      int lineCount = layout.getLineCount();
      for (int i = 0; i < lineCount; i++) {
        if (layout.getEllipsisCount(i) > 0) {
          return true;
        }
      }
    }
    return false;
  }

  private void navigateToCommentsPage() {
    Intent intent = new Intent(mContext, CommentsActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_POST_AUTHOR, mFeed.getAuthor());
    intent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK, mFeed.getPermlink());
    mContext.startActivity(intent);
  }

  private void navigateToUserProfile() {
    Intent intent = new Intent(mContext, ProfileActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, mFeed.getAuthor());
    mContext.startActivity(intent);
  }

  public void setPostData(Feed postData) {
    bind(postData);
  }

  private void setCommentCount(int count) {
    commentCount.setText(String.valueOf(count));
  }

  private void showPopup() {
    int menu_res_id = R.menu.popup_post;
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
    PopupMenu popup = new PopupMenu(contextThemeWrapper, popupMenuDots);
    //customize menu items
    if ((getActiveVoteCount() == 0) &&
      mFeed.getAuthor().equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      //add Delete Option
      popup.getMenu().add(PostMenu.Delete);
      //add share option
      popup.getMenu().add(PostMenu.Share);
    } else {
      //add Share
      popup.getMenu().add(PostMenu.Share);
      if (!mRebloggers.contains(mCurrentLoggedInUser)) {
        //add repost option
        popup.getMenu().add(PostMenu.Repost);
      }
    }
    popup.getMenuInflater().inflate(menu_res_id, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getTitle().equals(PostMenu.Delete)) {
          showAlertDialogForDelete();
          return true;
        } else if (item.getTitle().equals(PostMenu.Share)) {
          ShareUtils.shareMixedContent(mContext, mFeed);
          return true;
        } else if (item.getTitle().equals(PostMenu.Repost)) {
          showAlertDialogForRepost();
        }
        return false;
      }
    });
    popup.show();
  }

  private void showAlertDialogForRepost() {
    new AlertDialog.Builder(mContext)
      .setTitle("Repost?")
      .setMessage("This post will appear on your profile. This action cannot be reversed.")
      .setPositiveButton("Repost", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          repostThisPost();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void repostThisPost() {
    showProgressDialog(true, "Reposting....");
    steemRePoster = new SteemRePoster();
    steemRePoster.setRepostCallback(new SteemRePoster.RepostCallback() {
      @Override
      public void reposted() {
        showProgressDialog(false, "");
        Toast.makeText(mContext, "Reposted", Toast.LENGTH_LONG).show();
      }

      @Override
      public void failedToRepost() {
        showProgressDialog(false, "");
        Toast.makeText(mContext, "Failed to Repost", Toast.LENGTH_LONG).show();
      }
    });
    steemRePoster.repost(mFeed.getAuthor(), mFeed.getPermlink());
  }

  private void showProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    }
  }

  private int getActiveVoteCount() {
    int activeVoteCount = 0;
    for (int i = 0; i < mFeed.getVoters().size(); i++) {
      if (mFeed.getVoters().get(i).getPercent() > 0) {
        activeVoteCount++;
      }
    }
    return activeVoteCount;
  }

  private void showAlertDialogForDelete() {
    new AlertDialog.Builder(mContext)
      .setTitle("Delete Post")
      .setMessage("Do you want to Delete ? ")
      .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteThisPostItem();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void deleteThisPostItem() {
    if (postActionListener != null) {
      postActionListener.onPostDeleted(this.mItemIndex);
    }
    new Thread() {
      @Override
      public void run() {
        SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager
            .getInstance().getSC2AccessToken());
        steemConnect.delete(mFeed.getAuthor(), mFeed.getPermlink(), new SteemConnectCallback() {
          @Override
          public void onResponse(String response) {
          }

          @Override
          public void onError(SteemConnectException e) {
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(mContext, "Error occurred while deleting post.", Toast.LENGTH_LONG).show();
              }
            });
          }
        });
      }
    }.start();
  }

  public void setPostActionListener(PostActionListener postActionListener) {
    this.postActionListener = postActionListener;
  }

  public void setCommunities(ArrayList<String> communities) {
    communityStripeView.setCommunities(communities);
  }

  public void setItemIndex(int itemIndex) {
    this.mItemIndex = itemIndex;
  }

  @Override
  public void onRebloggedUserFetched(String reqTag, ArrayList<String> rebloggers) {
    try {
      if (rebloggers.contains(mFeed.getAuthor())) {
        rebloggers.remove(mFeed.getAuthor());
      }
      this.mRebloggers = rebloggers;
      setRepostCount(rebloggers.size());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRebloggedUserFailed() {

  }

  private void setRepostCount(int count) {
      repostContainer.setVisibility(VISIBLE);
      repostCount.setText(String.valueOf(count));
  }

  private String getRebloggedUserRequestTag() {
    return String.format("%s_%s", mFeed.getAuthor(), mFeed.getPermlink());
  }

  public interface PostActionListener {
    void onPostDeleted(int itemIndex);
  }
}

