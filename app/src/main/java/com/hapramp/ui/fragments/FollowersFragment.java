package com.hapramp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.api.FollowersApi;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.adapters.UserRecyclerAdapter;
import com.hapramp.views.UserItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hapramp.ui.activity.FollowListActivity.EXTRA_KEY_USERNAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment implements FollowersApi.FollowerCallback, UserItemView.FollowStateChangeListener {
  @BindView(R.id.followersRv)
  RecyclerView followersRV;
  Unbinder unbinder;
  private String username;
  private UserRecyclerAdapter userRecyclerAdapter;
  private FollowersApi followersApi;
  private Context mContext;
  private String lastUser;
  private boolean isThisInitialFetch;
  private UserItemView.FollowStateChangeListener followStateChangeListener;

  public FollowersFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_followers, container, false);
    unbinder = ButterKnife.bind(this, view);
    collectUsername();
    initFollowersApi();
    initRecyclerView();
    fetchFollowersList(null);
    return view;
  }

  private void collectUsername() {
    Bundle bundle = getArguments();
    username = bundle.getString(EXTRA_KEY_USERNAME,
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
  }

  private void initFollowersApi() {
    followersApi = new FollowersApi(mContext);
    followersApi.setFollowingCallback(this);
  }

  private void initRecyclerView() {
    followersRV.setLayoutManager(new LinearLayoutManager(mContext));
    userRecyclerAdapter = new UserRecyclerAdapter(mContext);
    followersRV.setAdapter(userRecyclerAdapter);
    userRecyclerAdapter.setFollowStateChangeListener(this);
    userRecyclerAdapter.setOnLoadMoreUserLoadListener(new UserRecyclerAdapter.OnLoadMoreUserLoadListener() {
      @Override
      public void onLoadMore() {
        fetchFollowersList(lastUser);
      }
    });
  }

  private void fetchFollowersList(String startFrom) {
    isThisInitialFetch = (startFrom == null);
    followersApi.requestFollowers(username, startFrom);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onFollowers(ArrayList<String> followers) {
    if (isThisInitialFetch) {
      userRecyclerAdapter.setUsers(followers);
    } else {
      followers.remove(0);
      userRecyclerAdapter.appendUsers(followers);
    }
    if (followers.size() > 0) {
      lastUser = followers.get(followers.size() - 1);
    }
  }

  @Override
  public void onError() {

  }

  public void setFollowStateChangeListener(UserItemView.FollowStateChangeListener followStateChangeListener) {
    this.followStateChangeListener = followStateChangeListener;
  }

  @Override
  public void onFollowStateChanged() {
    fetchFollowersList(null);
    if (followStateChangeListener != null) {
      followStateChangeListener.onFollowStateChanged();
    }
  }

  public void refreshData() {
    //Log.d("FollowersFragment", "refreshing data");
    fetchFollowersList(null);
  }
}
