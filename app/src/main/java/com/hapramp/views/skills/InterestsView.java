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
import com.hapramp.models.CommunityModel;
import com.hapramp.steem.Communities;
import com.hapramp.ui.activity.CommunitySelectionActivity;
import com.hapramp.utils.CommunityIds;
import com.hapramp.views.InterestGridViewGroup;
import com.hapramp.views.InterestItemView;

import java.util.List;

import static com.hapramp.ui.activity.CommunitySelectionActivity.EXTRA_PRESELECTED_MODE;

/**
 * Created by Ankit on 12/26/2017.
 */

public class InterestsView extends FrameLayout {
  private Context mContext;
  private InterestGridViewGroup parentView;
  private List<CommunityModel> communities;
  private TextView noInterestMessage;
  private boolean showEditButton;

  public InterestsView(@NonNull Context context) {
    super(context);
    mContext = context;
    init();
  }

  private void init() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.interest_view, this);
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
    this.showEditButton = editable;
    if (communities != null) {
      if (communities.size() > 0) {
        addViews();
      } else {
        noInterestMessage.setVisibility(VISIBLE);
      }
    } else {
      noInterestMessage.setVisibility(VISIBLE);
      noInterestMessage.setText(mContext.getResources().getString(R.string.unregistered_user_profile_msg));
    }
  }

  private void addViews() {
    parentView.removeAllViews();
    int childCount = showEditButton ? communities.size() + 1 : communities.size();
    parentView.setChildInfo(4, childCount);
    for (int i = 0; i < communities.size(); i++) {
      final InterestItemView view = new InterestItemView(mContext);
      view.setInterestDetails(communities.get(i));
      view.setSelection(true);
      parentView.addView(view, i,
        new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    if (showEditButton) {
      //add edit icon with click listener
      final InterestItemView editCommunityItem = new InterestItemView(mContext);
      CommunityModel communityModel = new CommunityModel("Add/Remove Community",
        "",
        Communities.EDIT_BORDER, "", "Edit", CommunityIds.EDIT_BORDER);
      editCommunityItem.setInterestDetails(communityModel);
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
    mContext.startActivity(i);
  }

}
