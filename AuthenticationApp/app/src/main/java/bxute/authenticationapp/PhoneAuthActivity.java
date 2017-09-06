package bxute.authenticationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneAuthActivity extends AppCompatActivity {

    @BindView(R.id.phoneNumberTv)
    EditText phoneNumberTv;
    @BindView(R.id.getOtp)
    Button getOtp;
    @BindView(R.id.otpInput)
    EditText otpInput;
    @BindView(R.id.verifyOtp)
    Button verifyOtp;
    private ProgressDialog progressDialog;
    private String mVerificationId;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            t("verify completed");
            sigInWithPhoneCred(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            hideProgress();
            t("failed verification");
        }

        @Override
        public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationID, forceResendingToken);
            hideProgress();
            Log.d("PhoneAuthAct", "code is sent");
            t("code sent");
            mVerificationId = verificationID;
        }
    };
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = PhoneAuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ButterKnife.bind(this);
        init();
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Sending OTP");
                requestSendOtp();
            }
        });
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });

    }

    private void sigInWithPhoneCred(PhoneAuthCredential phoneAuthCredential) {

        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    // singned in
                    FirebaseUser user = task.getResult().getUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("email", user.getPhoneNumber());
                    redirect(bundle);
                }else{
                    t("Cannot Verify...");
                }
            }
        });
    }

    private void init() {
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                hideProgress();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // singned in
                    Bundle bundle = new Bundle();
                    bundle.putString("email", user.getPhoneNumber());
                    redirect(bundle);

                    Log.d(TAG, "user is signed In.");
                } else {
                    // signed out
                    Log.d(TAG, "user is signed out");
                }
            }
        };
    }

    private void redirect(Bundle bundle) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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

    private void t(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void verify() {
        showProgress("Verifying...");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,otpInput.getText().toString());
        sigInWithPhoneCred(credential);
    }

    private void requestSendOtp() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumberTv.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
