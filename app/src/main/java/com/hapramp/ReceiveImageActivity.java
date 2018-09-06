package com.hapramp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.CreatePostActivity;
import com.hapramp.ui.activity.LoginActivity;

public class ReceiveImageActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_receive_image);
    if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
      Intent intent = getIntent();
      Intent cpi = new Intent(this, CreatePostActivity.class);
      cpi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      cpi.setAction(intent.getAction());
      cpi.setType(intent.getType());
      cpi.putExtras(intent.getExtras());
      startActivity(cpi);
    } else {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(loginIntent);
      finish();
    }
  }
}
