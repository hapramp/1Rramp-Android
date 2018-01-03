package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.response.UserModel;
import com.hapramp.views.skills.SkillsTabView;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private Context context;
    private List<UserModel.Skills> category;
    private OnCategoryItemClickListener categoryItemClickListener;
    private int selectedSkillId = 0;

    public CategoryRecyclerAdapter(Context context, OnCategoryItemClickListener categoryItemClickListener) {
        this.context = context;
        this.categoryItemClickListener = categoryItemClickListener;
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
        categoryViewHolder.bind(category.get(pos),categoryItemClickListener);
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

        public void bind(final UserModel.Skills model, final OnCategoryItemClickListener categoryItemClickListener){

            categoryItemView.setSkillId(model.id);

            if(selectedSkillId==model.getId()){
                categoryItemView.setSelected(true);
            }else{
                categoryItemView.setSelected(false);
            }

            categoryItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSkillId = model.getId();
                    notifyDataSetChanged();
                    categoryItemClickListener.onCategoryClicked(selectedSkillId);
                }
            });

        }

    }

    public interface OnCategoryItemClickListener{
        void onCategoryClicked(int id);
    }
}
