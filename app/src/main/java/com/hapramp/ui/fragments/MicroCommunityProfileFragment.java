package com.hapramp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.AppServerUserModel;
import com.hapramp.models.MicroCommunity;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MicroCommunityProfileFragment extends Fragment {
  public static final String EXTRA_COMMUNITY_MODEL = "com_model";
  @BindView(R.id.community_profile_image)
  ImageView communityProfileImage;
  @BindView(R.id.community_hashtag)
  TextView communityHashtagTv;
  @BindView(R.id.community_join_button)
  TextView communityJoinButton;
  @BindView(R.id.community_description)
  TextView communityDescription;
  Unbinder unbinder;
  private Context mContext;
  private MicroCommunity mCommunity;
  private boolean mHasJoined = false;
  private ProgressDialog progressDialog;
  private String TICK_TEXT = "\u2713";

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.micro_community_profile_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    mCommunity = getArguments().getParcelable(EXTRA_COMMUNITY_MODEL);
    communityHashtagTv.setText(String.format("#%s", mCommunity.getTag()));
    communityDescription.setText(mCommunity.getDescription());
    ImageHandler.loadCircularImage(mContext, communityProfileImage, mCommunity.getImageUrl());
    fetchUserInfo();
    attachListeners();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void fetchUserInfo() {
    if (!ConnectionUtils.isConnected(mContext)) {
      toast("No Internet connection!");
      return;
    }

    AppServerUserModel userModel = HaprampPreferenceManager.getInstance().getCurrentAppserverUser();
    if (userModel != null) {
      invalidateJoinButton(userModel);
      return;
    }

    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    RetrofitServiceGenerator.getService().fetchUserByUsername(username).enqueue(new Callback<AppServerUserModel>() {
      @Override
      public void onResponse(Call<AppServerUserModel> call, Response<AppServerUserModel> response) {
        if (response.isSuccessful()) {
          HaprampPreferenceManager.getInstance().saveCurrentAppServerUserAsJson(new Gson().toJson(response.body()));
          invalidateJoinButton(response.body());
        }
      }

      @Override
      public void onFailure(Call<AppServerUserModel> call, Throwable t) {

      }
    });
  }

  private void attachListeners() {
    communityJoinButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mHasJoined) {
          showLeaveAlert();
        } else {
          joinCommunity();
        }
      }
    });
  }

  private void toast(String msg) {
    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
  }

  private void invalidateJoinButton(AppServerUserModel appServerUserModel) {
    try {
      if (appServerUserModel != null) {
        mHasJoined = false;
        communityJoinButton.setEnabled(true);
        for (int i = 0; i < appServerUserModel.getMicroCommunities().size(); i++) {
          MicroCommunity microCommunity = appServerUserModel.getMicroCommunities().get(i);
          if (microCommunity.getTag().equals(mCommunity.getTag())) {
            mHasJoined = true;
          }
        }
        if (mHasJoined) {
          showLeaveButton();
        } else {
          showJoinButton();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showLeaveAlert() {
    Spanned msg = Html.fromHtml(String.format("You want to leave <b>#%s</b>", mCommunity.getTag()));
    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
      .setTitle("Are you sure?")
      .setMessage(msg)
      .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          leaveCommunity();
        }
      })
      .setNegativeButton("Cancel", null);
    builder.show();
  }

  private void joinCommunity() {
    showProgressDialog(true, "Joining... " + mCommunity.getTag());
    RetrofitServiceGenerator.getService().joinCommunity(mCommunity.getTag()).enqueue(new Callback<AppServerUserModel>() {
      @Override
      public void onResponse(Call<AppServerUserModel> call, Response<AppServerUserModel> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful()) {
          mHasJoined = true;
          showLeaveButton();
          toast("Congratulation! You are now part of " + mCommunity.getTag());
          updateUserCache(response.body());
        }
      }

      @Override
      public void onFailure(Call<AppServerUserModel> call, Throwable t) {
        showProgressDialog(false, "");
        toast("Failed to join");
      }
    });
  }

  private void showLeaveButton() {
    communityJoinButton.setText(String.format("%s Joined", TICK_TEXT));
  }

  private void showJoinButton() {
    communityJoinButton.setText("Join");
  }

  private void leaveCommunity() {
    showProgressDialog(true, "Leaving... " + mCommunity.getTag());
    RetrofitServiceGenerator.getService().leaveCommunity(mCommunity.getTag()).enqueue(new Callback<AppServerUserModel>() {
      @Override
      public void onResponse(Call<AppServerUserModel> call, Response<AppServerUserModel> response) {
        showProgressDialog(false, "");
        if (response.isSuccessful()) {
          mHasJoined = false;
          showJoinButton();
          toast("You left " + mCommunity.getTag());
          updateUserCache(response.body());
        }
      }

      @Override
      public void onFailure(Call<AppServerUserModel> call, Throwable t) {
        showProgressDialog(false, "");
        toast("Failed to leave");
      }
    });
  }

  private void showProgressDialog(boolean show, String message) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(mContext);
    }
    if (show) {
      progressDialog.setMessage(message);
      progressDialog.setCancelable(false);
      progressDialog.show();
    } else {
      progressDialog.dismiss();
    }
  }

  private void updateUserCache(AppServerUserModel userModel) {
    HaprampPreferenceManager.getInstance().saveCurrentAppServerUserAsJson(new Gson().toJson(userModel));
  }
}
