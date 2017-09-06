package bxute.authenticationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = GoogleSignInActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private int RC_GC_SIGNIN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        ButterKnife.bind(this);
        attachListeners();
        initialize();
    }

    private void attachListeners(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {

        if (mGoogleApiClient.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,RC_GC_SIGNIN);
    }

    private void initialize() {

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
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
                    // singned in
                    Bundle bundle = new Bundle();
                    bundle.putString("email", user.getEmail());
                    bundle.putString("id", user.getUid());
                    redirect(bundle);

                    Log.d(TAG, "user is signed In.");
                } else {
                    // signed out

                    Log.d(TAG, "user is signed out");
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_GC_SIGNIN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            t("Success");
            showProgress("Logging In....");
            GoogleSignInAccount account = result.getSignInAccount();
            signInWithFirebase(account);
        }else{
            Log.d(TAG,result.getStatus().getStatus()+"");
            t("failed ! ");
        }
    }

    private void signInWithFirebase(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if(task.isSuccessful()){
                            t("Success");
                            FirebaseUser user = task.getResult().getUser();
                            Bundle bundle = new Bundle();
                            bundle.putString("email",user.getEmail());
                            redirect(bundle);
                        }
                    }});

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

    private void redirect(Bundle bundle) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(bundle);
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
}
