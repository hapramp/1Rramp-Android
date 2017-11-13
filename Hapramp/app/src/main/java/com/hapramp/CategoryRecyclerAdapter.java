package com.hapramp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.logger.L;
import com.hapramp.models.response.SkillsModel;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private Context context;
    private List<SkillsModel> category;
    private OnCategoryItemClickListener categoryItemClickListener;
    private int selectedSkillId = 0;

    public CategoryRecyclerAdapter(Context context, OnCategoryItemClickListener categoryItemClickListener) {
        this.context = context;
        this.categoryItemClickListener = categoryItemClickListener;
    }

    public void setCategories(List<SkillsModel> category){
        this.category = category;
        notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(new CategoryItemView(context));
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

        public void bind(final SkillsModel model, final OnCategoryItemClickListener categoryItemClickListener){

            categoryItemView.setSkillsBgImage(model.getId());
            categoryItemView.setSkillTitle(model.getName());

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
