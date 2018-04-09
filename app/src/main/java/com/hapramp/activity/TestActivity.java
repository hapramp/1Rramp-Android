package com.hapramp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.hapramp.R;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.renderer.RendererView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RendererView rendererView = findViewById(R.id.renderView);
        List<PostStructureModel.Data> data = new ArrayList<>();
        data.add(new PostStructureModel.Data("Kajal Agrawal", ContentTypes.DataType.H1));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_subtitle), ContentTypes.DataType.H2));
        data.add(new PostStructureModel.Data("http://hdwallpapersfreepics.com/wp-content/uploads/2016/12/kajal_agarwal_jilla-3840x21601.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description1), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("http://3.bp.blogspot.com/-T55aYJvIgj8/TolnLM_anpI/AAAAAAAADQY/SAGh57yOkyI/s1600/kajal-agarwal-hot-veera-stills-011.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description2), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("http://1.bp.blogspot.com/-swjw4rlQiZI/Tolng7glMtI/AAAAAAAADRM/snnX8Yn1xEk/s1600/kajal-agarwal-hot-veera-stills-001.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description3), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("http://hdwallpapersfreepics.com/wp-content/uploads/2016/12/Kajal-Agarwal-Upcoming-Movie-List-2015-2016-Telugu-Tamil-Bollywood1.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description4), ContentTypes.DataType.TEXT));

        PostStructureModel postStructureModel = new PostStructureModel(data, ContentTypes.CONTENT_TYPE_ARTICLE);
        rendererView.render(postStructureModel);


    }
}
