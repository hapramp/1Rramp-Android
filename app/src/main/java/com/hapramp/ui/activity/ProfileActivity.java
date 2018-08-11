package com.hapramp.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RawApiCaller;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.ui.adapters.ProfileRecyclerAdapter;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ViewItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// Activity for User Profile
public class ProfileActivity extends AppCompatActivity implements RawApiCaller.FeedDataCallback {
  private static final int POST_LIMIT = 100;
  @BindView(R.id.closeBtn)
  TextView closeBtn;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.profilePostRv)
  RecyclerView profilePostRv;
  @BindView(R.id.profile_user_name)
  TextView profileUserName;
  private String username;
  private ProfileRecyclerAdapter profilePostAdapter;
  private ViewItemDecoration viewItemDecoration;
  private LinearLayoutManager llm;
  private RawApiCaller rawApiCaller;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);
    init();
    attachListeners();
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_PROFILE, null);
  }

  private void init() {
    rawApiCaller = new RawApiCaller(this);
    rawApiCaller.setDataCallback(this);
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    if (getIntent() == null) {
      Toast.makeText(this, "No Username Passed", Toast.LENGTH_SHORT).show();
      return;
    }
    username = getIntent().getExtras().getString(Constants.EXTRAA_KEY_STEEM_USER_NAME, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    profilePostAdapter = new ProfileRecyclerAdapter(this, username);
    llm = new LinearLayoutManager(this);
    profilePostRv.setLayoutManager(llm);
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.post_item_divider_view);
    viewItemDecoration = new ViewItemDecoration(drawable);
    viewItemDecoration.setWantTopOffset(false, 0);
    profilePostRv.addItemDecoration(viewItemDecoration);
    profilePostRv.setAdapter(profilePostAdapter);
    setScrollListener();
    fetchPosts();
  }

  private void attachListeners() {
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void setScrollListener() {
    profilePostRv.addOnScrollListener(new EndlessOnScrollListener(llm) {
      @Override
      public void onScrolledToEnd() {
        //loadMore();
      }
    });
  }

  private void fetchPosts() {
    rawApiCaller.requestUserBlogs(username);
  }

  @Override
  public void onDataLoaded(ArrayList<Feed> feeds) {
    profilePostAdapter.setPosts(feeds);
  }

  @Override
  public void onDataLoadError() {

  }

  public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager lm;
    EndlessOnScrollListener(LinearLayoutManager llm) {
      this.lm = llm;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      if (!recyclerView.canScrollVertically(1)) {
        onScrolledToEnd();
      }
    }
    public abstract void onScrolledToEnd();
  }
}
