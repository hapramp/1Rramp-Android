package com.hapramp.model;

import com.google.gson.Gson;
import com.hapramp.api.DataServer;
import com.hapramp.datamodels.requests.SteemLoginResponseModel;
import com.hapramp.datamodels.requests.SteemSignupRequestModel;
import com.hapramp.datamodels.response.SteemLoginRequestModel;
import com.hapramp.datamodels.response.SteemSignUpResponseModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.SteemHelper;
import com.hapramp.ui.callbacks.LoginCallbacks;
import com.hapramp.utils.ErrorCodes;
import com.hapramp.utils.HashGenerator;

import java.net.SocketTimeoutException;

import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 5/6/2018.
 */

public class LoginManager {

    private LoginCallbacks loginCallbacks;
    private String username;
    private String ppk;
    private Permlink mCommentParentLink;

    public void attemptLogin(String username, String ppk, final LoginCallbacks loginCallbacks) {
        this.loginCallbacks = loginCallbacks;
        this.username = username;
        this.ppk = ppk;
        if (!validateFields())
            return;

        loginCallbacks.onProcesing("Logging you in...");
        final SteemLoginRequestModel requestModel = new SteemLoginRequestModel();
        requestModel.setPpkHash(HashGenerator.getSHA2(ppk));
        requestModel.setUsername(username);

        DataServer.getService().login(requestModel).enqueue(new Callback<SteemLoginResponseModel>() {
            @Override
            public void onResponse(Call<SteemLoginResponseModel> call, Response<SteemLoginResponseModel> response) {
                if (response.isSuccessful()) {
                    onLoginSuccess(response.body());
                } else if (response.code() == ErrorCodes.NOT_FOUND) {
                    attemptSignup();
                } else if (response.code() == ErrorCodes.INTERNAL_SERVER_ERROR) {
                    loginCallbacks.onLoginFailed("Internal Server");
                }else if(response.code() == ErrorCodes.NOT_AUTHORIZED){
                    loginCallbacks.onLoginFailed("Not authorized");
                }
            }

            @Override
            public void onFailure(Call<SteemLoginResponseModel> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    loginCallbacks.onLoginFailed("Server Timeout, Try again");
                } else {
                    loginCallbacks.onLoginFailed(t.toString());
                }
            }
        });
    }


    // confirming from server if it exists there or not
    private void attemptSignup() {
        loginCallbacks.onProcesing("Attempting Signup...");
        SteemSignupRequestModel signupRequestModel = new SteemSignupRequestModel(username);
        DataServer.getService().signup(signupRequestModel).enqueue(new Callback<SteemSignUpResponseModel>() {
            @Override
            public void onResponse(Call<SteemSignUpResponseModel> call, Response<SteemSignUpResponseModel> response) {
                if (response.isSuccessful()) {
                    attemptComment(response.body().getToken());
                } else {
                    loginCallbacks.onSignupFailed();
                }
            }
            @Override
            public void onFailure(Call<SteemSignUpResponseModel> call, Throwable t) {
                loginCallbacks.onSignupFailed();
            }
        });
    }


    private boolean validateFields() {
        if (username.length() == 0) {
            loginCallbacks.onInvalidUsernameField("Username cannot be blank!");
            return false;
        }
        if (ppk.length() == 0) {
            loginCallbacks.onInvalidPostingKey();
            return false;
        }
        return true;
    }


    private void onLoginSuccess(SteemLoginResponseModel body) {
        HaprampPreferenceManager.getInstance().saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(body.getmCommunities())));
        loginCallbacks.onLoginSuccess(body.getmCommunities().size() == 0);
        saveUserAndPpkToPreference();
    }


    private void saveUserAndPpkToPreference() {
        HaprampPreferenceManager.getInstance().saveUserNameAndPpk(username, ppk);
        HaprampPreferenceManager.getInstance().setLoggedIn(true);
    }

    // commenting on a test post on blockchain to veryfy user
    private void attemptComment(final String token) {
        loginCallbacks.onProcesing("Contacting with Blockchain....");
        new Thread() {
            @Override
            public void run() {
                try {
                    final CommentOperation commentOperation = SteemHelper.getSteemInstance(username, ppk)
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
        loginCallbacks.onProcesing("Veryfying Blockchain info...");
        confirmCommentToServer();
        if (mCommentParentLink != null) {
            deleteComment(mCommentParentLink);
        }
    }

    // send confirmation of comment to our server
    private void confirmCommentToServer() {
        final SteemLoginRequestModel requestModel = new SteemLoginRequestModel();
        requestModel.setPpkHash(HashGenerator.getSHA2(ppk));
        requestModel.setUsername(username);
        DataServer.getService().signupDone(requestModel).enqueue(new Callback<SteemLoginResponseModel>() {
            @Override
            public void onResponse(Call<SteemLoginResponseModel> call, Response<SteemLoginResponseModel> response) {
                if (response.isSuccessful()) {
                    attemptLogin(username, ppk, loginCallbacks);
                } else if (response.code() == ErrorCodes.NOT_AUTHORIZED) {
                    loginCallbacks.onInvalidCredentials();
                }
            }
            @Override
            public void onFailure(Call<SteemLoginResponseModel> call, Throwable t) {
                onCommentConfirmationFailed(t.toString());
            }
        });
    }


    private void onCommentConfirmationFailed(String e) {
        loginCallbacks.onLoginFailed("Connection Failed To Server +" + e);
    }

    //delete the dummy-comment
    private void deleteComment(final Permlink parentPermlink) {
        new Thread() {
            @Override
            public void run() {
                try {
                    SteemHelper.getSteemInstance(username, ppk).deletePostOrComment(parentPermlink);
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
}
