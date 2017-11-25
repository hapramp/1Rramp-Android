package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.hapramp.utils.FontManager;
import com.hapramp.R;
import com.hapramp.views.SkillsItemView;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.OnSkillsUpdateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.SkillsUpdateBody;
import com.hapramp.models.response.SkillsModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkillRegistrationActivity extends AppCompatActivity implements FetchSkillsResponse, OnSkillsUpdateCallback {

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.skillsGridView)
    GridView skillsGridView;
    @BindView(R.id.skills_continueBtn)
    TextView skillsContinueBtn;
    SkillsGridAdapter skillsGridAdapter;
    private List<SkillsModel> skills;
    private HashMap<String, Integer> selectionMap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_registration);
        ButterKnife.bind(this);
        init();
        fetchSkills();
        attachListeners();
    }

    private void attachListeners() {
        skillsContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSkillsUpdate();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchSkills() {
        showProgress("Fetching Skills");
        DataServer.fetchSkills(this);
    }

    private void requestSkillsUpdate(){
        showProgress("Setting Your Skills...");
        SkillsUpdateBody body = new SkillsUpdateBody(getSelectionIds());
        DataServer.setSkills(body,this);
    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        backBtn.setTypeface(new FontManager().getTypeFace(FontManager.FONT_MATERIAL));
        // set Adapter
        skillsGridAdapter = new SkillsGridAdapter(this);
        skillsGridView.setAdapter(skillsGridAdapter);
        selectionMap = new HashMap<>();
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onSkillsFetched(List<SkillsModel> skillsModels) {
        hideProgress();
        skillsGridAdapter.onDataLoaded(skillsModels);
    }

    @Override
    public void onSkillFetchError() {
        hideProgress();
        L.D.m("SkillsActivity", "Error While Fetching..");
    }

    @Override
    public void onSkillsUpdated() {
        hideProgress();
        L.D.m("SkillsActivity", "Updated Skills");
       redirectToHome();
    }

    @Override
    public void onSkillsUpdateFailed() {
        hideProgress();
        L.D.m("SkillsActivity", "Error While Updating Skills..");
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

    public Integer[] getSelectionIds(){
        // get the iterator
        int i=0;
        Integer[] ids = new Integer[10];
        Iterator it = selectionMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            L.D.m("Skills Selection",pair.getKey() + " = " + pair.getValue());
            ids[i++] = (Integer) pair.getValue();
        }
        return ids;
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        hideProgress();
        finish();
    }

}
