package com.hapramp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hapramp.R;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.views.renderer.RendererView;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RendererView rendererView = findViewById(R.id.renderView);
//
//        List<PostStructureModel.Data> data = new ArrayList<>();
//        //data.add(new PostStructureModel.Data("Hello ankit", ContentTypes.DataType.TEXT));
//        data.add(new PostStructureModel.Data("https://i.ytimg.com/vi/dzlmDxiWBCI/hqdefault.jpg", ContentTypes.DataType.IMAGE));
//       // data.add(new PostStructureModel.Data(getResources().getString(R.string.sample), ContentTypes.DataType.TEXT));
//        //data.add(new PostStructureModel.Data("http://content.gulte.com/content/2011/09/photos/actress/Kajal%20Agarwal/normal/Kajal%20Agarwal_165.jpg", ContentTypes.DataType.IMAGE));
//
//        PostStructureModel postStructureModel = new PostStructureModel(data,ContentTypes.CONTENT_TYPE_ARTICLE);
//        rendererView.render(postStructureModel);
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
        finish();

    }

}