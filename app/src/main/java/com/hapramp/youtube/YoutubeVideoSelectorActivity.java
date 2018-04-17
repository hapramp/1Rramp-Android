package com.hapramp.youtube;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.utils.FontManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YoutubeVideoSelectorActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_selector);
        ButterKnife.bind(this);
        initView();
        attachListener();

    }

    private void initView() {

        resultAdapter = new YoutubeResultAdapter(this);
        youtubeResultsRv.setLayoutManager(new LinearLayoutManager(this));
        youtubeResultsRv.setAdapter(resultAdapter);

        backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        searchBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    private void attachListener() {

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchTerm = searchInput.getText().toString();
                if (searchTerm.length() >= 0) {
                    getResultsFor(searchTerm);
                }

            }
        });

    }

    private void getResultsFor(String term) {

        showProgressBar();

        String url = "http://api.anyaudio.in/api/v1/search?q=" + term;
        RetrofitServiceGenerator.getService().getYoutubeResults(url).enqueue(new Callback<YoutubeResultModel>() {
            @Override
            public void onResponse(Call<YoutubeResultModel> call, Response<YoutubeResultModel> response) {
                if (response.isSuccessful()) {
                    bindResults(response.body().getResults());
                }else{
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<YoutubeResultModel> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    private void bindResults(List<YoutubeResultModel.Result> results) {
        hideProgressBar();
        resultAdapter.setYoutubeResults(results);
    }

    private void hideProgressBar() {
        if (suggestionsProgressBar!=null){
            youtubeResultsRv.setVisibility(View.VISIBLE);
            suggestionsProgressBar.setVisibility(View.GONE);
        }
    }

    private void showProgressBar(){
        if (suggestionsProgressBar!=null){
            youtubeResultsRv.setVisibility(View.GONE);
            suggestionsProgressBar.setVisibility(View.VISIBLE);
        }
    }

}
