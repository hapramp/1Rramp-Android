package com.hapramp.views.comments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.models.CommentModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.SteemVoterFetcher;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.NestedCommentActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.RegexUtils;
import com.hapramp.utils.TextViewImageGetter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_CONTENT;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_PARENT_AUTHOR;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_PARENT_PERMLINK;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_TIMESTAMP;
import static com.hapramp.utils.VoteUtils.checkForMyVote;
import static com.hapramp.utils.VoteUtils.getNonZeroVoters;

public class CommentsItemView extends FrameLayout implements
  SteemVoterFetcher.SteemVotersFetchCallback {
  @BindView(R.id.comment_owner_pic)
  ImageView commentOwnerPic;
  @BindView(R.id.comment_owner_username)
  TextView commentOwnerUsername;
  @BindView(R.id.dot_separator)
  TextView dotSeparator;
  @BindView(R.id.timestamp)
  TextView timestampTv;
  @BindView(R.id.comment_content)
  TextView commentContent;
  @BindView(R.id.payoutBtn)
  ImageView payoutBtn;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.upvote)
  ImageView upvoteBtn;
  @BindView(R.id.upvote_count)
  TextView upvoteCount;
  @BindView(R.id.starAndPayoutContainer)
  RelativeLayout starAndPayoutContainer;
  @BindView(R.id.moreReplies)
  TextView moreReplies;
  @BindView(R.id.replyBtn)
  ImageView replyBtn;
  @BindView(R.id.popupMenuDots)
  ImageView popUpMenuDots;
  boolean amIVoted = false;
  int oldVote = 0;
  private Context mContext;
  private Handler mHandler;
  private SteemVoterFetcher steemVoterFetcher;
  private String author;
  private String permlink;
  private SteemConnect steemConnect;
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
  private String timestamp;
  private String content;
  private CommentActionListener commentActionListener;
  private int mItemIndex;

  public CommentsItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    mHandler = new Handler();
    steemVoterFetcher = new SteemVoterFetcher(context);
    steemVoterFetcher.setSteemVoterFetchCallback(this);
    steemConnect = SteemConnectUtils.getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    View view = LayoutInflater.from(context).inflate(R.layout.comment_item_view, this);
    ButterKnife.bind(this, view);
  }

  public CommentsItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setComment(CommentModel comment) {
    this.author = comment.getAuthor();
    this.permlink = comment.getPermlink();
    this.timestamp = comment.getCreatedAt();
    this.content = comment.getBody();
    commentContent.setTextIsSelectable(true);
    ImageHandler.loadCircularImage(mContext
      , commentOwnerPic,
      String.format(getResources().getString(R.string.steem_user_profile_pic_format),
        comment.getAuthor()));
    requestVoters(comment.getAuthor(), comment.getPermlink());
    commentOwnerUsername.setText(comment.getAuthor());
    timestampTv.setText(MomentsUtils.getFormattedTime(comment.getCreatedAt()));
    if (comment.getChildren() > 0) {
      moreReplies.setVisibility(VISIBLE);
      moreReplies.setText(String.format(Locale.US, "View %d %s", comment.getChildren(),
        (comment.getChildren() > 1 ? "replies" : "reply")));
    } else {
      moreReplies.setVisibility(GONE);
    }
    setSteemEarnings(comment);
    attachListeners();

    Spannable html;
    String htmlContent = RegexUtils.getHtmlContent(comment.getBody());
    TextViewImageGetter imageGetter = new TextViewImageGetter(mContext, commentContent);
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      html = (Spannable) Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
    } else {
      html = (Spannable) Html.fromHtml(htmlContent, imageGetter, null);
    }
    commentContent.setText(html);
    commentContent.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private void setSteemEarnings(CommentModel commentModel) {
    try {
      double pendingPayoutValue = Double.parseDouble(commentModel.getPendingPayoutValue().split(" ")[0]);
      double totalPayoutValue = Double.parseDouble(commentModel.getTotalPayoutValue().split(" ")[0]);
      double curatorPayoutValue = Double.parseDouble(commentModel.getCuratorPayoutValue().split(" ")[0]);
      String briefPayoutValueString;
      if (pendingPayoutValue > 0) {
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", pendingPayoutValue);
      } else {
        //cashed out
        briefPayoutValueString = String.format(Locale.US, "%1$.3f", totalPayoutValue + curatorPayoutValue);
      }
      payoutValue.setText(briefPayoutValueString);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void attachListeners() {
    if (!author.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      replyBtn.setVisibility(VISIBLE);
      replyBtn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToNestedCommentPage();
        }
      });
    } else {
      replyBtn.setVisibility(GONE);
    }
    moreReplies.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToNestedCommentPage();
      }
    });
    attachListenersOnStarView();
    commentOwnerPic.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToUserProfile(author);
      }
    });
    commentOwnerUsername.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToUserProfile(author);
      }
    });
  }

  private void navigateToUserProfile(String username) {
    Intent intent = new Intent(mContext, ProfileActivity.class);
    intent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, username);
    mContext.startActivity(intent);
  }

  private void navigateToNestedCommentPage() {
    Intent intent = new Intent(mContext, NestedCommentActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(EXTRA_PARENT_AUTHOR, author);
    bundle.putString(EXTRA_PARENT_PERMLINK, permlink);
    bundle.putString(EXTRA_CONTENT, content);
    bundle.putString(EXTRA_TIMESTAMP, timestamp);
    intent.putExtras(bundle);
    mContext.startActivity(intent);
  }

  private void attachListenersOnStarView() {
    upvoteBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (ConnectionUtils.isConnected(mContext)) {
          if (amIVoted) {
            deleteVoteOnSteem();
          } else {
            performVoteOnSteem(10000);
          }
        } else {
          Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  private void deleteVoteOnSteem() {
    setVoteCount(oldVote - 1);
    setVoteState(false);
    new Thread() {
      @Override
      public void run() {
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()
          , author,
          permlink, String.valueOf(0), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
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

  private void performVoteOnSteem(final int vote) {
    setVoteCount(oldVote + 1);
    setVoteState(true);
    new Thread() {
      @Override
      public void run() {
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()
          , author, permlink, String.valueOf(vote), new SteemConnectCallback() {
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

  private void requestVoters(String author, String permlink) {
    if (permlink != null) {
      steemVoterFetcher.requestVoters(author, permlink);
    }
  }

  private void voteDeleteSuccess() {
    amIVoted = false;
    oldVote = oldVote - 1;
    setVoteCount(oldVote);
  }

  private void castingVoteSuccess() {
    amIVoted = true;
    oldVote = oldVote + 1;
    setVoteCount(oldVote);
  }

  @Override
  public void onVotersFetched(List<Voter> voters) {
    bindVotes(voters, permlink);
  }

  private void bindVotes(List<Voter> votes, String permlink) {
    amIVoted = checkForMyVote(votes);
    oldVote = (int) getNonZeroVoters(votes);
    this.permlink = permlink;
    setVoteCount(oldVote);
    setVoteState(amIVoted);
  }

  private void setVoteCount(int count) {
    upvoteCount.setText(String.valueOf(count));
    if (count == 0 && author.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      showOptionMenuEnabled(true);
    } else {
      showOptionMenuEnabled(false);
    }
  }

  private void setVoteState(boolean voted) {
    if (voted) {
      upvoteBtn.setImageResource(R.drawable.like_filled);
    } else {
      upvoteBtn.setImageResource(R.drawable.like);
    }
  }

  private void showOptionMenuEnabled(boolean show) {
    if (popUpMenuDots != null) {
      if (show) {
        popUpMenuDots.setVisibility(VISIBLE);
        popUpMenuDots.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            showPopup();
          }
        });
      } else {
        popUpMenuDots.setVisibility(GONE);
        popUpMenuDots.setOnClickListener(null);
      }
    }
  }

  private void showPopup() {
    int menu_res_id = R.menu.post_menu_delete;
    ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
    PopupMenu popup = new PopupMenu(contextThemeWrapper, popUpMenuDots);
    popup.getMenuInflater().inflate(menu_res_id, popup.getMenu());
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.comment_delete) {
          showAlertDialogForDelete();
          return true;
        }
        return false;
      }
    });
    popup.show();
  }

  private void showAlertDialogForDelete() {
    new AlertDialog.Builder(mContext)
      .setTitle("Delete Comment")
      .setMessage("Do you want to Delete ? ")
      .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          deleteThisComment();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void deleteThisComment() {
    if (commentActionListener != null) {
      commentActionListener.onCommentDeleted(mItemIndex);
    }
    new Thread() {
      @Override
      public void run() {
        SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager
            .getInstance().getSC2AccessToken());
        steemConnect.delete(author, permlink, new SteemConnectCallback() {
          @Override
          public void onResponse(String response) {
          }

          @Override
          public void onError(final SteemConnectException e) {
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                Log.d("CommentDelete", e.toString());
                Toast.makeText(mContext, "Error occurred while deleting comment.", Toast.LENGTH_LONG).show();
              }
            });
          }
        });
      }
    }.start();
  }

  @Override
  public void onVotersFetchError() {

  }

  private void castingVoteFailed() {
    amIVoted = false;
    setVoteCount(oldVote);
    setVoteState(false);
  }

  private void voteDeleteFailed() {
    amIVoted = true;
    setVoteCount(oldVote);
    setVoteState(true);
  }

  public void setCommenttActionListener(CommentActionListener commenttActionListener) {
    this.commentActionListener = commenttActionListener;
  }

  public void setItemIndex(int index) {
    this.mItemIndex = index;
  }

  public interface CommentActionListener {
    void onCommentDeleted(int itemIndex);
  }
}
