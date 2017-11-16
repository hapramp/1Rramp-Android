package com.hapramp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.UserBioUpdateRequestCallback;
import com.hapramp.models.requests.UserBioUpdateRequestBody;
import com.hapramp.preferences.HaprampPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoEditingActivity extends AppCompatActivity implements UserBioUpdateRequestCallback {


    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.bioEt)
    EditText bioEt;
    @BindView(R.id.okBtn)
    TextView okBtn;
    @BindView(R.id.cancelBtn)
    TextView cancelBtn;

    String lastBio;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_editing);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        lastBio = getIntent().getExtras().getString("bio");
        bioEt.setText(lastBio);
        attachListener();

    }

    private void attachListener() {

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBio();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveBio() {
        if(validateBio()){
            showProgress("Updating Your Bio...");
            DataServer.updateUserBio(HaprampPreferenceManager.getInstance().getUserId(),
                    new UserBioUpdateRequestBody(bioEt.getText().toString()),this);
        }
    }

    private boolean validateBio(){

        if(bioEt.getText().toString().length()<1){
            Toast.makeText(this,"Too Short Bio",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

    @Override
    public void onBioUpdated() {
        hideProgress();
        Toast.makeText(this,"Bio Updated !",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBioUpdateError() {
        hideProgress();
        Toast.makeText(this,"Cannot Update Your Bio :(",Toast.LENGTH_LONG).show();
        finish();
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

}
