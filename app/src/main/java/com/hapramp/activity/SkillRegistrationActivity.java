package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.OnSkillsUpdateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.SkillsUpdateBody;
import com.hapramp.models.response.UserModel;
import com.hapramp.utils.FontManager;
import com.hapramp.views.skills.SelectableInterestsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkillRegistrationActivity extends AppCompatActivity implements OnSkillsUpdateCallback {

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.skillsGridView)
    SelectableInterestsView skillsGridView;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.communityContinueButton)
    TextView skillsContinueBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.skill_registration);
        ButterKnife.bind(this);
        init();
        attachListeners();

    }

    private void attachListeners() {

        skillsContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSkillsUpdate();
            }
        });

    }

    private void requestSkillsUpdate() {
        showProgress("Setting Your Skills...");
        SkillsUpdateBody body = new SkillsUpdateBody(getSelectionIds());
        DataServer.setSkills(body, this);
    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        backBtn.setTypeface(new FontManager().getTypeFace(FontManager.FONT_MATERIAL));
        List<UserModel.Skills> skills = new ArrayList<>();
        skillsGridView.setInterests(skills);

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

    public Integer[] getSelectionIds() {

        List<Integer> selected = skillsGridView.getSelectedSkills();
        int i = 0;
        Integer[] ids = new Integer[selected.size()];
        while (i<selected.size()) {
            ids[i] = selected.get(i);
            i++;
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
