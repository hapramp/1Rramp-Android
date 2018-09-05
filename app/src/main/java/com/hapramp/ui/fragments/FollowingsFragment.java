package com.hapramp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.FollowingsCallback;
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
public class FollowingsFragment extends Fragment implements
  UserItemView.FollowStateChangeListener,
  FollowingsCallback {
  @BindView(R.id.followingsRv)
  RecyclerView followingsRecyclerView;
  Unbinder unbinder;
  private String username;
  private UserRecyclerAdapter userRecyclerAdapter;
  private DataStore dataStore;
  private Context mContext;
  private String lastUser;
  private boolean isThisInitialFetch;
  private UserItemView.FollowStateChangeListener followStateChangeListener;

  public FollowingsFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_followings, container, false);
    unbinder = ButterKnife.bind(this, view);
    collectUsername();
    initFollowingsApi();
    initRecyclerView();
    fetchFollowingsList(null);
    return view;
  }

  private void collectUsername() {
    Bundle bundle = getArguments();
    username = bundle.getString(EXTRA_KEY_USERNAME,
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
  }

  private void initFollowingsApi() {
    dataStore = new DataStore();
  }

  private void initRecyclerView() {
    followingsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    userRecyclerAdapter = new UserRecyclerAdapter(mContext);
    userRecyclerAdapter.setFollowStateChangeListener(this);
    followingsRecyclerView.setAdapter(userRecyclerAdapter);
    userRecyclerAdapter.setOnLoadMoreUserLoadListener(new UserRecyclerAdapter.OnLoadMoreUserLoadListener() {
      @Override
      public void onLoadMore() {
        fetchFollowingsList(lastUser);
      }
    });
  }

  private void fetchFollowingsList(String startFrom) {
    isThisInitialFetch = (startFrom == null);
    dataStore.requestFollowings(username, startFrom, this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onFollowStateChanged() {
    fetchFollowingsList(null);
    if (followStateChangeListener != null) {
      followStateChangeListener.onFollowStateChanged();
    }
  }

  public void setFollowStateChangeListener(UserItemView.FollowStateChangeListener followStateChangeListener) {
    this.followStateChangeListener = followStateChangeListener;
  }

  public void refreshData() {
    fetchFollowingsList(null);
  }

  @Override
  public void onFollowingsAvailable(ArrayList<String> followings) {
    if (isThisInitialFetch) {
      userRecyclerAdapter.setUsers(followings);
    } else {
      followings.remove(0);
      userRecyclerAdapter.appendUsers(followings);
    }
    if (followings.size() > 0) {
      lastUser = followings.get(followings.size() - 1);
    }
  }

  @Override
  public void onFollowingsFetchError(String err) {

  }
}
