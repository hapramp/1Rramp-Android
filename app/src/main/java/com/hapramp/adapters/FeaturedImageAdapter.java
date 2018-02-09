package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.FeaturedImageSelectionModel;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.editor.FeaturedImageView;

import java.util.ArrayList;

/**
 * Created by Ankit on 2/3/2018.
 */

public class FeaturedImageAdapter extends RecyclerView.Adapter<FeaturedImageAdapter.FeaturedImageViewHolder>{

    private final Context mContext;
    ArrayList<FeaturedImageSelectionModel> imageSelectionModels;
    FeaturedImageSelectionModel item;
    int selectedIndex = -1;

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClicked(int index) {

            item = imageSelectionModels.get(index);
            if(index==selectedIndex){
                // double selection results to de-selection
                item.setSelected(false);
                selectedIndex = -1;
            }else{
                //reset the last selection
                if(selectedIndex!=-1){
                    imageSelectionModels.get(selectedIndex).setSelected(false);
                    notifyItemChanged(selectedIndex);
                }

                item.setSelected(true);
                selectedIndex = index;

            }
            notifyItemChanged(index);

        }
    };

    public FeaturedImageAdapter(Context context) {

        this.mContext = context;
        this.imageSelectionModels = new ArrayList<>();

    }

    public void setImageSelectionModels(ArrayList<FeaturedImageSelectionModel> models){

        this.imageSelectionModels = models;
        notifyDataSetChanged();

    }

    public String getSelectedFeaturedImageUrl(){

        if(selectedIndex!=-1){
            return imageSelectionModels.get(selectedIndex).getUrl();
        }
        return null;
    }

    @Override
    public FeaturedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new FeaturedImageView(mContext);
        return new FeaturedImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeaturedImageViewHolder holder, int position) {
        holder.bind(imageSelectionModels.get(position),position,onItemClickListener);
    }

    @Override
    public int getItemCount() {
       // Log.d("EditorView","items "+imageSelectionModels.size());
        return imageSelectionModels.size();
    }

    private interface OnItemClickListener{
        void onItemClicked(int index);
    }

    class FeaturedImageViewHolder extends RecyclerView.ViewHolder{

        FeaturedImageView view;
        public FeaturedImageViewHolder(View itemView) {
            super(itemView);
            view = (FeaturedImageView) itemView;
        }

        public void bind(FeaturedImageSelectionModel data, final int index, final OnItemClickListener onItemClickListener){

            view.setImageSource(data.getUrl());
            view.setSelection(data.isSelected());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClicked(index);
                    }
                }
            });

        }

    }
}
