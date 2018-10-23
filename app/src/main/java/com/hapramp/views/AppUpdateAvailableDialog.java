package com.hapramp.views;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppUpdateAvailableDialog extends Dialog {
  public AppCompatActivity appCompatActivity;
  public Dialog dialog;
  @BindView(R.id.update_title)
  TextView updateTitle;
  @BindView(R.id.not_now_btn)
  TextView notNowBtn;
  @BindView(R.id.update_btn)
  TextView updateBtn;

  public AppUpdateAvailableDialog(@NonNull AppCompatActivity appCompatActivity) {
    super(appCompatActivity);
    this.appCompatActivity = appCompatActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.app_update_available_dialog);
    ButterKnife.bind(this);
    setListeners();
    setCancelable(false);
  }

  private void setListeners() {
    notNowBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    updateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
          "https://play.google.com/store/apps/details?id=com.hapramp"));
        intent.setPackage("com.android.vending");
        appCompatActivity.startActivity(intent);
        dismiss();
      }
    });
  }
}
