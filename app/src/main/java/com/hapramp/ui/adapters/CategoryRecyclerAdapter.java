package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.CommunityModel;
import com.hapramp.views.skills.CommunityTabItemView;

import java.util.ArrayList;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

  private Context context;
  private OnCategoryItemClickListener categoryItemClickListener;
  private int selectedCommunityId = 0;
  private ArrayList<CommunityModel> communities;

  public CategoryRecyclerAdapter(Context context, OnCategoryItemClickListener categoryItemClickListener) {
    this.context = context;
    this.categoryItemClickListener = categoryItemClickListener;
  }


  public void setCommunities(ArrayList<CommunityModel> communityModels) {
    this.communities = communityModels;
    notifyDataSetChanged();
  }

  @Override
  public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    return new CategoryViewHolder(new CommunityTabItemView(context));
  }

  @Override
  public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int pos) {
    categoryViewHolder.bind(communities.get(pos), categoryItemClickListener);
  }

  @Override
  public int getItemCount() {
    return communities != null ? communities.size() : 0;
  }


  public interface OnCategoryItemClickListener {
    void onCategoryClicked(String tag);
  }

  class CategoryViewHolder extends RecyclerView.ViewHolder {

    CommunityTabItemView categoryItemView;

    public CategoryViewHolder(View itemView) {
      super(itemView);
      categoryItemView = (CommunityTabItemView) itemView;
    }

    public void bind(final CommunityModel model, final OnCategoryItemClickListener categoryItemClickListener) {

      categoryItemView.setCommunity(model);

      if (selectedCommunityId == model.getmId()) {
        categoryItemView.setSelected(true);
      } else {
        categoryItemView.setSelected(false);
      }

      categoryItemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectedCommunityId = model.getmId();
          notifyDataSetChanged();
          categoryItemClickListener.onCategoryClicked(model.getmTag());
        }
      });

    }

  }
}
