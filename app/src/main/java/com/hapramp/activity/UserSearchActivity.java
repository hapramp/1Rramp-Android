package com.hapramp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.hapramp.adapters.ViewPagerAdapter;
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
    private static boolean SEARCH_MODE = false;

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.searchInput)
    EditText searchInput;
    @BindView(R.id.searchBtn)
    TextView searchBtn;
    @BindView(R.id.action_bar_container)
    RelativeLayout actionBarContainer;
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
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.suggestionsContainer)
    RelativeLayout suggestionsContainer;
    UserSuggestionListAdapter adapter;
    SearchManager searchManager;
    private Handler mHandler;
    private final String backTextIcon = "\uF04D";
    private final String closeSearchTextIcon = "\uF156";

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

    @Override
    protected void onResume() {
        super.onResume();
        setBrowseMode();
    }

    private void initView() {

        //set up view pager
        setupViewPager(viewpager);
        //set up tabs
        tabs.setupWithViewPager(viewpager);
        tabs.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        adapter = new UserSuggestionListAdapter(this);
        suggestionsListView.setAdapter(adapter);
        mHandler = new Handler();
        backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        searchBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void attachListener() {

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchTerm = searchInput.getText().toString();
                if (searchTerm.length() >= 0) {
                    fetchSuggestions(searchTerm);
                    setSearchMode();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SEARCH_MODE) {
                    setBrowseMode();
                } else {
                    close();
                }
            }
        });

    }

    private void setSearchMode() {

        SEARCH_MODE = true;

        // show suggestion
        if (suggestionsContainer != null) {
            suggestionsContainer.setVisibility(View.VISIBLE);
        }

        //change back icon to cross
        if (backBtn != null) {
            backBtn.setText(closeSearchTextIcon);
        }

    }

    private void setBrowseMode() {

        SEARCH_MODE = false;

        // hide suggestion
        if (suggestionsContainer != null) {
            suggestionsContainer.setVisibility(View.GONE);
        }

        //clear search view
        if (searchInput != null) {
            searchInput.setText("");
        }

        //change cross icon to back
        if (backBtn != null) {
            backBtn.setText(backTextIcon);
        }
    }

    private void close() {
        finish();
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
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
    }

    private void failedToFetchPlatformUsers() {
        //show error
    }

    private void fetchSuggestions(String query) {

        if (suggestionsContainer != null) {
            suggestionsContainer.setVisibility(View.VISIBLE);
        }

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

//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                suggestionsListView.setVisibility(View.GONE);
//                messagePanel.setVisibility(View.VISIBLE);
//                suggestionsProgressBar.setVisibility(View.VISIBLE);
//                messagePanel.setText("Preparing...");
//
//            }
//        });
    }

    @Override
    public void onPrepared() {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                suggestionsListView.setVisibility(View.GONE);
//                messagePanel.setVisibility(View.VISIBLE);
//                suggestionsProgressBar.setVisibility(View.GONE);
//                messagePanel.setText("Ready For Search!!");
//            }
//        });
    }

    @Override
    public void onSearching() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                suggestionsListView.setVisibility(View.GONE);
                messagePanel.setVisibility(View.VISIBLE);
                suggestionsProgressBar.setVisibility(View.VISIBLE);
                messagePanel.setText("Searching...");
            }
        });

    }

    @Override
    public void onSearched(final ArrayList<String> suggestions) {

        if (suggestionsContainer != null) {
            suggestionsContainer.setVisibility(View.VISIBLE);
        }

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
