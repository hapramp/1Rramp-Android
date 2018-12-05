package com.hapramp.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.MicroCommunity;
import com.hapramp.ui.activity.MicroCommunityActivity;
import com.hapramp.utils.CommunityIds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MicroCommunityContainerView extends FrameLayout {
  @BindView(R.id.tagsContainer)
  LinearLayout tagsContainer;
  @BindView(R.id.loading_progress_bar)
  ProgressBar loadingProgressBar;
  private Context mContext;
  private int mParentCommunityId = CommunityIds.EXPLORE;
  private List<MicroCommunity> mCommunityList;

  public MicroCommunityContainerView(@NonNull Context context) {
    super(context);
    initialize(context);
  }

  private void initialize(Context context) {
    this.mContext = context;
    mCommunityList = new ArrayList<>();
    View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_container_view, this);
    ButterKnife.bind(this, view);
    fetchCommunities();
  }

  private void fetchCommunities() {
    if (loadingProgressBar != null) {
      loadingProgressBar.setVisibility(VISIBLE);
    }
    RetrofitServiceGenerator.getService().fetchMicroCommunity().enqueue(new Callback<List<MicroCommunity>>() {
      @Override
      public void onResponse(Call<List<MicroCommunity>> call, Response<List<MicroCommunity>> response) {
        if (loadingProgressBar != null) {
          loadingProgressBar.setVisibility(GONE);
        }
        if (response.isSuccessful()) {
          handleCommunityListResponse(response.body());
        }
      }

      @Override
      public void onFailure(Call<List<MicroCommunity>> call, Throwable t) {

      }
    });
  }

  private void handleCommunityListResponse(List<MicroCommunity> communityList) {
    this.mCommunityList = communityList;
    addSubCommunityToView();
  }

  private void addSubCommunityToView() {
    List<MicroCommunity> relevantList = new ArrayList<>();
    if (mParentCommunityId == CommunityIds.EXPLORE || mParentCommunityId == CommunityIds.FEED) {
      relevantList = mCommunityList;
    } else {
      for (int i = 0; i < mCommunityList.size(); i++) {
        if (mCommunityList.get(i).getCommunity().getId() == mParentCommunityId) {
          relevantList.add(mCommunityList.get(i));
        }
      }
    }
    //add to view
    addMicrocommunitiesToView(relevantList);
  }

  private void addMicrocommunitiesToView(final List<MicroCommunity> microcoms) {
    try {
      tagsContainer.removeAllViews();
      TextView titileView = null;
      for (int i = 0; i < microcoms.size(); i++) {
        final MicroCommunity microCommunity = microcoms.get(i);
        View view = LayoutInflater.from(mContext).inflate(R.layout.micro_community_item_view, null);
        titileView = view.findViewById(R.id.micro_community_tag);
        titileView.setText(String.format("%s", microCommunity.getTag()));
        titileView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            openCommunityDetailsPage(microCommunity);
          }
        });
        tagsContainer.addView(view);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openCommunityDetailsPage(MicroCommunity microCommunity) {
    Intent intent = new Intent(mContext, MicroCommunityActivity.class);
    intent.putExtra(MicroCommunityActivity.EXTRA_COMMUNITY,microCommunity);
    mContext.startActivity(intent);
  }

  public MicroCommunityContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initialize(context);
  }

  public MicroCommunityContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize(context);
  }

  public void setParentCommunityId(int id) {
    this.mParentCommunityId = id;
    if (mCommunityList.size() == 0) {
      return;
    }
    addSubCommunityToView();
  }
}
