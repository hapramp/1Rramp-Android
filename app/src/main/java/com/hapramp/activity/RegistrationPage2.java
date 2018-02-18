package com.hapramp.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationPage2 extends AppCompatActivity {

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.usernameHolder)
    LinearLayout usernameHolder;
    @BindView(R.id.lock_icon)
    TextView lockIcon;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.passwordHolder)
    LinearLayout passwordHolder;
    @BindView(R.id.cnf_lock_icon)
    TextView cnfLockIcon;
    @BindView(R.id.cnf_password)
    EditText cnfPassword;
    @BindView(R.id.cnf_passwordHolder)
    LinearLayout cnfPasswordHolder;
    @BindView(R.id.registerButton)
    TextView registerBtn;
    private String email;

    private Typeface materialTypeface;
    private String TAG = RegistrationPage2.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page2);
        ButterKnife.bind(this);
        email = getIntent().getExtras().getString("email");
    }

}
