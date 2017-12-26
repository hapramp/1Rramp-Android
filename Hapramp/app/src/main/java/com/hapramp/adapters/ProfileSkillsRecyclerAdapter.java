package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.response.UserModel;
import com.hapramp.views.SkillsTabView;

import java.util.List;

/**
 * Created by Ankit on 10/29/2017.
 */

public class ProfileSkillsRecyclerAdapter extends RecyclerView.Adapter<ProfileSkillsRecyclerAdapter.CategoryViewHolder>{

    private Context context;
    private List<UserModel.Skills> category;

    public ProfileSkillsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(List<UserModel.Skills> category){
        this.category = category;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(new SkillsTabView(context));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int pos) {
        categoryViewHolder.bind(category.get(pos));
    }

    @Override
    public int getItemCount() {
        return category!=null?category.size():0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        SkillsTabView categoryItemView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryItemView = (SkillsTabView) itemView;
        }

        public void bind(final UserModel.Skills model){

            categoryItemView.setSkillsBgImage(model.id);
            categoryItemView.setSkillTitle(model.name);
            categoryItemView.setSelected(true);
            categoryItemView.enableTabIndicator(false);

        }

    }
}
