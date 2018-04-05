package com.hapramp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.adapters.UserSuggestionListAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.UserModelWrapper;
import com.hapramp.models.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.search.SearchManager;
import com.hapramp.steem.models.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearchActivity extends AppCompatActivity implements SearchManager.SearchListener {

    private boolean loadedUserFromAppServer;
    ListView suggestionsListView;
    ProgressBar suggestionProgressBar;
    TextView messagePanel;
    UserSuggestionListAdapter adapter;
    SearchManager searchManager;
    TextView countTv;
    private SearchManager.SearchBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initSearchManager();
        fetchUserListFromAppServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_page_menu, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchSuggestions(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    fetchSuggestions(newText);
                }
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                suggestionsListView.setVisibility(View.GONE);
                messagePanel.setVisibility(View.GONE);
                countTv.setVisibility(View.GONE);
                suggestionProgressBar.setVisibility(View.GONE);

                return false;
            }
        });

        return true;
    }

    private void initView() {
        suggestionsListView = findViewById(R.id.suggestionsListView);
        suggestionProgressBar = findViewById(R.id.suggestionsProgressBar);
        messagePanel = findViewById(R.id.messagePanel);
        countTv = findViewById(R.id.countTv);
        adapter = new UserSuggestionListAdapter(this);
        suggestionsListView.setAdapter(adapter);
        builder = SearchManager.Builder();
    }

    private void initSearchManager() {

        //load from cache
        if (isUserCacheAvailable()) {
            searchManager = builder.feedWords(getUsers())
                    .setSearchCallback(this)
                    .build();
        } else {
            onNoUserCache();
        }
    }

    private void fetchUserListFromAppServer() {

        RetrofitServiceGenerator.getService().getAllUsersOnPlatform().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    updateUserCache(response.body());
                    loadedUserFromAppServer = true;
                } else {
                    failedToFetchPlatformUsers();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                failedToFetchPlatformUsers();
            }
        });

    }

    private void updateUserCache(List<UserModel> response) {

        String userJson = new Gson().toJson(new UserModelWrapper(response));
        HaprampPreferenceManager.getInstance().saveAllPlatformUserAsJson(userJson);
        updateSearchManager();

    }

    private void updateSearchManager() {
        initSearchManager();
    }

    private boolean isUserCacheAvailable() {
        return HaprampPreferenceManager.getInstance().getAllPlatformUserAsJson().length() > 0;
    }

    public ArrayList<String> getUsers() {

        UserModelWrapper userModelWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getAllPlatformUserAsJson(), UserModelWrapper.class);
        ArrayList<String> usernames = new ArrayList<>();
        for (int i = 0; i < userModelWrapper.getUsers().size(); i++) {
            usernames.add(userModelWrapper.getUsers().get(i).getUsername());
        }
        return usernames;

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

    @Override
    public void onPreparing() {

        suggestionsListView.setVisibility(View.GONE);
        messagePanel.setVisibility(View.VISIBLE);
        suggestionProgressBar.setVisibility(View.VISIBLE);

        messagePanel.setText("Preparing...");

    }

    @Override
    public void onPrepared() {

        suggestionsListView.setVisibility(View.GONE);
        messagePanel.setVisibility(View.VISIBLE);
        suggestionProgressBar.setVisibility(View.GONE);
        messagePanel.setText("Ready For Search!!");
        countTv.setVisibility(View.GONE);

    }

    @Override
    public void onSearching() {

        suggestionsListView.setVisibility(View.GONE);
        messagePanel.setVisibility(View.VISIBLE);
        suggestionProgressBar.setVisibility(View.VISIBLE);
        countTv.setVisibility(View.VISIBLE);
        countTv.setVisibility(View.GONE);
        messagePanel.setText("Searching...");
    }

    @Override
    public void onSearched(ArrayList<String> suggestions) {

        suggestionsListView.setVisibility(View.VISIBLE);
        messagePanel.setVisibility(View.GONE);
        suggestionProgressBar.setVisibility(View.GONE);
        countTv.setVisibility(View.VISIBLE);
        countTv.setText(suggestions.size()+" Result Found!");
        adapter.setWords(suggestions);

    }
}
