package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.views.skills.CommunityItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunitySelectionView extends FrameLayout {
  CommunityGridViewGroup communityGridViewGroup;
  TextView noInterestMsg;

  private Context mContext;
  private ArrayList<Integer> selectedCommunityIds;
  private List<CommunityModel> mCommunityList;

  public CommunitySelectionView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.community_view, this);
    selectedCommunityIds = new ArrayList<>();
    communityGridViewGroup = view.findViewById(R.id.viewWrapper);
    noInterestMsg = view.findViewById(R.id.no_interest_msg);
  }

  public CommunitySelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CommunitySelectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setCommunityList(List<CommunityModel> communityList) {
    this.mCommunityList = communityList;
    addViews();
  }

  public void setCommunityList(List<CommunityModel> communityList, List<CommunityModel> preselectedCommunity) {
    this.mCommunityList = communityList;
    selectedCommunityIds.clear();
    for (int i = 0; i < preselectedCommunity.size(); i++) {
      selectedCommunityIds.add(preselectedCommunity.get(i).getCommunityId());
    }
    addViews();
  }

  private CommunitySelectionListener communitySelectionListener;

  public List<Integer> getSelectionList() {
    return selectedCommunityIds;
  }

  private void addViews() {
    communityGridViewGroup.removeAllViews();
    communityGridViewGroup.setChildInfo(3,mCommunityList.size());
    for (int i = 0; i < mCommunityList.size(); i++) {
      final CommunityItemView view = new CommunityItemView(mContext);
      view.setCommunityDetails(mCommunityList.get(i));
      view.setSelection((selectedCommunityIds.indexOf(mCommunityList.get(i).getCommunityId()) > -1));
      view.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          int index = selectedCommunityIds.indexOf(view.getCommunityId());
          if (index == -1) {
            view.setSelection(true);
            selectedCommunityIds.add(view.getCommunityId());
          } else {
            if (selectedCommunityIds.size() < 2) {
              Toast.makeText(mContext, "You must have atleast one community", Toast.LENGTH_LONG).show();
              return;
            }
            view.setSelection(false);
            selectedCommunityIds.remove(index);
          }
          if (communitySelectionListener != null) {
            communitySelectionListener.onCommunitySelectionChanged(selectedCommunityIds);
          }
        }
      });
      communityGridViewGroup.addView(view, i,
        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

  }

  public void setCommunitySelectionListener(CommunitySelectionListener communitySelectionListener) {
    this.communitySelectionListener = communitySelectionListener;
  }

  public interface CommunitySelectionListener {
    void onCommunitySelectionChanged(List<Integer> selections);
  }
}
