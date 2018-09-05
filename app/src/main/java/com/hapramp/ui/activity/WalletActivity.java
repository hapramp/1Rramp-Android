package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.fragments.EarningFragment;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletActivity extends AppCompatActivity {
  public static final String EXTRA_USERNAME = "username";
  @BindView(R.id.backBtn)
  TextView closeBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wallet);
    ButterKnife.bind(this);
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    String username = getIntent().getExtras().getString(EXTRA_USERNAME,
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    Bundle args = new Bundle();
    args.putString(EarningFragment.ARG_USERNAME, username);
    EarningFragment earningFragment = new EarningFragment();
    earningFragment.setArguments(args);
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, earningFragment)
      .commit();
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
