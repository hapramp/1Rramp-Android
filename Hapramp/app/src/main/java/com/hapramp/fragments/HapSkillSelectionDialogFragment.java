package com.hapramp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.views.SkillsItemView;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.logger.L;
import com.hapramp.models.response.SkillsModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HapSkillSelectionDialogFragment extends DialogFragment implements FetchSkillsResponse {

    SkillsGridAdapter skillsGridAdapter;
    @BindView(R.id.hapskill_caption)
    TextView hapskillCaption;
    @BindView(R.id.skillsGridView)
    GridView skillsGridView;
    @BindView(R.id.hapskills_progressBar)
    ProgressBar hapskillsProgressBar;

    Unbinder unbinder;
    private List<SkillsModel> skills;
    private HashMap<String, Integer> selectionMap;


    public HapSkillSelectionDialogFragment() {
        fetchSkills();
    }

    private void fetchSkills() {
        DataServer.fetchSkills(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hap_skill_selection_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onSkillsFetched(List<SkillsModel> skillsModels) {
        skillsGridAdapter.onDataLoaded(skillsModels);
    }

    @Override
    public void onSkillFetchError() {
        L.D.m("HapSkills", "Error While Fetching..");
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        unbinder.unbind();

    }


    class SkillsGridAdapter extends BaseAdapter {

        private List<SkillsModel> skills;
        private Context context;
        public SkillsGridAdapter(@NonNull Context context) {
            this.context = context;
        }

        public void onDataLoaded(List<SkillsModel> skills) {
            this.skills = skills;
            notifyDataSetChanged();
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
            view.setSkillsBgImage(skills.get(position).getId());
            view.setSkillTitle(skills.get(position).getName());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = skills.get(position).getName();
                    if (selectionMap.get(key) != null) {
                        //selected already
                        view.setSelection(false);
                        selectionMap.remove(key);
                    } else {
                        // prev. unselected
                        view.setSelection(true);
                        selectionMap.put(key, skills.get(position).getId());
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

    public Integer[] getSelectionIds() {
        // get the iterator
        int i = 0;
        Integer[] ids = new Integer[10];
        Iterator it = selectionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            L.D.m("Skills Selection", pair.getKey() + " = " + pair.getValue());
            ids[i++] = (Integer) pair.getValue();
        }
        return ids;
    }

}
