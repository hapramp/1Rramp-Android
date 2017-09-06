package bxute.authenticationapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookAuthActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_auth);
        ButterKnife.bind(this);
        dumpKeyHash();
        initFirebase();
        init();
    }

    private void dumpKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (
                PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void init() {
        Log.d("Facebook", "init();");
        loginButton.setReadPermissions("email", "read_custom_friendlists","public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mTracker;
            private AccessTokenTracker mAccessTokenTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Profile.getCurrentProfile() == null) {
                    mTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            mTracker.stopTracking();
                        }
                    };
                }

                mAccessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        if (currentAccessToken != null) {
                            graphRequest(currentAccessToken);
                        }
                    }
                };

                handleFacebookAcesss(AccessToken.getCurrentAccessToken());

            }

            @Override
            public void onCancel() {
                t("cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", error.toString());
                t("error" + error.toString());
            }
        });

        if(isLoggedIn()){
            Log.d("Facebook"," already login");
            handleFacebookAcesss(AccessToken.getCurrentAccessToken());
        }

    }

    private boolean isLoggedIn() {
        Log.d("Facebook", "token - " + AccessToken.getCurrentAccessToken());
        return AccessToken.getCurrentAccessToken() != null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // singned in
                    Bundle bundle = new Bundle();
                    bundle.putString("email", user.getEmail());
                    bundle.putString("id", user.getUid());
                    redirect(bundle);

                    Log.d("Facebook", "user is signed In.");
                } else {
                    // signed out
                    Log.d("Facebook", "user is signed out");
                }
            }
        };

    }

    private void handleFacebookAcesss(AccessToken token) {

        Log.d("Facebook", "token " + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Facebook"," task email -  "+task.getResult().getUser().getEmail());
                            FirebaseUser user = task.getResult().getUser();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", user.getEmail());
                            bundle.putString("id", user.getUid());
                            redirect(bundle);

                        } else {
                        }
                    }
                });
    }

    private void graphRequest(AccessToken token) {

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Facebook", response.toString());
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,friendlists,updated_time,last_name, email,gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
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
