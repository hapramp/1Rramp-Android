package com.hapramp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.BuildConfig;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.models.requests.SteemLoginResponseModel;
import com.hapramp.models.requests.SteemSignupRequestModel;
import com.hapramp.models.response.SteemLoginRequestModel;
import com.hapramp.models.response.SteemSignUpResponseModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.SteemHelper;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.ErrorCodes;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.HashGenerator;
import com.hapramp.utils.PixelUtils;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final long COMMENT_DELAY = 6000;
    private static final int QR_CODE_REQUEST_CODE = 109;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.user_icon)
    TextView userIcon;
    @BindView(R.id.username)
    EditText usernameEt;
    @BindView(R.id.usernameHolder)
    LinearLayout usernameHolder;
    @BindView(R.id.lock_icon)
    TextView lockIcon;
    @BindView(R.id.private_posting_key)
    EditText privatePostingKeyEt;
    @BindView(R.id.privateKeyHolder)
    RelativeLayout privateKeyHolder;
    @BindView(R.id.loginButton)
    TextView loginButton;
    @BindView(R.id.createSteemAccountBtn)
    TextView createSteemAccountBtn;
    ProgressDialog progressDialog;
    @BindView(R.id.scanBtn)
    TextView scanBtn;
    @BindView(R.id.helpBtn)
    TextView helpBtn;
    @BindView(R.id.connectivityText)
    TextView connectivityText;

    private String mUsername;
    private String mPPk;

    private Handler mHandler;
    private Permlink mCommentParentLink;
    private boolean networkChangeReceiverRegistered;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkLastLoginAndMoveAhead();
        init();

    }

    private void checkLastLoginAndMoveAhead() {
        if(HaprampPreferenceManager.getInstance().isLoggedIn()){
            navigateToHomePage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!networkChangeReceiverRegistered) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }

            networkChangeReceiverRegistered = true;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (networkChangeReceiverRegistered) {
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiverRegistered = false;
        }

    }

    private void init() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        networkChangeReceiver = new NetworkChangeReceiver();

        Typeface typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        lockIcon.setTypeface(typeface);
        userIcon.setTypeface(typeface);
        scanBtn.setTypeface(typeface);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mHandler = new Handler();

        usernameEt.setText("unittestaccount");
        //}

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

                if (data.getStringExtra("ppk").length() < 50) {
                    showToast("Invalid Posting Key");
                    return;
                }

                mPPk = data.getStringExtra("ppk");
                privatePostingKeyEt.setText(mPPk);

            } else {
                showToast("Cannot Read QR");
            }
        } else {
            showToast("Cannot Read QR");
        }

    }

    private boolean validateFields() {

        if (mUsername.length() == 0) {
            showToast("Username Cannot Be Blank!");
            usernameEt.requestFocus();
            return false;
        }

        if (mPPk.length() < 50) {
            showToast("Invalid Posting Key");
            privatePostingKeyEt.requestFocus();
            return false;
        }

        return true;
    }

    // attempt to log into our server
    private void attemptLogin() {

        mUsername = usernameEt.getText().toString();
        mPPk = privatePostingKeyEt.getText().toString();

        if (!validateFields())
            return;

        showProgressWithMessage("Logging You In...");
        final SteemLoginRequestModel requestModel = new SteemLoginRequestModel();
        requestModel.setPpkHash(HashGenerator.getSHA2(mPPk));
        requestModel.setUsername(mUsername);

        DataServer.getService().login(requestModel).enqueue(new Callback<SteemLoginResponseModel>() {
            @Override
            public void onResponse(Call<SteemLoginResponseModel> call, Response<SteemLoginResponseModel> response) {

                if (response.isSuccessful()) {

                    onLoginSuccess(response.body());

                } else if (response.code() == ErrorCodes.NOT_FOUND) {

                    attemptSignup();

                } else if (response.code() == ErrorCodes.INTERNAL_SERVER_ERROR) {

                    onLoginFailed("Internal Server");

                }
            }

            @Override
            public void onFailure(Call<SteemLoginResponseModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    onLoginFailed("Server Timeout, Try again");
                } else {
                    onLoginFailed(t.toString());
                }
            }
        });

    }

    // confirming from server if it exists there or not
    private void attemptSignup() {

        showProgressWithMessage("Attempting Signup...");

        SteemSignupRequestModel signupRequestModel = new SteemSignupRequestModel(mUsername);

        DataServer.getService().signup(signupRequestModel).enqueue(new Callback<SteemSignUpResponseModel>() {
            @Override
            public void onResponse(Call<SteemSignUpResponseModel> call, Response<SteemSignUpResponseModel> response) {

                if (response.isSuccessful()) {

                    attemptComment(response.body().getToken());

                } else {

                    onSignUpFailed();

                }
            }

            @Override
            public void onFailure(Call<SteemSignUpResponseModel> call, Throwable t) {
                onSignUpFailed();
            }
        });

    }

    // user is not confirmed from server
    private void onSignUpFailed() {

        hideProgress();
        showToast("Something Went Wrong While Signing Up");
    }

    // commenting on a test post on blockchain to veryfy user
    private void attemptComment(final String token) {

        showProgressWithMessage("Contacting with Blockchain....");

        new Thread() {

            @Override
            public void run() {
                try {

                    final CommentOperation commentOperation = SteemHelper.getSteemInstance(mUsername, mPPk)
                            .createComment(
                                    new AccountName("the-dragon"),
                                    new Permlink("say-hello-to-hapramp"),
                                    token,
                                    new String[]{""}
                            );

                    mCommentParentLink = commentOperation.getParentPermlink();

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // issue confirm request via handler
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressWithMessage("Veryfying Blockchain info...");
                confirmCommentToServer();

                if (mCommentParentLink != null) {
                    deleteComment(mCommentParentLink);
                }

            }
        }, COMMENT_DELAY);

    }

    // send confirmation of comment to our server
    private void confirmCommentToServer() {

        final SteemLoginRequestModel requestModel = new SteemLoginRequestModel();
        requestModel.setPpkHash(HashGenerator.getSHA2(mPPk));
        requestModel.setUsername(mUsername);

        DataServer.getService().signupDone(requestModel).enqueue(new Callback<SteemLoginResponseModel>() {
            @Override
            public void onResponse(Call<SteemLoginResponseModel> call, Response<SteemLoginResponseModel> response) {

                if (response.isSuccessful()) {

                    attemptLogin();

                } else if (response.code() == ErrorCodes.NOT_AUTHORIZED) {

                    onInvalidCredentials();

                }

            }

            @Override
            public void onFailure(Call<SteemLoginResponseModel> call, Throwable t) {

                onCommentConfirmationFailed(t.toString());

            }
        });

    }

    //delete the dummy-comment
    private void deleteComment(final Permlink parentPermlink) {

        new Thread() {
            @Override
            public void run() {
                try {
                    SteemHelper.getSteemInstance(mUsername, mPPk).deletePostOrComment(parentPermlink);
                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    // Events

    private void onCommentConfirmationFailed(String e) {

        hideProgress();
        showToast("Connection Failed To Server +" + e);

    }

    private void onInvalidCredentials() {

        hideProgress();
        showToast("Invalid Credentials");

    }

    private void onLoginFailed(String status) {

        hideProgress();
        showToast(status);

    }

    private void onLoginSuccess(SteemLoginResponseModel body) {

        hideProgress();
        saveUserAndPpkToPreference();
        //if (body.getmCommunities().size() == 0) {
            navigateToCommunityPage();
//        } else {
//            HaprampPreferenceManager.getInstance().saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(body.getmCommunities())));
//            navigateToHomePage();
//            //navigateToPostCreatePage();
//        }

    }

    private void navigateToHomePage() {

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();

    }

    private void navigateToPostCreatePage() {

        Intent i = new Intent(this, PostCreateActivity.class);
        startActivity(i);
        finish();

    }

    private void navigateToCommunityPage() {

        Intent i = new Intent(this, CommunitySelectionActivity.class);
        startActivity(i);
        finish();
    }

    private void saveUserAndPpkToPreference() {

        HaprampPreferenceManager.getInstance().saveUserNameAndPpk(mUsername, mPPk);
        HaprampPreferenceManager.getInstance().setLoggedIn(true);

    }

    // User Feedback Methods

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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://steemit.com/login.html"));
        startActivity(browserIntent);
    }

    private void openSteemitSignUp() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://signup.steemit.com/"));
        startActivity(browserIntent);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                if (ConnectionUtils.isConnected(LoginActivity.this)) {
                    enableSigninButton();
                    showConnectivityBar();
                } else {
                    disableSigninButton();
                    showConnectivityErrorBar();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    private void showConnectivityErrorBar() {

        connectivityText.setVisibility(View.VISIBLE);
        connectivityText.setText("Connectivity Lost!");
        connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityRed));

    }

    private void showConnectivityBar() {

        connectivityText.setVisibility(View.VISIBLE);
        connectivityText.setText("Connection established !");
        connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityGreen));
        // hide after 4 sec
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
