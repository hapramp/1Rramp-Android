package com.hapramp.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.adapters.UserSuggestionListAdapter;
import com.hapramp.models.UserModelWrapper;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.search.SearchManager;
import com.hapramp.steem.FollowApiObjectWrapper;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class UserSearchActivity extends AppCompatActivity implements SearchManager.SearchListener {

    public static final String TAG = UserSearchActivity.class.getSimpleName();

    private boolean loadedUserFromAppServer;
    ListView suggestionsListView;
    ProgressBar suggestionProgressBar;
    TextView messagePanel;
    EditText searchInputBox;
    UserSuggestionListAdapter adapter;
    SearchManager searchManager;
    TextView countTv;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        attachListener();
        initSearchManager();
        fetchFollowingsAndCache();
    }

    private void initView() {

        suggestionsListView = findViewById(R.id.suggestionsListView);
        suggestionProgressBar = findViewById(R.id.suggestionsProgressBar);
        messagePanel = findViewById(R.id.messagePanel);
        countTv = findViewById(R.id.countTv);
        searchInputBox = findViewById(R.id.searchInput);
        adapter = new UserSuggestionListAdapter(this);
        suggestionsListView.setAdapter(adapter);
        mHandler = new Handler();

    }

    private void attachListener() {
        searchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 0) {
                    fetchSuggestions(s.toString());
                }
                //fetchSuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initSearchManager() {
        searchManager = new SearchManager(this);
    }


    private void updateUserCache(List<UserModel> response) {

        String userJson = new Gson().toJson(new UserModelWrapper(response));
        HaprampPreferenceManager.getInstance().saveAllPlatformUserAsJson(userJson);
        updateSearchManager();

    }

    private void updateSearchManager() {
        initSearchManager();
    }

    private void onNoUserCache() {
        if (loadedUserFromAppServer) {
            //show error
        } else {
            //show progress
        }
    }

    private void failedToFetchPlatformUsers() {
        //show error
    }


    private void fetchSuggestions(String query) {
        searchManager.requestSuggestionsFor(query);
    }

    private void fetchFollowingsAndCache() {

        final String follower = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        final String startFollower = "";
        final FollowType followType = FollowType.BLOG;
        final short limit = 1000;

        onPreparing();

        new Thread() {
            @Override
            public void run() {
                try {

                    SteemJ steemJ = new SteemJ();
                    List<FollowApiObject> followApiObjects = steemJ.getFollowing(new AccountName(follower), new AccountName(startFollower), followType, limit);
                    //Log.d(TAG,"Followings : "+followApiObjects.toString());
                    HaprampPreferenceManager.getInstance().saveCurrentUserFollowingsAsJson(new Gson().toJson(new FollowApiObjectWrapper(followApiObjects)));
                    onPrepared();

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onPreparing() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                suggestionsListView.setVisibility(View.GONE);
                messagePanel.setVisibility(View.VISIBLE);
                suggestionProgressBar.setVisibility(View.VISIBLE);
                messagePanel.setText("Preparing...");

            }
        });
    }

    @Override
    public void onPrepared() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                suggestionsListView.setVisibility(View.GONE);
                messagePanel.setVisibility(View.VISIBLE);
                suggestionProgressBar.setVisibility(View.GONE);
                messagePanel.setText("Ready For Search!!");
                countTv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSearching() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                suggestionsListView.setVisibility(View.GONE);
                messagePanel.setVisibility(View.VISIBLE);
                suggestionProgressBar.setVisibility(View.VISIBLE);
                countTv.setVisibility(View.VISIBLE);
                countTv.setVisibility(View.GONE);
                messagePanel.setText("Searching...");
            }
        });

    }

    @Override
    public void onSearched(final ArrayList<String> suggestions) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                suggestionsListView.setVisibility(View.VISIBLE);
                messagePanel.setVisibility(View.GONE);
                suggestionProgressBar.setVisibility(View.GONE);
                countTv.setVisibility(View.VISIBLE);
                countTv.setText(suggestions.size() + " Result Found!");
                adapter.setUsernames(suggestions);
            }
        });
    }
}
