package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MicroCommunityContainerView extends FrameLayout {
  @BindView(R.id.tagsContainer)
  LinearLayout tagsContainer;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  private Context mContext;

  public MicroCommunityContainerView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_container_view, this);
    ButterKnife.bind(this, view);
    showLoadinProgressBar();
  }

  private void showLoadinProgressBar() {
    loadingProgressBar.setVisibility(VISIBLE);
    new android.os.Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        loadMicroCommunities();
      }
    }, 3000);
  }

  private void loadMicroCommunities() {
    ArrayList<String> names = new ArrayList<>();
    names.add("dotdesing");
    names.add("mytravelclub");
    names.add("photomojo");
    names.add("magicpics");
    names.add("dotdesing");
    names.add("mytravelclub");
    names.add("photomojo");
    names.add("magicpics");
    names.add("dotdesing");
    names.add("mytravelclub");
    names.add("photomojo");
    names.add("magicpics");
    loadingProgressBar.setVisibility(GONE);
    addMicrocommunitiesToView(names);
  }

  private void addMicrocommunitiesToView(ArrayList<String> microcoms) {
    try {
      tagsContainer.removeAllViews();
      TextView titileView = null;
      for (int i = 0; i < microcoms.size(); i++) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_item_view, null);
        titileView = view.findViewById(R.id.micro_community_tag);
        titileView.setText(String.format("#%s", microcoms.get(i)));
        tagsContainer.addView(view);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public MicroCommunityContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public MicroCommunityContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }
}
