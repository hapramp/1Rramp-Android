package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.SteemRePoster;
import com.hapramp.ui.adapters.UserListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RebloggedListActivity extends AppCompatActivity {
  public static final String EXTRA_USER_LIST = "extra_user_list";
  public static final String EXTRA_AUTHOR = "author";
  public static final String EXTRA_PERMLINK = "permlink";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.userListView)
  ListView userListView;
  @BindView(R.id.rePostButton)
  TextView rePostButton;
  private String author;
  private String permlink;
  private ArrayList<String> mUsers;
  private SteemRePoster steemRePoster;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_list);
    ButterKnife.bind(this);
    progressDialog = new ProgressDialog(this);
    collectExtras();
    attachListeners();
  }

  private void collectExtras() {
    Intent intent = getIntent();
    if (intent != null) {
      author = intent.getStringExtra(EXTRA_AUTHOR);
      permlink = intent.getStringExtra(EXTRA_PERMLINK);
      mUsers = intent.getStringArrayListExtra(EXTRA_USER_LIST);
      setUsers(mUsers);
    }
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    rePostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAlertDialogForRepost();
      }
    });
  }

  private void setUsers(ArrayList<String> users) {
    UserListAdapter userListAdapter = new UserListAdapter(this);
    userListAdapter.setUsernames(users);
    userListView.setAdapter(userListAdapter);
    invalidateRepostButton();
  }

  private void showAlertDialogForRepost() {
    new AlertDialog.Builder(this)
      .setTitle("Repost?")
      .setMessage("This post will appear on your profile. This action cannot be reversed.")
      .setPositiveButton("Repost", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          repostThisPost();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void invalidateRepostButton() {
    String currentLoggedInUser = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (mUsers.contains(currentLoggedInUser) || author.equals(currentLoggedInUser)) {
      rePostButton.setVisibility(View.GONE);
    } else {
      rePostButton.setVisibility(View.VISIBLE);
    }
  }

  private void repostThisPost() {
    showProgressDialog(true, "Reposting....");
    steemRePoster = new SteemRePoster();
    steemRePoster.setRepostCallback(new SteemRePoster.RepostCallback() {
      @Override
      public void reposted() {
        showProgressDialog(false, "");
        Toast.makeText(RebloggedListActivity.this, "Reposted", Toast.LENGTH_LONG).show();
        finish();
      }

      @Override
      public void failedToRepost() {
        showProgressDialog(false, "");
        Toast.makeText(RebloggedListActivity.this, "Failed to Repost", Toast.LENGTH_LONG).show();
      }
    });
    steemRePoster.repost(author, permlink);
  }

  private void showProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    }
  }
}
