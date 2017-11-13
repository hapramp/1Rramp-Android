package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hapramp.FontManager;
import com.hapramp.LoginActivity;
import com.hapramp.R;
import com.hapramp.Validator;

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
        init();

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


    private void init() {

        progressDialog = new ProgressDialog(this);
        Typeface materialTypeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        backBtn.setTypeface(materialTypeface);
        userIcon.setTypeface(materialTypeface);
        forgetPasswordIcon.setTypeface(materialTypeface);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLoginPage();
            }
        });

        sendPasswordRecoveryEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _e = email.getText().toString().trim();
                if(validateEmail(_e))
                   sendPasswordResetEmail(_e);
            }
        });

    }

    private boolean validateEmail(String _e) {
        if(Validator.validateEmail(_e)){
            return true;
        }else{
            Toast.makeText(this,"Invalid Email!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void sendPasswordResetEmail(String email) {

        showProgress("Sending Password Email...");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            emailSent();
                        } else {
                            cannotSendEmail();
                        }
                    }
                });
    }

    private void cannotSendEmail() {
        hideProgress();
        Toast.makeText(this,"Cannot Send Email! :(",Toast.LENGTH_SHORT).show();
    }

    private void emailSent() {
        hideProgress();
        Toast.makeText(this,"Recovery Email Sent! :)",Toast.LENGTH_SHORT).show();
        redirectToLoginPage();
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}