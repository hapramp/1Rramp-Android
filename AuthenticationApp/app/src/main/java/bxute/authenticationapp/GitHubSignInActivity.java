package bxute.authenticationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GitHubSignInActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = GitHubSignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub_sign_in);
    }


    private void t(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void redirect(Bundle bundle) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void init(){
        progressDialog = new ProgressDialog(this);
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

                    Log.d(TAG, "user is signed In.");
                } else {
                    // signed out

                    Log.d(TAG, "user is signed out");
                }
            }
        };

    }

}
