package com.hapramp.youtube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.YoutubeSuggestionsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeVideoSelectorActivity extends AppCompatActivity implements YoutubeSuggestionsHelper.SuggestionsCallback, YoutubeResultAdapter.VideoItemClickListener {

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
    @BindView(R.id.youtube_results_rv)
    RecyclerView youtubeResultsRv;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.suggestionsProgressBar)
    ProgressBar suggestionsProgressBar;
    private YoutubeResultAdapter resultAdapter;
    private YoutubeSuggestionsHelper youtubeSuggestionsHelper;
    private ArrayList<String> suggestions;
    public static final String EXTRA_VIDEO_KEY = "video_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_selector);
        ButterKnife.bind(this);
        init();
        attachListener();
        AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_YOUTUBE_SEARCH,null);
    }

    private void init() {
        resultAdapter = new YoutubeResultAdapter(this);
        youtubeResultsRv.setLayoutManager(new LinearLayoutManager(this));
        youtubeResultsRv.setAdapter(resultAdapter);
        backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        searchBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        youtubeSuggestionsHelper = YoutubeSuggestionsHelper.getInstance(this);
        youtubeSuggestionsHelper.setSuggestionsCallback(this);
    }

    public void sendResultBackToParent(String videoId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_VIDEO_KEY, videoId);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void fetchSuggestions(String query) {
        youtubeSuggestionsHelper.findSuggestion(query);
    }

    private void close() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    private void attachListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString();
                if (s.length() > 0) {
                    fetchSuggestions(q);
                }
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
                    getResultsFor(searchTerm);
                }
            }
        });

        suggestionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getResultsFor(suggestions.get(position));
            }
        });

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchTerm = searchInput.getText().toString();
                    if (searchTerm.length() >= 0) {
                        getResultsFor(searchTerm);
                    }
                    return true;
                }
                return false;
            }
        });
        resultAdapter.setVideoItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void getResultsFor(String term) {
        showProgressBar();
        hideYoutubeSuggestions();
        hideYoutubeResults();
        String url = "http://api.anyaudio.in/api/v1/search?q=" + term;
        RetrofitServiceGenerator.getService().getYoutubeResults(url).enqueue(new Callback<YoutubeResultModel>() {
            @Override
            public void onResponse(Call<YoutubeResultModel> call, Response<YoutubeResultModel> response) {
                if (response.isSuccessful()) {
                    bindResults(response.body().getResults());
                } else {
                    hideProgressBar();
                }
            }
            @Override
            public void onFailure(Call<YoutubeResultModel> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    private void bindSuggestions(ArrayList<String> suggestions) {
        suggestionsListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestions));
    }

    private void bindResults(List<YoutubeResultModel.Result> results) {
        hideProgressBar();
        showYoutubeResults();
        resultAdapter.setYoutubeResults(results);
        AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_YOUTUBE);
    }

    private void hideProgressBar() {
        if (suggestionsProgressBar != null) {
            suggestionsProgressBar.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (suggestionsProgressBar != null) {
            suggestionsProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showYoutubeResults() {
        if (youtubeResultsRv != null) {
            youtubeResultsRv.setVisibility(View.VISIBLE);
        }
    }

    private void hideYoutubeResults() {
        if (youtubeResultsRv != null) {
            youtubeResultsRv.setVisibility(View.GONE);
        }
    }

    private void showYoutubeSuggestions() {
        if (suggestionsListView != null) {
            suggestionsListView.setVisibility(View.VISIBLE);
        }
    }

    private void hideYoutubeSuggestions() {
        if (suggestionsListView != null) {
            suggestionsListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFetching() {
        hideYoutubeResults();
        hideYoutubeSuggestions();
        showProgressBar();
    }

    @Override
    public void onSuggestionsFetched(ArrayList<String> suggestions) {
        showYoutubeSuggestions();
        hideYoutubeResults();
        hideProgressBar();
        this.suggestions = suggestions;
        bindSuggestions(suggestions);
    }

    @Override
    public void onClicked(String video_id) {
        sendResultBackToParent(video_id);
    }
}
