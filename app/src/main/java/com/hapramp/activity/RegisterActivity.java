package com.hapramp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.hapramp.R;
import com.hapramp.interfaces.CreateUserCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.usernameHolder)
    LinearLayout usernameHolder;
    @BindView(R.id.signUpButton)
    TextView signUpButton;
    @BindView(R.id.divider)
    FrameLayout divider;
    @BindView(R.id.withGoogleBtn)
    TextView withGoogleBtn;
    @BindView(R.id.loginButton)
    TextView loginButton;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private int RC_GC_SIGNIN = 102;
    private String TAG = RegisterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

}
