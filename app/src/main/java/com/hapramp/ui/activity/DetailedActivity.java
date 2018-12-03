package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.EventReporter;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.datastore.callbacks.SinglePostCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.notification.FirebaseNotificationStore;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.SteemCommentCreator;
import com.hapramp.steem.SteemRePoster;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.CommunityUtils;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PostMenu;
import com.hapramp.utils.RegexUtils;
import com.hapramp.utils.ShareUtils;
import com.hapramp.utils.VoteUtils;
import com.hapramp.views.CommunityStripView;
import com.hapramp.views.VoterPeekView;
import com.hapramp.views.comments.CommentsItemView;
import com.hapramp.views.extraa.StarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DetailedActivity extends AppCompatActivity implements
  SteemCommentCreator.SteemCommentCreateCallback,
  CommentsCallback {
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.overflowBtn)
  ImageView overflowBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.reference_line)
  Space referenceLine;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.post_header_container)
  RelativeLayout postHeaderContainer;
  @BindView(R.id.post_title)
  TextView postTitle;
  @BindView(R.id.markdownView)
  WebView webView;
  @BindView(R.id.hashtags)
  TextView hashtagsView;
  @BindView(R.id.shareBtn)
  TextView shareBtn;
  @BindView(R.id.commentsViewContainer)
  LinearLayout commentsViewContainer;
  @BindView(R.id.commentLoadingProgressBar)
  ProgressBar commentLoadingProgressBar;
  @BindView(R.id.emptyCommentsCaption)
  TextView emptyCommentsCaption;
  @BindView(R.id.moreCommentsCaption)
  TextView moreCommentsCaption;
  @BindView(R.id.commentCreaterAvatar)
  ImageView commentCreaterAvatar;
  @BindView(R.id.commentInputBox)
  EditText commentInputBox;
  @BindView(R.id.sendButton)
  TextView sendButton;
  @BindView(R.id.mockCommentParentView)
  RelativeLayout mockCommentParentView;
  @BindView(R.id.scroller)
  ScrollView scroller;
  @BindView(R.id.shadow)
  ImageView shadow;
  @BindView(R.id.commentBtn)
  ImageView commentBtn;
  @BindView(R.id.commentCount)
  TextView commentCount;
  @BindView(R.id.comment_btn_container)
  LinearLayout commentBtnContainer;
  @BindView(R.id.payoutBtn)
  ImageView dollarIcon;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.starView)
  StarView starView;
  @BindView(R.id.postMetaContainer)
  RelativeLayout postMetaContainer;
  @BindView(R.id.details_activity_cover)
  View detailsActivityCover;
  @BindView(R.id.feed_loading_progress_bar)
  ProgressBar feedLoadingProgressBar;
  @BindView(R.id.rating_desc)
  TextView ratingDesc;
  @BindView(R.id.voters_peek_view)
  VoterPeekView votersPeekView;
  @BindView(R.id.gotoParentBtn)
  TextView gotoParentBtn;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  private Handler mHandler;
  private Feed post;
  private ProgressDialog progressDialog;
  private SteemCommentCreator steemCommentCreator;
  private List<CommentModel> comments;
  private ArrayList<String> mRebloggers;
  private SteemConnect steemConnect;
  private DataStore dataStore;
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
  private SteemRePoster steemRePoster;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details_post);
    ButterKnife.bind(this);
    init();
    collectExtras();
    setTypefaces();
    attachListener();
    EventReporter.addEvent(AnalyticsParams.SCREEN_DETAILED_POST);
  }

  private void init() {
    mHandler = new Handler();
    dataStore = new DataStore();
    comments = new ArrayList<>();
    mRebloggers = new ArrayList<>();
    steemCommentCreator = new SteemCommentCreator();
    steemCommentCreator.setSteemCommentCreateCallback(this);
    progressDialog = new ProgressDialog(this);
    SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
    instanceBuilder.setAcessToken(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    steemConnect = instanceBuilder.build();
  }

  private void collectExtras() {
    Bundle bundle = getIntent().getExtras();
    if (bundle.getParcelable(Constants.EXTRAA_KEY_POST_DATA) != null) {
      post = bundle.getParcelable(Constants.EXTRAA_KEY_POST_DATA);
      mRebloggers = bundle.getStringArrayList(Constants.EXTRA_KEY_REBLOGGERS);
      bindPostValues(post);
      return;
    }

    if (bundle.getString(Constants.EXTRAA_KEY_POST_AUTHOR) != null) {
      String author = bundle.getString(Constants.EXTRAA_KEY_POST_AUTHOR);
      String permlink = bundle.getString(Constants.EXTRAA_KEY_POST_PERMLINK);
      final String parentPermlink = bundle.getString(Constants.EXTRAA_KEY_PARENT_PERMLINK, "");
      String notifId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_NOTIFICATION_ID, null);
      if (parentPermlink.length() > 0) {
        enableParentPostButton(parentPermlink);
      }
      if (notifId != null) {
        FirebaseNotificationStore.markAsRead(notifId);
      }
      showFeedLoading(true);
      requestSingleFeed(author, permlink);
    }
  }

  private void enableParentPostButton(final String parentPermlink) {
    if (gotoParentBtn != null) {
      gotoParentBtn.setVisibility(View.VISIBLE);
      gotoParentBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String me = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
          if (me.length() > 0) {
            openDetailsPage(me, parentPermlink);
          }
        }
      });
    }
  }

  private void disableParentPostButton() {
    if (gotoParentBtn != null) {
      gotoParentBtn.setVisibility(GONE);
    }
  }

  private void openDetailsPage(String author, String permlink) {
    Intent intent = new Intent(this, DetailedActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_POST_AUTHOR, author);
    bundle.putString(Constants.EXTRAA_KEY_POST_PERMLINK, permlink);
    intent.putExtras(bundle);
    startActivity(intent);
  }

  private void bindPostValues(Feed feed) {
    this.post = feed;
    showFeedLoading(false);
    if (post.getDepth() == 0) {
      disableParentPostButton();
    }
    requestReplies();
    ImageHandler.loadCircularImage(this, feedOwnerPic,
      String.format(getResources().getString(R.string.steem_user_profile_pic_format), post.getAuthor()));
    feedOwnerTitle.setText(post.getAuthor());
    feedOwnerSubtitle.setText(
      String.format(getResources().getString(R.string.post_subtitle_format),
        MomentsUtils.getFormattedTime(post.getCreatedAt())));
    String title = post.getTitle();
    if (title != null && title.length() > 0) {
      postTitle.setVisibility(VISIBLE);
      postTitle.setText(title);
    }
    updateVotersPeekView(feed.getActiveVoters());
    renderMarkdown(post.getBody());
    ImageHandler.loadCircularImage(this, commentCreaterAvatar,
      String.format(getResources().getString(R.string.steem_user_profile_pic_format),
        HaprampPreferenceManager.getInstance().getCurrentSteemUsername()));
    bindVotes(post.getVoters(), post.getPermlink());
    setSteemEarnings(post);
    setCommentCount(post.getChildren());
    attachListenersOnStarView();
    setCommunities(post.getTags());
  }

  private void showFeedLoading(boolean loading) {
    if (loading) {
      detailsActivityCover.setVisibility(VISIBLE);
      feedLoadingProgressBar.setVisibility(VISIBLE);
    } else {
      detailsActivityCover.setVisibility(GONE);
      feedLoadingProgressBar.setVisibility(GONE);
    }
  }

  private void updateVotersPeekView(List<Voter> voters) {
    if (votersPeekView != null) {
      votersPeekView.setVoters(voters);
    }
  }

  private void navigateToUserProfile() {
    Intent intent = new Intent(this, ProfileActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, post.getAuthor());
    startActivity(intent);
  }

  private void bindVotes(List<Voter> votersList, String permlink) {
    starView.setData(votersList, permlink)
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

  private void showPopup() {
    int menu_res_id = R.menu.popup_post;
    String currentLoggedInUser = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, R.style.PopupMenuOverlapAnchor);
    PopupMenu popup = new PopupMenu(contextThemeWrapper, overflowBtn);
    //customize menu items
    //add Share
    popup.getMenu().add(PostMenu.Share);
    if (mRebloggers != null) {
      if (!mRebloggers.contains(currentLoggedInUser) && !currentLoggedInUser.equals(post.getAuthor())) {
        //add repost option
        popup.getMenu().add(PostMenu.Repost);
      }
    }
    popup.getMenuInflater().inflate(menu_res_id, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getTitle().equals(PostMenu.Share)) {
          ShareUtils.shareMixedContent(DetailedActivity.this, post);
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
    new AlertDialog.Builder(this)
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
        Toast.makeText(DetailedActivity.this, "Reposted", Toast.LENGTH_LONG).show();
      }

      @Override
      public void failedToRepost() {
        showProgressDialog(false, "");
        Toast.makeText(DetailedActivity.this, "Failed to Repost", Toast.LENGTH_LONG).show();
      }
    });
    steemRePoster.repost(post.getAuthor(), post.getPermlink());
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

  private void requestSingleFeed(String author, String permlink) {
    dataStore.requestSingleFeed(author, permlink, new SinglePostCallback() {
      @Override
      public void onPostFetched(Feed feed) {
        bindPostValues(feed);
        showFeedLoading(false);
      }

      @Override
      public void onPostFetchError(String err) {

      }
    });
  }

  private void renderMarkdown(String body) {
    body = RegexUtils.getHtmlContent(body);
    webView.setWebChromeClient(new WebChromeClient());
    webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
    webView.setWebChromeClient(new WebChromeClient());
    webView.getSettings().setAllowFileAccess(true);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null) {
          if (url.matches(Constants.URL_REGEX)) {
            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
          }
        } else {
          return true;
        }
        return true;
      }
    });
    // webView.getSettings().setLoadWithOverviewMode(true);
    webView.loadDataWithBaseURL("file:///android_asset/",
      "<link rel=\"stylesheet\" type=\"text/css\" href=\"md_theme.css\" />" + body,
      "text/html; charset=utf-8",
      "utf-8",
      null);
  }

  @Override
  public void onCommentCreateProcessing() {
  }

  @Override
  public void onCommentCreated() {
    hideProgress();
    requestReplies();
  }

  @Override
  public void onCommentCreateFailed() {
    hideProgress();
  }

  private void hideProgress() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void requestReplies() {
    dataStore.requestComments(this.post.getAuthor(), this.post.getPermlink(), this);
  }

  private void setCommentCount(int count) {
    if (commentCount != null) {
      commentCount.setText(String.valueOf(count));
    }
  }

  private void setTypefaces() {
  }

  private void attachListenersOnStarView() {
    starView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (ConnectionUtils.isConnected(DetailedActivity.this)) {
          starView.onStarIndicatorTapped();
        } else {
          Toast.makeText(DetailedActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
      }
    });

    starView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (ConnectionUtils.isConnected(DetailedActivity.this)) {
          starView.onStarIndicatorLongPressed();
        } else {
          Toast.makeText(DetailedActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
        return true;
      }
    });
  }

  private void performVoteOnSteem(final int vote) {
    starView.voteProcessing();
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
          post.getAuthor(),
          post.getPermlink(),
          String.valueOf(vote), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  updatePostFromBlockchain();
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
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
          post.getAuthor(),
          post.getPermlink(),
          String.valueOf(0), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  updatePostFromBlockchain();
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
    dataStore.requestSingleFeed(post.getAuthor(), post.getPermlink(), new SinglePostCallback() {
      @Override
      public void onPostFetched(Feed feed) {
        if (feed != null) {
          bindPostValues(feed);
        }
      }

      @Override
      public void onPostFetchError(String err) {
      }
    });
  }

  private void attachListener() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    starView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        starView.onStarIndicatorTapped();
      }
    });
    moreCommentsCaption.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToCommentsPage();
      }
    });
    sendButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        postComment();
      }
    });
    commentCount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToCommentsPage();
      }
    });
    commentBtnContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToCommentsPage();
      }
    });
    shareBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ShareUtils.shareMixedContent(DetailedActivity.this, post);
      }
    });
    overflowBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showPopup();
      }
    });
    feedOwnerPic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToUserProfile();
      }
    });
    feedOwnerTitle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        navigateToUserProfile();
      }
    });

    votersPeekView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openVotersList();
      }
    });

    commentInputBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().trim().length() > 0) {
          sendButton.setTextColor(Color.parseColor("#ff6b95"));
          sendButton.setEnabled(true);
        } else {
          sendButton.setTextColor(Color.parseColor("#8eff6b95"));
          sendButton.setEnabled(false);
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  private void openVotersList() {
    Intent intent = new Intent(this, VotersListActivity.class);
    intent.putParcelableArrayListExtra(VotersListActivity.EXTRA_USER_LIST, post.getVoters());
    startActivity(intent);
  }

  private void castingVoteFailed() {
    if (starView != null) {
      starView.failedToCastVote();
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

  private void setCommunities(List<String> communities) {
    if (communities == null)
      return;
    ArrayList<String> addedCommunity = new ArrayList<>();
    StringBuilder hashtags = new StringBuilder();
    for (int i = 0; i < communities.size(); i++) {
      String title = CommunityUtils.getCommunityTitleFromTag(communities.get(i));
      String tag = communities.get(i);
      if (Communities.doesCommunityExists(title) && !addedCommunity.contains(title)) {
        addedCommunity.add(title);
      } else {
        if (!tag.equals("hapramp")) {
          hashtags.append("<b>  #</b>")
            .append(CommunityUtils.getCommunityTitleFromTag(tag))
            .append("  ");
        }
      }
    }
    communityStripeView.setCommunities(communities);
    hashtagsView.setText(Html.fromHtml(hashtags.toString()));
  }

  private void setSteemEarnings(Feed feed) {
    try {
      double pendingPayoutValue = Double.parseDouble(feed.getPendingPayoutValue().split(" ")[0]);
      double totalPayoutValue = Double.parseDouble(feed.getTotalPayoutValue().split(" ")[0]);
      double curatorPayoutValue = Double.parseDouble(feed.getCuratorPayoutValue().split(" ")[0]);
      double maxAcceptedValue = Double.parseDouble(feed.getMaxAcceptedPayoutValue().split(" ")[0]);
      String briefPayoutValueString;
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
      if (maxAcceptedValue == 0) {
        payoutValue.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
      } else {
        payoutValue.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
      }
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void navigateToCommentsPage() {
    Intent intent = new Intent(DetailedActivity.this, CommentsActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_POST_AUTHOR, post.getAuthor());
    intent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK, post.getPermlink());
    startActivity(intent);
  }

  private void postComment() {
    String cmnt = commentInputBox.getText().toString().trim();
    commentInputBox.setText("");
    if (cmnt.length() > 2) {
      steemCommentCreator.createComment(cmnt, post.getAuthor(), post.getPermlink());
    } else {
      Toast.makeText(this, "Comment Too Short!!", Toast.LENGTH_LONG).show();
      return;
    }
    //add temp comment to view
    CommentModel commentModel = new CommentModel();
    commentModel.setAuthor(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    commentModel.setBody(cmnt);
    commentModel.setCreatedAt(MomentsUtils.getCurrentTime());
    comments.add(0, commentModel);
    addAllCommentsToView(comments);
  }

  @Override
  public void onCommentsFetching() {
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(VISIBLE);
    }
  }

  @Override
  public void onCommentsAvailable(ArrayList<CommentModel> comments) {
    this.comments = comments;
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(GONE);
    }
    addAllCommentsToView(comments);
  }

  private void addAllCommentsToView(List<CommentModel> discussions) {
    commentsViewContainer.removeAllViews();
    int commentCount = discussions.size();
    if (commentCount == 0) {
      emptyCommentsCaption.setText("No Comments");
      emptyCommentsCaption.setVisibility(VISIBLE);
    } else {
      emptyCommentsCaption.setVisibility(GONE);
    }
    int range = commentCount > 3 ? 3 : discussions.size();
    for (int i = 0; i < range; i++) {
      addCommentToView(discussions.get(i), i);
    }
    if (commentCount > 3) {
      moreCommentsCaption.setVisibility(VISIBLE);
    }
  }

  private void addCommentToView(CommentModel comment, final int index) {
    CommentsItemView commentsItemView = new CommentsItemView(this);
    commentsItemView.setCommenttActionListener(new CommentsItemView.CommentActionListener() {
      @Override
      public void onCommentDeleted(int itemIndex) {
        removeCommentAt(itemIndex);
      }
    });
    commentsItemView.setComment(comment);
    commentsItemView.setItemIndex(index);
    commentsViewContainer.addView(commentsItemView, index,
      new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  private void removeCommentAt(int index) {
    try {
      if (commentsViewContainer != null) {
        commentsViewContainer.removeViewAt(index);
      }
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onCommentsFetchError(String error) {
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(GONE);
    }
    if (emptyCommentsCaption != null) {
      emptyCommentsCaption.setText(R.string.comment_fetch_error_text);
    }
  }
}
