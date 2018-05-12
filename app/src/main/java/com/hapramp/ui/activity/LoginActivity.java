package com.hapramp.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.callbacks.login.LoginCallbacks;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.PixelUtils;
import com.hapramp.viewmodel.common.ConnectivityViewModel;
import com.hapramp.viewmodel.login.LoginViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginCallbacks {


    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int QR_CODE_REQUEST_CODE = 109;

    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.usernameTv)
    EditText usernameEt;
    @BindView(R.id.lock_icon)
    TextView lockIcon;
    @BindView(R.id.private_posting_key)
    EditText privatePostingKeyEt;
    @BindView(R.id.loginButton)
    TextView loginButton;
    @BindView(R.id.createSteemAccountBtn)
    TextView createSteemAccountBtn;
    @BindView(R.id.scanBtn)
    TextView scanBtn;
    @BindView(R.id.helpBtn)
    TextView helpBtn;
    @BindView(R.id.connectivityText)
    TextView connectivityText;

    ProgressDialog progressDialog;
    private String mUsername;
    private String mPPk;
    private ConnectivityViewModel connectivityViewModel;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkLastLoginAndMoveAhead();
        init();
        attachListeners();
    }

    private void checkLastLoginAndMoveAhead() {
        if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
            if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() == 0) {
                navigateToCommunityPage();
            } else {
                navigateToHomePage();
            }
        }
    }

    private void init() {
        Crashlytics.setString(CrashReporterKeys.UI_ACTION,"login init");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        lockIcon.setTypeface(typeface);
        userIcon.setTypeface(typeface);
        scanBtn.setTypeface(typeface);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        connectivityViewModel = ViewModelProviders.of(this).get(ConnectivityViewModel.class);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        connectivityViewModel.getConnectivityState().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isConnected) {
                if (isConnected) {
                    enableSigninButton();
                    showConnectivityBar();
                } else {
                    disableSigninButton();
                    showConnectivityErrorBar();
                }
            }
        });
    }

    private void attachListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, QrScanningActivity.class);
                startActivityForResult(i, QR_CODE_REQUEST_CODE);
            }
        });
        createSteemAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSteemitSignUp();
            }
        });
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginHelpDailog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_CODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getStringExtra(QrScanningActivity.EXTRAA_PPK).length() == 0) {
                    showToast(getString(R.string.invalid_posting_key));
                    return;
                }
                mPPk = data.getStringExtra(QrScanningActivity.EXTRAA_PPK);
                privatePostingKeyEt.setText(mPPk);
            } else {
                showToast(getString(R.string.cannot_read_qr_code));
            }
        } else {
            showToast(getString(R.string.cannot_read_qr_code));
        }
    }

    private void attemptLogin() {
        mUsername = usernameEt.getText().toString();
        mPPk = privatePostingKeyEt.getText().toString();
        loginViewModel.attemptLogin(mUsername, mPPk, this);
    }


    @Override
    public void onLoginFailed(String status) {
        hideProgress();
        showToast(status);
    }

    @Override
    public void onSignupFailed() {
        hideProgress();
        showToast(getString(R.string.singup_issue));
    }

    @Override
    public void onInvalidCredentials() {
        hideProgress();
        showToast(getString(R.string.invalid_credentials));
    }

    @Override
    public void onInvalidUsernameField(String msg) {
        showToast(msg);
    }

    @Override
    public void onInvalidPostingKey() {
        showToast(getString(R.string.invalid_posting_key));
    }

    @Override
    public void onProcesing(String msg) {
        showProgressWithMessage(msg);
    }

    @Override
    public void onLoginSuccess(boolean freshUser) {
        hideProgress();
        if (freshUser) {
            navigateToCommunityPage();
        } else {
            navigateToHomePage();
        }
    }


    private void navigateToHomePage() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void navigateToCommunityPage() {
        Intent i = new Intent(this, CommunitySelectionActivity.class);
        startActivity(i);
        finish();
    }

    private void showProgressWithMessage(String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void showLoginHelpDailog() {
        final Dialog dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.login_info_dialog_view, null);
        TextView cancel = v.findViewById(R.id.cancelHelpDialog);
        TextView gotoSteemIt = v.findViewById(R.id.gotoSteemit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        gotoSteemIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSteemitInNativeBrowser();
            }
        });
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setLayout((PixelUtils.getDimension(this).widthPixels), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void openSteemitInNativeBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_url)));
        startActivity(browserIntent);
    }

    private void openSteemitSignUp() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_signup)));
        startActivity(browserIntent);
    }

    private void showConnectivityErrorBar() {
        connectivityText.setVisibility(View.VISIBLE);
        connectivityText.setText(R.string.connectivity_lost);
        connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityRed));
    }

    private void showConnectivityBar() {
        connectivityText.setVisibility(View.VISIBLE);
        connectivityText.setText(R.string.connectivity_established);
        connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityGreen));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connectivityText.setVisibility(View.GONE);
            }
        }, 4000);
    }

    private void disableSigninButton() {
        loginButton.setEnabled(false);
    }

    private void enableSigninButton() {
        loginButton.setEnabled(true);
    }

}
