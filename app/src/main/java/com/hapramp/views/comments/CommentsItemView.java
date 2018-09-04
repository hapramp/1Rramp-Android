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
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.models.CommentModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.SteemVoterFetcher;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.NestedCommentActivity;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.views.extraa.StarView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_CONTENT;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_PARENT_AUTHOR;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_PARENT_PERMLINK;
import static com.hapramp.ui.activity.NestedCommentActivity.EXTRA_TIMESTAMP;
import static com.hapramp.utils.VoteUtils.checkForMyVote;
import static com.hapramp.utils.VoteUtils.getMyVotePercent;
import static com.hapramp.utils.VoteUtils.getNonZeroVoters;
import static com.hapramp.utils.VoteUtils.getVotePercentSum;

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
  TextView payoutBtn;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.starView)
  StarView starView;
  @BindView(R.id.starAndPayoutContainer)
  RelativeLayout starAndPayoutContainer;
  @BindView(R.id.moreReplies)
  TextView moreReplies;
  @BindView(R.id.replyBtn)
  ImageView replyBtn;
  @BindView(R.id.replyBtnContainer)
  RelativeLayout replyBtnContainer;
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

  public CommentsItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    mHandler = new Handler();
    steemVoterFetcher = new SteemVoterFetcher(context);
    steemVoterFetcher.setSteemVoterFetchCallback(this);
    SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
    instanceBuilder.setAcessToken(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    steemConnect = instanceBuilder.build();
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
        briefPayoutValueString = String.format(Locale.US, "$%1$.3f", pendingPayoutValue);
      } else {
        //cashed out
        briefPayoutValueString = String.format(Locale.US, "$%1$.3f", totalPayoutValue + curatorPayoutValue);
      }
      payoutValue.setText(briefPayoutValueString);
    }
    catch (Exception e) {
      Crashlytics.log(e.toString());
    }
  }

  private void attachListeners() {
    if (!author.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      replyBtnContainer.setVisibility(VISIBLE);
      replyBtnContainer.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToNestedCommentPage();
        }
      });
    } else {
      replyBtnContainer.setVisibility(GONE);
    }
    moreReplies.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToNestedCommentPage();
      }
    });
    attachListenersOnStarView();
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
    starView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (ConnectionUtils.isConnected(mContext)) {
          starView.onStarIndicatorTapped();
        } else {
          Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
      }
    });

    starView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (ConnectionUtils.isConnected(mContext)) {
          starView.onStarIndicatorLongPressed();
        } else {
          Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
        return true;
      }
    });
  }

  private void castingVoteFailed() {
    if (starView != null) {
      starView.failedToCastVote();
    }
  }

  private void voteDeleteFailed() {
    if (starView != null) {
      starView.failedToDeleteVoteFromServer();
    }
  }

  @Override
  public void onVotersFetched(List<Voter> voters) {
    bindVotes(voters, permlink);
  }

  private void bindVotes(List<Voter> votes, String permlink) {
    long votePercentSum = getVotePercentSum(votes);
    boolean amIVoted = checkForMyVote(votes);
    long myVotePercent = amIVoted ? getMyVotePercent(votes) : 0;
    long totalVotes = getNonZeroVoters(votes);
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
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()
          , author, permlink, String.valueOf(vote), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              // addMeAsVoter(vote);
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
        steemConnect.vote(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()
          , author,
          permlink, String.valueOf(0), new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              //removeMeFromVoterList();
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

  private void castingVoteSuccess() {
    if (starView != null) {
      starView.castedVoteSuccessfully();
    }
//    fetchUpdatedBalance();
  }

  private void voteDeleteSuccess() {
    if (starView != null) {
      starView.deletedVoteSuccessfully();
    }
//    fetchUpdatedBalance();
  }

  @Override
  public void onVotersFetchError() {

  }

}
