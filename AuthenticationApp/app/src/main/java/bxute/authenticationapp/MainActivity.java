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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String TAG = MainActivity.class.getSimpleName();
    private EditText emailEt;
    private EditText passwordEt;
    private Button signIButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {

        progressDialog = new ProgressDialog(this);
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.password);
        signIButton = (Button) findViewById(R.id.SignIn);
        signUpButton = (Button) findViewById(R.id.SignUp);

        signIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Logging In...");
                signIn(emailEt.getText().toString(), passwordEt.getText().toString());
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("Registering...");
                Log.d(TAG, "clicked register");
                createAccount(emailEt.getText().toString(), passwordEt.getText().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();

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

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createAccount: onComplete- " + task.isSuccessful());
                            FirebaseUser user = task.getResult().getUser();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", user.getEmail());
                            bundle.putString("id", user.getUid());
                            redirect(bundle);
                        } else {
                            t(task.getException().toString());
                            Log.d(TAG, "failed to sign in");
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "failed create account " + e.toString());
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgress();
                Log.d(TAG, "signIn : onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    FirebaseUser user = task.getResult().getUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("email", user.getEmail());
                    bundle.putString("id", user.getUid());
                    redirect(bundle);
                } else {
                    t(task.getException().toString());
                    Log.w(TAG, "signIn :failed:", task.getException());
                }
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
}
