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
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CreateUserCallback;
import com.hapramp.interfaces.FetchUserCallback;
import com.hapramp.logger.L;
import com.hapramp.models.UserAccountModel;
import com.hapramp.models.requests.CreateUserRequest;
import com.hapramp.models.response.CreateUserReponse;
import com.hapramp.models.response.FetchUserResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FetchUserCallback, CreateUserCallback {

    @BindView(R.id.withGoogleBtn)
    TextView signInWithGoogle;
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
    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.lock_icon)
    TextView lockIcon;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = LoginActivity.class.getSimpleName();
    private Typeface materialTypeface;
    private GoogleApiClient mGoogleApiClient;
    private int RC_GC_SIGNIN = 101;
    private FirebaseUser user;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkLastStatus();
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterPage();
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToForgetPassword();
            }
        });

    }

    private void authenticateWithEmailPassword() {
        if (validFields()) {
            signInWithFirebase(email.getText().toString(), password.getText().toString());
        }
    }

    private boolean validFields() {

        String _e = email.getText().toString().trim();
        String _p = password.getText().toString().trim();
        //check email
        if (Validator.validateEmail(_e)) {
            // check password
            if (_p.length() > 6) {
                return true;
            } else {
                Toast.makeText(this, "Short Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void failedToSignIn() {
        Toast.makeText(this, "Invalid Credentials! Please check and re-try", Toast.LENGTH_SHORT).show();
    }

    private void notifyUserForEmailSent() {
        Toast.makeText(this, "We have sent you a Confirmation Link to Your Email!", Toast.LENGTH_LONG).show();
    }

    private void checkLastStatus() {
        if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
            redirectToHome();
        }
    }

    private void initialize() {

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        materialTypeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        lockIcon.setTypeface(materialTypeface);
        userIcon.setTypeface(materialTypeface);
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
                    Log.d(TAG, "user is signed In.");
                } else {
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
        intent.setFlags(0);
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

            GoogleSignInAccount account = result.getSignInAccount();
            L.D.m(TAG, "account received :" + account.getEmail());
            showProgress("Logging in as : " + account.getEmail());
            signInWithFirebase(account);

        } else {
            Log.d(TAG, result.getStatus().getStatus() + "");
            t(result.getStatus().getStatusMessage());
        }
    }

    private void signInWithFirebase(String email, String password) {

        showProgress("Signing In...");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    L.D.m(TAG, "signed in with firebase");
                    user = task.getResult().getUser();
                    if (user.isEmailVerified()) {
                        hideProgress();
                        fetchUserFromAppServer(user);
                    } else {
                        // send verification email
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    hideProgress();
                                    notifyUserForEmailSent();
                                }
                            }
                        });
                    }
                } else {
                    hideProgress();
                    failedToSignIn();
                }

            }
        });

    }

    private void signInWithFirebase(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            L.D.m(TAG, "signed in with firebase");
                            user = task.getResult().getUser();
                            fetchUserFromAppServer(user);
                        } else {
                            L.D.m(TAG, "Error:" + task.getException());
                        }
                    }
                });

    }

    private void fetchUserFromAppServer(final FirebaseUser user) {

        showProgress("Preparing Your Account...");
        user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                token = task.getResult().getToken();
                HaprampPreferenceManager.getInstance().saveToken(token);
                DataServer.fetchUser(LoginActivity.this);
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
    public void onUserFetched(FetchUserResponse userResponse) {

        L.D.m(TAG, "User fetched : " + userResponse.toString());
        HaprampPreferenceManager.getInstance().setUser(new Gson().toJson(userResponse));
        HaprampPreferenceManager.getInstance().setLoggedIn(true);
        HaprampPreferenceManager.getInstance().setUserId(String.valueOf(userResponse.id));
        HaprampPreferenceManager.getInstance().setUserEmail(userResponse.email);

        if (userResponse.organization == null) {
            redirectToOrgsPage();
        } else {
            if (userResponse.skills.size() == 0) {
                redirectToSkillsPage();
            } else {
                redirectToHome();
            }
        }


    }

    @Override
    public void onUserFetchedError() {
        Toast.makeText(this, "User Fetched Error!", Toast.LENGTH_LONG).show();
        hideProgress();
    }

    @Override
    public void onUserNotExists() {
        L.D.m(TAG, "User doesn`t exists, Creating new :)");
        hideProgress();
        createUser();
    }

    private void createUser() {
        showProgress("Creating User New...");
        DataServer.createUser(new CreateUserRequest(user.getEmail(), user.getDisplayName(), user.getDisplayName(), token, 1), this);
    }

    @Override
    public void onUserCreated(CreateUserReponse body) {

        L.D.m(TAG, "User Created! :)");

        UserAccountModel accountModel = new UserAccountModel(body.id, body.username, body.full_name, body.karma);
        HaprampPreferenceManager.getInstance().setUser(new Gson().toJson(accountModel));
        HaprampPreferenceManager.getInstance().setLoggedIn(true);
        HaprampPreferenceManager.getInstance().setUserEmail(body.email);
        redirectToOrgsPage();
        hideProgress();

    }

    @Override
    public void onFailedToCreateUser(String message) {
        L.D.m(TAG, "Failed To Create User :(");
        hideProgress();
    }

    private void redirectToOrgsPage() {
        Intent intent = new Intent(this, OrganisationActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToSkillsPage() {
        Intent intent = new Intent(this, SkillRegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToForgetPassword() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToRegisterPage() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        hideProgress();
        finish();
    }

}
