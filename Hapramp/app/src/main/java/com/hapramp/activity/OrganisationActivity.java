package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hapramp.utils.FontManager;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.OrgUpdateCallback;
import com.hapramp.interfaces.OrgsFetchCallback;
import com.hapramp.logger.L;
import com.hapramp.models.UserAccountModel;
import com.hapramp.models.requests.UserUpdateModel;
import com.hapramp.models.response.OrgsResponse;
import com.hapramp.preferences.HaprampPreferenceManager;

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
    private List<OrgsResponse> mOrgs;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organisation);
        ButterKnife.bind(this);
        init();
        fetchOrgs();
    }

    private void fetchOrgs() {
        DataServer.getOrgs(this);
    }

    private void init(){
        progressDialog = new ProgressDialog(this);
        materialTypeface = new FontManager().getTypeFace(FontManager.FONT_MATERIAL);
        organisationPeopleIcon.setTypeface(materialTypeface);
        backBtn.setTypeface(materialTypeface);
    }

    @Override
    public void onOrgsFetched(List<OrgsResponse> orgs) {
        mOrgs = orgs;
        ArrayList<String> _o = new ArrayList<>();
        for (OrgsResponse org :orgs) {
            _o.add(org.getName());
        }
        orgsListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_o.toArray(new String[0])));
        orgsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestSetOrg(position);
            }
        });
    }

    private void requestSetOrg(int position) {

        UserAccountModel userAccountModel = HaprampPreferenceManager.getInstance().getUser();
        UserUpdateModel userUpdateModel = new UserUpdateModel(
                HaprampPreferenceManager.getInstance().getUserEmail(),
                userAccountModel.getUsername(),
                HaprampPreferenceManager.getInstance().getFirstName()+" "+HaprampPreferenceManager.getInstance().getLastName(),
                mOrgs.get(position).id);

        L.D.m("Org","UserUpdateModel "+userAccountModel.toString());
        showProgress("Setting Your Organisation...");
        DataServer.updateOrg(String.valueOf(userAccountModel.getId()),userUpdateModel,this);

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
        Log.d("Org","Updated Org");
    }

    @Override
    public void onOrgUpdated() {
        hideProgress();
        redirect();
        Log.d("Org","Updated Org");
    }

    private void redirect() {
        Intent intent = new Intent(this,SkillRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onOrgUpdateFailed() {
        hideProgress();
        Log.d("Org","Updated Failed!!");
    }
}