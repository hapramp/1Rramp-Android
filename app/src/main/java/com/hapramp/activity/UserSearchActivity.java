package com.hapramp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.adapters.UserSuggestionListAdapter;
import com.hapramp.models.UserModelWrapper;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.search.SearchManager;
import com.hapramp.steem.FollowApiObjectWrapper;
import com.hapramp.utils.FontManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.apis.follow.enums.FollowType;
import eu.bittrade.libs.steemj.apis.follow.model.FollowApiObject;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

public class UserSearchActivity extends AppCompatActivity implements SearchManager.SearchListener {

    public static final String TAG = UserSearchActivity.class.getSimpleName();
    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.searchInput)
    EditText searchInput;
    @BindView(R.id.searchBtn)
    TextView searchBtn;
    @BindView(R.id.action_bar_container)
    RelativeLayout actionBarContainer;
    @BindView(R.id.countTv)
    TextView countTv;
    @BindView(R.id.suggestionsListView)
    ListView suggestionsListView;
    @BindView(R.id.suggestionsProgressBar)
    ProgressBar suggestionsProgressBar;
    @BindView(R.id.messagePanel)
    TextView messagePanel;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.appBar)
    RelativeLayout appBar;

    private boolean loadedUserFromAppServer;
    UserSuggestionListAdapter adapter;
    SearchManager searchManager;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        attachListener();
        initSearchManager();
        fetchFollowingsAndCache();
    }

    private void initView() {

        adapter = new UserSuggestionListAdapter(this);
        suggestionsListView.setAdapter(adapter);
        mHandler = new Handler();
        backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        searchBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void attachListener() {

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchTerm = searchInput.getText().toString();
                    if (searchTerm.length() >= 0) {
                        fetchSuggestions(searchTerm);
                    }
                    return true;
                }
                return false;
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchInput.getText().toString();
                if (searchTerm.length() >= 0) {
                    fetchSuggestions(searchTerm);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

    }

    private void close(){
        finish();
        overridePendingTransition(R.anim.slide_left_enter,R.anim.slide_left_exit);
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
                suggestionsProgressBar.setVisibility(View.VISIBLE);
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
                suggestionsProgressBar.setVisibility(View.GONE);
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
                suggestionsProgressBar.setVisibility(View.VISIBLE);
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
                suggestionsProgressBar.setVisibility(View.GONE);
                // countTv.setVisibility(View.VISIBLE);
                // countTv.setText(suggestions.size() + " Result Found!");
                adapter.setUsernames(suggestions);
            }
        });
    }
}
