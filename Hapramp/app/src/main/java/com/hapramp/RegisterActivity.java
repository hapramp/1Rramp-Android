package com.hapramp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hapramp.activity.RegistrationPage2;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CreateUserCallback;
import com.hapramp.models.requests.CreateUserRequest;
import com.hapramp.models.response.CreateUserReponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.logger.L;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CreateUserCallback {

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
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
        attachListeners();
    }

    private void init() {

        userIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void attachListeners() {

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validEmail()){
                    proceedRegistrationWithEmail(email.getText().toString());
                }
            }
        });

        withGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGoogleAccounts();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validEmail() {
        return email.getText().length()>4;
    }

    private void proceedRegistrationWithEmail(String email){
        Intent intent = new Intent(this, RegistrationPage2.class);
        intent.putExtra("email",email);
        startActivity(intent);
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
            L.D.m(TAG, "handle signin...");
            showProgress("Logging In...");
            GoogleSignInAccount account = result.getSignInAccount();
            L.D.m(TAG, "account received :" + account.getEmail());
            signInWithFirebase(account);

        } else {
            Log.d(TAG, result.getStatus().getStatus() + "");
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
                            L.D.m(TAG, "signed in with firebase");
                            user = task.getResult().getUser();
                            createUser();
                        } else {
                            L.D.m(TAG, "Error:" + task.getException());
                        }
                    }
                });

    }

    private void createUser() {

        showProgress("Creating User New...");
        user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                DataServer.createUser(new CreateUserRequest(user.getEmail()
                                , user.getDisplayName()
                                , user.getDisplayName()
                                , task.getResult().getToken(), 1)
                        , RegisterActivity.this);
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onUserCreated(CreateUserReponse body) {
        finish();
        redirect();
    }


    private void redirect() {

        Intent intent = new Intent(this, OrganisationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFailedToCreateUser() {
        finish();
        Toast.makeText(this, "Cannot Create User", Toast.LENGTH_LONG).show();
    }
}
