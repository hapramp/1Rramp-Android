package com.hapramp.views.skills;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.ui.activity.CommunitySelectionActivity;

import java.util.List;

import static com.hapramp.ui.activity.CommunitySelectionActivity.EXTRA_PRESELECTED_MODE;

/**
 * Created by Ankit on 12/26/2017.
 */

public class InterestsView extends FrameLayout {
  private Context mContext;
  private ViewGroup parentView;
  private List<CommunityModel> communities;
  private TextView noInterestMessage;
  private boolean editable;

  public InterestsView(@NonNull Context context) {
    super(context);
    mContext = context;
    init();
  }

  private void init() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.community_view_container, this);
    parentView = view.findViewById(R.id.viewWrapper);
    noInterestMessage = view.findViewById(R.id.no_interest_msg);
  }

  public InterestsView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    init();
  }

  public InterestsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    init();
  }

  public void setCommunities(List<CommunityModel> communities, boolean editable) {
    this.communities = communities;
    this.editable = editable;
    if (communities != null && communities.size() > 0) {
      addViews();
    } else {
      noInterestMessage.setVisibility(VISIBLE);
    }
  }

  private void addViews() {
    if (parentView.getChildCount() > 0) {
      // already added, no need to add more duplicate views
      return;
    }

    for (int i = 0; i < communities.size(); i++) {
      final CommunityItemView view = new CommunityItemView(mContext);
      view.setCommunityDetails(communities.get(i));
      view.setSelection(false);
      parentView.addView(view, i,
        new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    if (editable) {
      //add edit icon with click listener
      final CommunityItemView editCommunityItem = new CommunityItemView(mContext);
      CommunityModel communityModel = new CommunityModel("Add or Remove Community",
        "android.resource://com.hapramp/drawable/edit_community",
        "", "#77938d8d", "Edit Community", 404);
      editCommunityItem.setCommunityDetails(communityModel);
      editCommunityItem.setSelection(false);
      editCommunityItem.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToCommunitSelectionPage();
        }
      });
      parentView.addView(editCommunityItem);
    }
    noInterestMessage.setVisibility(GONE);

  }

  private void navigateToCommunitSelectionPage() {
    Intent i = new Intent(mContext, CommunitySelectionActivity.class);
    i.putExtra(EXTRA_PRESELECTED_MODE, true);
    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    mContext.startActivity(i);
  }

}
