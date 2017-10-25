package com.hapramp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.response.SkillsModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<SkillsModel> category;
    private OnCategoryItemClickListener categoryItemClickListener;

    public CategoryRecyclerAdapter(Context context, OnCategoryItemClickListener categoryItemClickListener) {
        this.context = context;
        this.categoryItemClickListener = categoryItemClickListener;
    }

    public void setCategories(ArrayList<SkillsModel> category){
        this.category = category;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = new CategoryItemView(context);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int pos) {
        categoryViewHolder.bind(category.get(pos),categoryItemClickListener);
    }

    @Override
    public int getItemCount() {
        return category!=null?category.size():0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        CategoryItemView categoryItemView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryItemView = (CategoryItemView) itemView;
        }

        public void bind(SkillsModel model,OnCategoryItemClickListener categoryItemClickListener){

            categoryItemView.setSkillsBgImage(model.getId());
            categoryItemView.setSkillTitle(model.getName());
            categoryItemView.setOnClickListener(categoryItemClickListener);

        }

    }

    interface OnCategoryItemClickListener{
        void onCategoryClicked(int id);
    }
}
