package bxute.authenticationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginOptionsActivity extends AppCompatActivity {

    @BindView(R.id.emailOption)
    Button emailOption;
    @BindView(R.id.phoneOption)
    Button phoneOption;
    @BindView(R.id.googleOption)
    Button googleOption;
    @BindView(R.id.facebookOption)
    Button facebookOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);
        ButterKnife.bind(this);
        emailOption = (Button) findViewById(R.id.emailOption);
        phoneOption = (Button) findViewById(R.id.phoneOption);
        emailOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionsActivity.this, MainActivity.class));
                finish();

            }
        });

        phoneOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionsActivity.this, PhoneAuthActivity.class));
                finish();
            }
        });


        googleOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionsActivity.this, GoogleSignInActivity.class));
                finish();
            }
        });

        facebookOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionsActivity.this, FacebookAuthActivity.class));
                finish();
            }
        });
    }
}
