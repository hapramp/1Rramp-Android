package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.adapters.SkillsGridAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.FetchSkillsResponse;
import com.hapramp.interfaces.OnSkillsUpdateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.SkillsUpdateBody;
import com.hapramp.models.response.UserModel;
import com.hapramp.models.response.UserModel.Skills;
import com.hapramp.utils.FontManager;

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
    private List<UserModel.Skills> skills;

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
    public void onSkillsFetched(List<UserModel.Skills> skillsModels) {
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

    public Integer[] getSelectionIds(){
        // get the iterator
        int i=0;
        Integer[] ids = new Integer[10];
        Iterator it = skillsGridAdapter.getSelectionMap().entrySet().iterator();
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
