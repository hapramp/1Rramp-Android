package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.OrgUpdateCallback;
import com.hapramp.interfaces.OrgsFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.UserResponse;
import com.hapramp.models.requests.UserUpdateModel;
import com.hapramp.models.response.OrgsResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrganisationActivity extends AppCompatActivity implements OrgsFetchCallback, OrgUpdateCallback {

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.actionbar_title)
    TextView actionbarTitle;
    @BindView(R.id.organisation_people_icon)
    TextView organisationPeopleIcon;
    @BindView(R.id.orgs)
    ListView orgsListView;
    @BindView(R.id.organisation_continueBtn)
    TextView organisationContinueBtn;
    Typeface materialTypeface;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.orgsCaption)
    TextView orgsCaption;
    @BindView(R.id.orgsLoadingProgress)
    ProgressBar orgsLoadingProgress;
    @BindView(R.id.skipButton)
    TextView skipButton;
    private List<OrgsResponse> mOrgs;
    private ProgressDialog progressDialog;
    private int selectedPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        ButterKnife.bind(this);
        init();
        fetchOrgs();
    }

    private void fetchOrgs() {
        orgsLoadingProgress.setVisibility(View.VISIBLE);
        DataServer.getOrgs(this);
    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        materialTypeface = new FontManager().getTypeFace(FontManager.FONT_MATERIAL);
        organisationPeopleIcon.setTypeface(materialTypeface);
        backBtn.setTypeface(materialTypeface);
        orgsListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });

        organisationContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSetOrg();
            }
        });

    }

    @Override
    public void onOrgsFetched(List<OrgsResponse> orgs) {

        orgsLoadingProgress.setVisibility(View.GONE);

        mOrgs = orgs;
        ArrayList<String> _o = new ArrayList<>();
        for (OrgsResponse org : orgs) {
            _o.add(org.getName());
        }
        orgsListView.setAdapter(new ArrayAdapter<String>(this, R.layout.org_list_item, _o.toArray(new String[0])));

        orgsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPos = position;
            }
        });

    }

    private void requestSetOrg() {

        if(selectedPos==-1){
            Toast.makeText(this,"Select Organisation to update",Toast.LENGTH_SHORT).show();
            return;
        }

        UserResponse userAccountModel = HaprampPreferenceManager.getInstance().getUser();
        UserUpdateModel userUpdateModel = new UserUpdateModel(
                userAccountModel.email,
                userAccountModel.username,
                userAccountModel.full_name,
                mOrgs.get(selectedPos).id);

        L.D.m("Org", "UserUpdateModel " + userAccountModel.toString());
        showProgress("Setting Your Organisation...");
        DataServer.updateOrg(String.valueOf(userAccountModel.id), userUpdateModel, this);

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
    public void onOrgFetchedError() {
        hideProgress();
        Log.d("Org", "Updated Org");
    }

    @Override
    public void onOrgUpdated() {
        hideProgress();
        redirect();
        Log.d("Org", "Updated Org");
    }

    private void redirect() {
        Intent intent = new Intent(this, SkillRegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onOrgUpdateFailed() {
        hideProgress();
        Log.d("Org", "Updated Failed!!");
    }

}