package com.hapramp.views.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.views.extraa.CategoryTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/25/2017.
 */

public class PostCommunityView extends FrameLayout {

  private WrapViewGroup rootView;
  private Context mContext;
  private List<CommunityModel> communities;
  private ArrayList<String> selectedTags;
  private CommunitySelectionChangeListener communitySelectionChangeListener;
  private List<String> mDefaultSelections;

  public PostCommunityView(@NonNull Context context) {
    super(context);
    mContext = context;
    init();
  }

  private void init() {
    View view = LayoutInflater.from(mContext).inflate(R.layout.post_community_view, this);
    rootView = view.findViewById(R.id.viewWrapper);
    selectedTags = new ArrayList<>();
    selectedTags.add(Communities.TAG_HAPRAMP);
    mDefaultSelections = new ArrayList<>();
  }

  public PostCommunityView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    init();
  }

  public PostCommunityView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    init();
  }

  public List<String> getSelectedTags() {
    return selectedTags;
  }

  public void setDefaultSelection(List<String> coms) {
    this.mDefaultSelections = coms;
    selectedTags.clear();
    selectedTags.addAll(mDefaultSelections);
  }

  public void initCategory() {
    CommunityListWrapper cr = new Gson().fromJson(HaprampPreferenceManager.getInstance().getAllCommunityAsJson(), CommunityListWrapper.class);
    communities = cr.getCommunityModels();
    addViews();
  }

  private void addViews() {
    if (rootView != null) {
      rootView.removeAllViews();
    }
    for (int i = 0; i < communities.size(); i++) {
      final CategoryTextView view = new CategoryTextView(mContext);
      view.setText(communities.get(i).getmName());
      view.setTag(communities.get(i).getmTag());
      view.setSelected(mDefaultSelections.contains(communities.get(i).getmTag()));
      view.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          int index = selectedTags.indexOf(view.getTag());
          if (index == -1) {
            // select it
            if (selectedTags.size() > 3) {
              Toast.makeText(mContext, "Maximum 3 Communities", Toast.LENGTH_LONG).show();
            } else {
              view.setSelected(true);
              selectedTags.add(0, (String) view.getTag());
            }
          } else {
            view.setSelected(false);
            selectedTags.remove(index);
          }

          if (communitySelectionChangeListener != null) {
            communitySelectionChangeListener.onCommunitySelectionChanged(selectedTags);
          }
        }
      });

      rootView.addView(view, i,
        new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  public void setCommunitySelectionChangeListener(CommunitySelectionChangeListener communitySelectionChangeListener) {
    this.communitySelectionChangeListener = communitySelectionChangeListener;
  }

  public interface CommunitySelectionChangeListener {
    void onCommunitySelectionChanged(List<String> communities);
  }
}
