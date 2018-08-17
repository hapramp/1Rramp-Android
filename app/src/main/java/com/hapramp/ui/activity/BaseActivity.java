package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class BaseActivity extends AppCompatActivity {
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
  }

  public void toast(String msg) {
    try {
      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }catch (Exception e){
      Crashlytics.log(e.toString());
    }
  }

  public void showProgressDialog(String msg, boolean show) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.show();
      } else {
        progressDialog.hide();
      }
    }
  }
}
