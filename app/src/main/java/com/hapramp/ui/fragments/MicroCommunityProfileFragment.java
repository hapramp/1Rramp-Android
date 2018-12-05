package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MicroCommunityProfileFragment extends Fragment {
  public static final String EXTRA_HASHTAG = "hashtag";
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
  private String mHashtag;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.micro_community_profile_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    mHashtag = getArguments().getString(EXTRA_HASHTAG,"");
    communityHashtagTv.setText(String.format("#%s", mHashtag));
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
