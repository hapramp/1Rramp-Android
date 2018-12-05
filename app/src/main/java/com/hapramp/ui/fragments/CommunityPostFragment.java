package com.hapramp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.R;

public class CommunityPostFragment extends Fragment {
  private Context mContext;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.community_post_fragment,container,false);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }
}
