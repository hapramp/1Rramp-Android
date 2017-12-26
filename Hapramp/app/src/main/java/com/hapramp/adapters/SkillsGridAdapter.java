package com.hapramp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hapramp.models.response.UserModel;
import com.hapramp.models.response.UserModel.Skills;
import com.hapramp.views.SkillsItemView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ankit on 12/26/2017.
 */
public class SkillsGridAdapter extends BaseAdapter {

    private List<UserModel.Skills> skills;
    private Context context;
    private HashMap<String, Integer> selectionMap;

    public SkillsGridAdapter(@NonNull Context context) {
        this.context = context;
        selectionMap = new HashMap<>();
    }

    public void onDataLoaded(List<UserModel.Skills> skills) {
        this.skills = skills;
        notifyDataSetChanged();
    }

    public HashMap<String,Integer> getSelectionMap(){
        return selectionMap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SkillsItemView view = convertView == null ? new SkillsItemView(context) : (SkillsItemView) convertView;
        bindDataView(position, view);
        return view;
    }

    private void bindDataView(final int position, final SkillsItemView view) {
        // set background image
        view.setSkillTitle(skills.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = skills.get(position).getName();
                if(selectionMap.get(key)!=null) {
                    //selected already
                    view.setSelection(false);
                    selectionMap.remove(key);
                }else{
                    // prev. unselected
                    view.setSelection(true);
                    selectionMap.put(key,skills.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getCount() {
        return skills == null ? 0 : skills.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
