package com.hapramp.activity;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hapramp.R;
import com.hapramp.views.RatingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.ratingBar)
    RatingView ratingBar;
    @BindView(R.id.ratingBtn)
    Button ratingBtn;

    int rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        ButterKnife.bind(this);

        Log.d("ViewC","View "+findViewById(R.id.ratingBar).toString());

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
