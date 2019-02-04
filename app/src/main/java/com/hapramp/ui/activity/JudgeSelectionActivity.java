package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.datastore.SteemRequestBody;
import com.hapramp.models.JudgeModel;
import com.hapramp.models.LookupAccount;
import com.hapramp.ui.adapters.JudgeListAdapter;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.views.JudgeRemovableItemView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.hapramp.views.JudgeSelectionView.MAX_JUDGES_ALLOWED;

public class JudgeSelectionActivity extends AppCompatActivity implements JudgeListAdapter.JudgeListListener {
  public static final String EXTRA_SELECTED_JUDGES = "selected_judges";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.judges_list)
  RecyclerView judgesList;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.selected_judge_container)
  LinearLayout selectedJudgeContainer;
  @BindView(R.id.done_btn)
  TextView doneBtn;
  @BindView(R.id.selected_judge_wrapper)
  RelativeLayout selectedJudgeWrapper;
  @BindView(R.id.shadow)
  View shadow;
  @BindView(R.id.judge_search_bar)
  EditText judgeSearchBar;
  PublishSubject<String> publishSubject = PublishSubject.create();
  private JudgeListAdapter judgeListAdapter;
  private ArrayList<JudgeModel> selectedJudges;
  private ArrayList<JudgeModel> mAllJudges;
  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  private void setupSearch() {
    //Add text change watcher
    compositeDisposable.add(
      RxTextView.textChangeEvents(judgeSearchBar)
        .skipInitialValue()
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(getSearchInputObserver()));

    compositeDisposable.add(publishSubject
      .debounce(300, TimeUnit.MILLISECONDS)
      .distinctUntilChanged()
      .switchMapSingle(new Function<String, SingleSource<LookupAccount>>() {
        @Override
        public SingleSource<LookupAccount> apply(String username) {
          return RetrofitServiceGenerator
            .getService()
            .getUsernames(URLS.STEEMIT_API_URL, SteemRequestBody.lookupAccounts(username))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
        }
      }).subscribeWith(usernamesResponseObserver()));

    hideSearchingProgress();
  }

  private DisposableObserver<LookupAccount> usernamesResponseObserver() {
    return new DisposableObserver<LookupAccount>() {
      @Override
      public void onNext(LookupAccount accounts) {
        onUserSuggestionsAvailable(accounts.getmResult());
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    };
  }

  private void onUserSuggestionsAvailable(List<String> usernames) {
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
    updateJudgesSelectionInList(JudgeModel.getJudgeModelsFrom((ArrayList<String>) usernames));
    updateBottomBarView();
  }

  private DisposableObserver<TextViewTextChangeEvent> getSearchInputObserver() {
    return new DisposableObserver<TextViewTextChangeEvent>() {
      @Override
      public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
        String searchTerm = textViewTextChangeEvent.text().toString().trim().toLowerCase();
        if (searchTerm.length() > 0) {
          if (ConnectionUtils.isConnected(JudgeSelectionActivity.this)) {
            onSearchingUsernames();
            publishSubject.onNext(searchTerm);
          } else {
            Toast.makeText(JudgeSelectionActivity.this, "No Connectivity", Toast.LENGTH_LONG).show();
          }
        } else {
          hideKeyboardByDefault();
        }
      }

      @Override
      public void onError(Throwable e) {
        hideKeyboardByDefault();
      }

      @Override
      public void onComplete() {

      }
    };
  }

  private void hideKeyboardByDefault() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    judgeSearchBar.clearFocus();
  }

  private void onSearchingUsernames() {
    if(progressBar!=null){
      progressBar.setVisibility(View.VISIBLE);
    }
  }

  private void hideSearchingProgress(){
    if(progressBar!=null){
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_judge_selection);
    ButterKnife.bind(this);
    collectAlreadySelectedJudges();
    init();
    attachListeners();
    setupSearch();
  }

  private void collectAlreadySelectedJudges() {
    selectedJudges = new ArrayList<>();
    if (getIntent() != null) {
      selectedJudges = getIntent().getParcelableArrayListExtra(EXTRA_SELECTED_JUDGES);
    }
  }

  private void init() {
    judgeListAdapter = new JudgeListAdapter(this);
    judgeListAdapter.setJudgeListListener(this);
    judgesList.setLayoutManager(new LinearLayoutManager(this));
    judgesList.setAdapter(judgeListAdapter);
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        returnResult();
      }
    });

    doneBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        returnResult();
      }
    });
  }

  private void returnResult() {
    Intent intent = new Intent();
    intent.putParcelableArrayListExtra(EXTRA_SELECTED_JUDGES, selectedJudges);
    setResult(RESULT_OK, intent);
    finish();
  }

  private void updateJudgesSelectionInList(ArrayList<JudgeModel> judgeList) {
    this.mAllJudges = judgeList;
    for (int i = 0; i < judgeList.size(); i++) {
      boolean found = false;
      for (int j = 0; j < selectedJudges.size(); j++) {
        if (judgeList.get(i).getmUsername().equals(selectedJudges.get(j).getmUsername())) {
          found = true;
          break;
        }
      }
      if (found) {
        judgeList.get(i).setSelected(true);
      } else {
        judgeList.get(i).setSelected(false);
      }
    }
    judgeListAdapter.setJudges(judgeList);
  }

  @Override
  public void onAddJudge(JudgeModel judge) {
    addJudge(judge);
    updateBottomBarView();
    hideKeyboardByDefault();
  }

  @Override
  public void onRemoveJudge(JudgeModel judge) {
    removeJudge(judge);
    updateJudgesSelectionInList(mAllJudges);
    updateBottomBarView();
  }

  private void addJudge(JudgeModel judge) {
    if (selectedJudges.size() < MAX_JUDGES_ALLOWED) {
      selectedJudges.add(judge);
      judge.setSelected(true);
      updateJudgesSelectionInList(mAllJudges);
    } else {
      Toast.makeText(this, "Max " + MAX_JUDGES_ALLOWED + " judges allowed!", Toast.LENGTH_LONG).show();
    }
  }

  private void updateBottomBarView() {
    try {
      final int selected = selectedJudges.size();
      if (selected > 0) {
        //show bottom bar
        shadow.setVisibility(View.VISIBLE);
        selectedJudgeWrapper.setVisibility(View.VISIBLE);
        selectedJudgeContainer.removeAllViews();
        for (int i = 0; i < selected; i++) {
          JudgeRemovableItemView judgeRemovableItemView = new JudgeRemovableItemView(this);
          judgeRemovableItemView.setmJudgeRemoveListener(new JudgeRemovableItemView.JudgeRemoveListener() {
            @Override
            public void onRemoveJudge(JudgeModel judgeModel) {
              onJudgeRemovedFromBottomBarAt(judgeModel);
            }
          });
          judgeRemovableItemView.setJudge(selectedJudges.get(i), i);
          selectedJudgeContainer.addView(judgeRemovableItemView, i);
        }
      } else {
        //hide bottom bar
        shadow.setVisibility(View.GONE);
        selectedJudgeWrapper.setVisibility(View.GONE);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void onJudgeRemovedFromBottomBarAt(JudgeModel judgeModel) {
    removeJudge(judgeModel);
    updateJudgesSelectionInList(mAllJudges);
    updateBottomBarView();
  }

  private void removeJudge(JudgeModel judgeModel) {
    for (int i = 0; i < selectedJudges.size(); i++) {
      if (selectedJudges.get(i).getmUsername().equals(judgeModel.getmUsername())) {
        selectedJudges.remove(i);
      }
    }
  }
}
