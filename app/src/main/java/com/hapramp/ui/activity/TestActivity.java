package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.R;
import com.hapramp.views.hashtag.CustomHashTagInput;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    ButterKnife.bind(this);
  }
}
