package com.hapramp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.pageTitle)
    TextView pageTitle;
    @BindView(R.id.forgetPasswordIcon)
    TextView forgetPasswordIcon;
    @BindView(R.id.sendPasswordRecoveryEmailBtn)
    TextView sendPasswordRecoveryEmailBtn;
    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.emailHolder)
    LinearLayout emailHolder;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

    }

}