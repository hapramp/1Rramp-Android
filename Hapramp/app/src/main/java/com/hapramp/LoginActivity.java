package com.hapramp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CreateOrUpdateUserCallback;
import com.hapramp.models.requests.UpdateUserRequestModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.logger.L;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CreateOrUpdateUserCallback {

    @BindView(R.id.sign_in_button)
    SignInButton signInWithGoogle;
    @BindView(R.id.loginButton)
    TextView loginButton;
    @BindView(R.id.forgotPassButton)
    TextView forgotPassButton;
    @BindView(R.id.signUpButton)
    TextView signUpButton;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = LoginActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private int RC_GC_SIGNIN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        attachListeners();
        initialize();
    }

    private void attachListeners() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateWithEmailPassword();
            }
        });

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGoogleAccounts();
            }
        });
    }

    private void authenticateWithEmailPassword() {
        if (validFields()) {
            signInWithFirebase(email.getText().toString(), password.getText().toString());
        }
    }

    private boolean validFields() {
        if (password.getText().toString().length() < 6)
            return false;
        if (email.getText().length() < 6)
            return false;
        return true;
    }

    private void signInWithFirebase(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    t("Success");
                    L.D.m(TAG, "signed in with firebase");
                    FirebaseUser user = task.getResult().getUser();
                    if (user.isEmailVerified()) {
                        updateAppServer(user);
                    } else {
                        // send verification email
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    notifyUserForEmailSent();
                                }
                            }
                        });
                    }
                } else {
                    failedToSignIn();
                }

            }
        });

    }

    private void failedToSignIn() {
        Toast.makeText(this, "Invalid Credentials! Please check and re-try", Toast.LENGTH_SHORT).show();
    }

    private void notifyUserForEmailSent() {
        Toast.makeText(this, "We sent Confirmation to you email.", Toast.LENGTH_LONG).show();
    }

    private void initialize() {

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                hideProgress();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateAppServer(user);
                    Log.d(TAG, "user is signed In.");
                } else {
                    // signed out

                    Log.d(TAG, "user is signed out");
                }
            }
        };
    }

    private void requestGoogleAccounts() {

        if (mGoogleApiClient.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_GC_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GC_SIGNIN) {
            L.D.m(TAG, "received result from google");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            t("Success With Google User Account");
            L.D.m(TAG, "handle signin...");
            showProgress("Logging In...");
            GoogleSignInAccount account = result.getSignInAccount();
            L.D.m(TAG, "account received :" + account.getEmail());
            signInWithFirebase(account);

        } else {
            Log.d(TAG, result.getStatus().getStatus() + "");
            t("failed ! ");
        }
    }

    private void signInWithFirebase(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        L.D.m(TAG, "Id token: " + account.getIdToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            t("Success With firebase Auth");
                            L.D.m(TAG, "signed in with firebase");
                            FirebaseUser user = task.getResult().getUser();
                            updateAppServer(user);
                        } else {
                            L.D.m(TAG, "Error:" + task.getException());
                        }
                    }
                });

    }

    private void updateAppServer(final FirebaseUser user) {

        user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                DataServer.createOrUpdateUser(
                        new UpdateUserRequestModel(user.getEmail()
                                , user.getDisplayName()
                                , task.getResult().getToken()),
                        LoginActivity.this);
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

    private void t(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void redirect() {
        Intent intent = new Intent(this, OrganisationActivity.class);
        startActivity(intent);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        t("No Connection");
        hideProgress();
    }

    @Override
    public void onUserCreated() {
        redirect();
    }

    @Override
    public void onFailedToCreateOrUpdateUser() {
        Toast.makeText(this, "Failed to Authenticate", Toast.LENGTH_LONG).show();
    }
}
