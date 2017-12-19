package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.views.CreateButtonView;
import com.hapramp.views.StarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements CreateButtonView.ItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }


    @Override
    public void onCreateArticleButtonClicked() {
        Toast.makeText(this,"Clicked Article",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreatePostButtonClicked() {
        Toast.makeText(this,"Clicked Post",Toast.LENGTH_LONG).show();
    }

}
