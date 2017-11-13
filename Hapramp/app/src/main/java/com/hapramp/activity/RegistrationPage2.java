package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hapramp.LoginActivity;
import com.hapramp.R;
import com.hapramp.RegisterActivity;
import com.hapramp.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.FontManager;

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
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = RegistrationPage2.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page2);
        ButterKnife.bind(this);
        email = getIntent().getExtras().getString("email");
        init();
        attachListener();

    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        materialTypeface = new FontManager(this).getDefault();
        userIcon.setTypeface(materialTypeface);
        lockIcon.setTypeface(materialTypeface);
        cnfLockIcon.setTypeface(materialTypeface);
        backBtn.setTypeface(materialTypeface);
    }

    private void attachListener() {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validDetails())
                    return;
                applyRegistration();
            }
        });

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


    private void applyRegistration() {
        showProgress("Applying For Registration...");
            mAuth.createUserWithEmailAndPassword(email, password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = task.getResult().getUser();
                        updateUserProfile(user);
                        showProgress("Sending Verification Email...");
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                hideProgress();
                                Toast.makeText(RegistrationPage2.this, "Your Verification Mail Sent, Plz Confirm!", Toast.LENGTH_LONG).show();
                                redirectToLoginPage();
                            }
                        });
                    } else {
                        hideProgress();
                        Toast.makeText(RegistrationPage2.this, "Something went wrong " + task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUserProfile(FirebaseUser user) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

    }

    private boolean validDetails() {

        String _u = userName.getText().toString().trim();
        String _p = password.getText().toString().trim();
        String _cp = cnfPassword.getText().toString().trim();

        if(Validator.validateUsername(_u)){
            //validate password
            if(Validator.validatePassword(_p)){
                // check match
                if(_p.equals(_cp)){
                    return true;
                }else{
                    Toast.makeText(this,"Passwords Donot Match!",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Passwords Too short!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Invalid Username!",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
