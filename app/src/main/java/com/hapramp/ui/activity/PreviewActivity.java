package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.R;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.models.data.Content;
import com.hapramp.steem.models.data.FeedDataItemModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preview_activity);
  }

}
