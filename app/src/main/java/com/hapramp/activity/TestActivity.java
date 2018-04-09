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
        data.add(new PostStructureModel.Data("http://www.ssmusic.tv/wp-content/uploads/2016/09/Kajal-Aggarwal-Stills-From-Janatha-Garage-Pakka-Local-Song-Shoot-03-copy.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description1), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("H82_9BLjowY", ContentTypes.DataType.YOUTUBE));
        data.add(new PostStructureModel.Data("", ContentTypes.DataType.HR));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_block_info), ContentTypes.DataType.BLOCKQUOTE));
        data.add(new PostStructureModel.Data("http://3.bp.blogspot.com/-T55aYJvIgj8/TolnLM_anpI/AAAAAAAADQY/SAGh57yOkyI/s1600/kajal-agarwal-hot-veera-stills-011.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description2), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("http://1.bp.blogspot.com/-swjw4rlQiZI/Tolng7glMtI/AAAAAAAADRM/snnX8Yn1xEk/s1600/kajal-agarwal-hot-veera-stills-001.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description3), ContentTypes.DataType.TEXT));
        data.add(new PostStructureModel.Data("https://www.25cineframes.com/images/gallery/2015/04/kajal-agarwal-jilla-movie-latest-photos-stills-vijay-brahmanandam/04-Kajal_Agarwal_Jilla_Movie_Latest_Photos__Stills_Vijay_Brahmanandam.jpg", ContentTypes.DataType.IMAGE));
        data.add(new PostStructureModel.Data(getResources().getString(R.string.kajal_description4), ContentTypes.DataType.TEXT));

        PostStructureModel postStructureModel = new PostStructureModel(data, ContentTypes.CONTENT_TYPE_ARTICLE);
        rendererView.render(postStructureModel);


    }
}
