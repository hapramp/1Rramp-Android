package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.R;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.views.renderer.RendererView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewActivity extends AppCompatActivity {

    @BindView(R.id.renderView)
    RendererView renderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview_activity);
        ButterKnife.bind(this);
        ArrayList<FeedDataItemModel> feedDataItemModels = getIntent().getParcelableArrayListExtra("data");
        renderView.render(new PostStructureModel(feedDataItemModels, FeedDataConstants.FEED_TYPE_ARTICLE));

    }

}
