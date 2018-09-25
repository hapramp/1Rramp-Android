package com.hapramp.views.comments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
  private Context mContext;
  private Handler mHandler;
  private SteemVoterFetcher steemVoterFetcher;
  private String author;
  private String permlink;
  private SteemConnect steemConnect;
  boolean amIVoted = false;
  int oldVote = 0;
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

    ImageHandler.loadCircularImage(mContext
      , commentOwnerPic,
      String.format(getResources().getString(R.string.steem_user_profile_pic_format),
        comment.getAuthor()));
    requestVoters(comment.getAuthor(), comment.getPermlink());
    commentOwnerUsername.setText(comment.getAuthor());
    timestampTv.setText(MomentsUtils.getFormattedTime(comment.getCreatedAt()));
    commentContent.setText(comment.getBody());
    if (comment.getChildren() > 0) {
      moreReplies.setVisibility(VISIBLE);
      moreReplies.setText(String.format(Locale.US, "VIEW %d %s", comment.getChildren(),
        (comment.getChildren() > 1 ? "REPLIES" : "REPLY")));
    } else {
      moreReplies.setVisibility(GONE);
    }
    setSteemEarnings(comment);
    attachListeners();
  }

  private void requestVoters(String author, String permlink) {
    if (permlink != null)
      steemVoterFetcher.requestVoters(author, permlink);
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
            performVoteOnSteem(5);
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

  private void setVoteCount(int count) {
    upvoteCount.setText(String.valueOf(count));
  }

  private void setVoteState(boolean voted) {
    if (voted) {
      upvoteBtn.setImageResource(R.drawable.like_filled);
    } else {
      upvoteBtn.setImageResource(R.drawable.like);
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

  @Override
  public void onVotersFetchError() {

  }
}
