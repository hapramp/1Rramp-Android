package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.LeaderboardModel;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LeaderboardBar extends FrameLayout {
  @BindView(R.id.leader_board_icon)
  ImageView leaderBoardIcon;
  @BindView(R.id.view_all)
  TextView viewAll;
  @BindView(R.id.leader1_icon)
  ImageView leader1Icon;
  @BindView(R.id.leader1)
  RelativeLayout leader1;
  @BindView(R.id.leader2_icon)
  ImageView leader2Icon;
  @BindView(R.id.leader2)
  RelativeLayout leader2;
  @BindView(R.id.leader3_icon)
  ImageView leader3Icon;
  @BindView(R.id.leader3)
  RelativeLayout leader3;
  private Context mContext;

  // variable to store disposables
  private CompositeDisposable compositeDisposable;
  private List<LeaderboardModel.Winners> winners = new ArrayList<>();

  public LeaderboardBar(@NonNull Context context) {
    this(context, null);
  }

  public LeaderboardBar(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LeaderboardBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  /**
   * prepare views
   * and initializes objects
   *
   * @param context context of container component (Activity/Fragment)
   */
  private void init(Context context) {
    compositeDisposable = new CompositeDisposable();
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.leaderboard_bar, this);
    ButterKnife.bind(this, view);
    loadLeaders();
  }

  /**
   * fetch leaderboard
   */
  private void loadLeaders() {
    RetrofitServiceGenerator.getService().getLeaderboardList()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeWith(new SingleObserver<LeaderboardModel>() {
        @Override
        public void onSubscribe(Disposable d) {
          compositeDisposable.add(d);
        }

        @Override
        public void onSuccess(LeaderboardModel leaderboardModel) {
          handleLeaderBoardList(leaderboardModel);
        }

        @Override
        public void onError(Throwable e) {
        }
      });
  }

  /**
   * manage leaderboard repsonse
   *
   * @param leaderboardModel reponse from api
   */
  private void handleLeaderBoardList(LeaderboardModel leaderboardModel) {
    winners = leaderboardModel.getmWinners();
    sortWinners(winners);
    addLeadersToView();
  }

  private void sortWinners(List<LeaderboardModel.Winners> winners) {
    Collections.sort(winners, new Comparator<LeaderboardModel.Winners>() {
      @Override
      public int compare(LeaderboardModel.Winners winner1, LeaderboardModel.Winners winner2) {
        return (int) (winner2.getmScore() - winner1.getmScore());
      }
    });
  }

  /**
   * adds atmost 3 leaders to bar
   */
  private void addLeadersToView() {
    try {
      int size = winners.size();
      leader1.setVisibility(GONE);
      leader2.setVisibility(GONE);
      leader3.setVisibility(GONE);
      viewAll.setVisibility(GONE);

      if (size > 0) {
        LeaderboardModel.Winners leader1Info = winners.get(0);
        leader1.setVisibility(VISIBLE);
        loadImageTo(leader1Icon, avatarUrlOf(leader1Info.getmAuthor()));

        if (size > 1) {
          LeaderboardModel.Winners leader2Info = winners.get(1);
          leader2.setVisibility(VISIBLE);
          loadImageTo(leader2Icon, avatarUrlOf(leader2Info.getmAuthor()));

          if (size > 2) {
            LeaderboardModel.Winners leader3Info = winners.get(2);
            leader3.setVisibility(VISIBLE);
            loadImageTo(leader3Icon, avatarUrlOf(leader3Info.getmAuthor()));

            if (size > 3) {
              viewAll.setVisibility(VISIBLE);
            }
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * loads image
   *
   * @param imageView to load into
   * @param url       from image will be loaded
   */
  private void loadImageTo(ImageView imageView, String url) {
    ImageHandler.loadCircularImage(mContext, imageView, url);
  }

  /**
   * format user avatar image url
   *
   * @param username of avar
   * @return formatted username
   */
  private String avatarUrlOf(String username) {
    return String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
      username);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    compositeDisposable.dispose();
  }
}
