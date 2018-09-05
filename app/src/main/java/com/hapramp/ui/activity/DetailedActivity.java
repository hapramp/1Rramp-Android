package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
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

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CommentsCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.SteemCommentCreator;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.RegexUtils;
import com.hapramp.utils.ShareUtils;
import com.hapramp.utils.VoteUtils;
import com.hapramp.views.comments.CommentsItemView;
import com.hapramp.views.extraa.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;

public class DetailedActivity extends AppCompatActivity implements
  SteemCommentCreator.SteemCommentCreateCallback,
  CommentsCallback {
  @BindView(R.id.closeBtn)
  TextView closeBtn;
  @BindView(R.id.overflowBtn)
  TextView overflowBtn;
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
  @BindView(R.id.club3)
  TextView club3;
  @BindView(R.id.club2)
  TextView club2;
  @BindView(R.id.club1)
  TextView club1;
  @BindView(R.id.post_header_container)
  RelativeLayout postHeaderContainer;
  @BindView(R.id.markdownView)
  WebView webView;
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
  TextView sendCommentButton;
  @BindView(R.id.mockCommentParentView)
  RelativeLayout mockCommentParentView;
  @BindView(R.id.scroller)
  ScrollView scroller;
  @BindView(R.id.shadow)
  ImageView shadow;
  @BindView(R.id.commentBtn)
  TextView commentBtn;
  @BindView(R.id.commentCount)
  TextView commentCount;
  @BindView(R.id.payoutBtn)
  TextView hapcoinBtn;
  @BindView(R.id.payoutValue)
  TextView hapcoinsCount;
  @BindView(R.id.starView)
  StarView starView;
  @BindView(R.id.postMetaContainer)
  RelativeLayout postMetaContainer;
  @BindView(R.id.hashtags)
  TextView hashtagsTv;
  @BindView(R.id.details_activity_cover)
  View detailsActivityCover;
  @BindView(R.id.comment_btn_container)
  LinearLayout commentBtnContainer;
  @BindView(R.id.post_title)
  TextView postTitle;
  private Handler mHandler;
  private Feed post;
  private ProgressDialog progressDialog;
  private SteemCommentCreator steemCommentCreator;
  private List<CommentModel> comments = new ArrayList<>();
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
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_DETAILED_POST, null);
  }

  private void init() {
    mHandler = new Handler();
    dataStore = new DataStore();
    steemCommentCreator = new SteemCommentCreator();
    steemCommentCreator.setSteemCommentCreateCallback(this);
    progressDialog = new ProgressDialog(this);
    SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
    instanceBuilder.setAcessToken(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    steemConnect = instanceBuilder.build();
  }

  private void collectExtras() {
    post = getIntent().getExtras().getParcelable(Constants.EXTRAA_KEY_POST_DATA);
    bindPostValues();
  }

  private void attachListener() {
    closeBtn.setOnClickListener(new View.OnClickListener() {
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
    sendCommentButton.setOnClickListener(new View.OnClickListener() {
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
  }

  private void navigateToUserProfile() {
    Intent intent = new Intent(this, ProfileActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, post.getAuthor());
    startActivity(intent);
  }

  private void showPopup() {
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this,
      R.style.PopupMenuOverlapAnchor);
    PopupMenu popup = new PopupMenu(contextThemeWrapper, overflowBtn);
    popup.getMenuInflater().inflate(R.menu.popup_post, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        ShareUtils.shareMixedContent(DetailedActivity.this, post);
        return true;
      }
    });
    popup.show();
  }

  private void bindPostValues() {
    detailsActivityCover.setVisibility(View.GONE);
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
    renderMarkdown(post.getBody());
    ImageHandler.loadCircularImage(this, commentCreaterAvatar,
      String.format(getResources().getString(R.string.steem_user_profile_pic_format),
        HaprampPreferenceManager.getInstance().getCurrentSteemUsername()));
    bindVotes(post.getVoters(), post.getPermlink());
    setSteemEarnings(post.getPendingPayoutValue());
    setCommentCount(post.getChildren());
    attachListenersOnStarView();
    setCommunities(post.getTags());
  }

  private void requestReplies() {
    dataStore.requestComments(post.getAuthor(), post.getPermlink(), this);
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
        if (url != null && (url.matches(Constants.REGEX_URL))) {
          view.getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
          return true;
        } else {
          return false;
        }
      }
    });
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.loadDataWithBaseURL("file:///android_asset/",
      "<link rel=\"stylesheet\" type=\"text/css\" href=\"md_theme.css\" />" + body,
      "text/html; charset=utf-8",
      "utf-8",
      null);
  }

  @Override
  public void onCommentCreateProcessing() {
  }

  private void setCommentCount(int count) {
    if (commentCount != null) {
      commentCount.setText(String.format(getResources().getString(R.string.comment_format), count));
    }
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

  private void setTypefaces() {
    Typeface t = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
    closeBtn.setTypeface(t);
    overflowBtn.setTypeface(t);
    commentBtn.setTypeface(t);
    hapcoinBtn.setTypeface(t);
    sendCommentButton.setTypeface(t);
  }

  private void addAllCommentsToView(List<CommentModel> discussions) {
    commentsViewContainer.removeAllViews();
    int commentCount = discussions.size();
    if (commentCount == 0) {
      emptyCommentsCaption.setText("No Comments");
      emptyCommentsCaption.setVisibility(VISIBLE);
    } else {
      emptyCommentsCaption.setVisibility(View.GONE);
    }
    int range = commentCount > 3 ? 3 : discussions.size();
    for (int i = 0; i < range; i++) {
      addCommentToView(discussions.get(i), i);
    }
    if (commentCount > 3) {
      moreCommentsCaption.setVisibility(VISIBLE);
    }
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

  private void bindVotes(List<Voter> votes, String permlink) {
    long votePercentSum = VoteUtils.getVotePercentSum(votes);
    boolean amIVoted = VoteUtils.checkForMyVote(votes);
    long myVotePercent = amIVoted ? VoteUtils.getMyVotePercent(votes) : 0;
    long totalVotes = VoteUtils.getNonZeroVoters(votes);
    starView.setVoteState(
      new StarView.Vote(
        amIVoted,
        permlink,
        myVotePercent,
        totalVotes,
        votePercentSum))
      .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
        @Override
        public void onVoted(String full_permlink, int _vote) {
          performVoteOnSteem(_vote);
        }

        @Override
        public void onVoteDeleted(String full_permlink) {
          deleteVoteOnSteem();
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
                  Log.d("Vote", "un voted!!");
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

    List<CommunityModel> cm = new ArrayList<>();
    StringBuilder hashtags = new StringBuilder();
    for (int i = 0; i < communities.size(); i++) {
      if (Communities.doesCommunityExists(communities.get(i))) {
        cm.add(new CommunityModel("", "", communities.get(i),
          HaprampPreferenceManager.getInstance().getCommunityColorFromTag(communities.get(i)),
          HaprampPreferenceManager.getInstance().getCommunityNameFromTag(communities.get(i)),
          0
        ));
      } else {
        hashtags.append("<b>  #</b>")
          .append(communities.get(i))
          .append("  ");
      }
    }
    addCommunitiesToLayout(cm);
    hashtagsTv.setText(Html.fromHtml(hashtags.toString()));
  }

  private void addCommunitiesToLayout(List<CommunityModel> cms) {
    int size = cms.size();
    if (size > 0) {
      club1.setVisibility(VISIBLE);
      club1.setText(cms.get(0).getmName());
      club1.getBackground().setColorFilter(
        Color.parseColor(cms.get(0).getmColor()),
        PorterDuff.Mode.SRC_ATOP);
      if (size > 1) {
        club2.setVisibility(VISIBLE);
        club2.setText(cms.get(1).getmName());
        club2.getBackground().setColorFilter(
          Color.parseColor(cms.get(1).getmColor()),
          PorterDuff.Mode.SRC_ATOP);
        if (size > 2) {
          club3.setVisibility(VISIBLE);
          club3.setText(cms.get(2).getmName());
          club3.getBackground().setColorFilter(
            Color.parseColor(cms.get(2).getmColor()),
            PorterDuff.Mode.SRC_ATOP);
        }
      }
    }
  }

  private void setSteemEarnings(String payout) {
    if (hapcoinsCount != null && payout != null) {
      hapcoinsCount.setText(String.format(getResources().getString(R.string.hapcoins_format), payout.substring(0, payout.indexOf(' '))));
    }
  }

  @Override
  public void onCommentCreated() {
    hideProgress();
    requestReplies();
  }

  private void navigateToCommentsPage() {
    Intent intent = new Intent(DetailedActivity.this, CommentsActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_POST_AUTHOR, post.getAuthor());
    intent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK, post.getPermlink());
    startActivity(intent);
  }

  private void addCommentToView(CommentModel comment, int index) {
    CommentsItemView commentsItemView = new CommentsItemView(this);
    commentsItemView.setComment(comment);
    commentsViewContainer.addView(commentsItemView, index,
      new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
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
  public void whileWeAreFetchingComments() {
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(VISIBLE);
    }
  }

  @Override
  public void onCommentsAvailable(ArrayList<CommentModel> comments) {
    this.comments = comments;
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(View.GONE);
    }
    addAllCommentsToView(comments);
  }

  @Override
  public void onCommentsFetchError(String error) {
    if (commentLoadingProgressBar != null) {
      commentLoadingProgressBar.setVisibility(View.GONE);
    }
    if (emptyCommentsCaption != null) {
      emptyCommentsCaption.setText(R.string.comment_fetch_error_text);
    }
  }
}
