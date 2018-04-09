package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.hapramp.R;
import com.hapramp.steem.FeedData;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.views.renderer.RendererView;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RendererView rendererView = findViewById(R.id.renderView);
        List<FeedDataItemModel> data = new ArrayList<>();
        data.add(new FeedDataItemModel("Kajal Agrawal", FeedData.ContentType.H1));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_subtitle), FeedData.ContentType.H2));
        data.add(new FeedDataItemModel("http://www.ssmusic.tv/wp-content/uploads/2016/09/Kajal-Aggarwal-Stills-From-Janatha-Garage-Pakka-Local-Song-Shoot-03-copy.jpg", FeedData.ContentType.IMAGE));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_description1), FeedData.ContentType.TEXT));
        data.add(new FeedDataItemModel("H82_9BLjowY", FeedData.ContentType.YOUTUBE));
        data.add(new FeedDataItemModel("", FeedData.ContentType.HR));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_block_info), FeedData.ContentType.BLOCKQUOTE));
        data.add(new FeedDataItemModel("http://3.bp.blogspot.com/-T55aYJvIgj8/TolnLM_anpI/AAAAAAAADQY/SAGh57yOkyI/s1600/kajal-agarwal-hot-veera-stills-011.jpg", FeedData.ContentType.IMAGE));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_description2), FeedData.ContentType.TEXT));
        data.add(new FeedDataItemModel("http://1.bp.blogspot.com/-swjw4rlQiZI/Tolng7glMtI/AAAAAAAADRM/snnX8Yn1xEk/s1600/kajal-agarwal-hot-veera-stills-001.jpg", FeedData.ContentType.IMAGE));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_description3), FeedData.ContentType.TEXT));
        data.add(new FeedDataItemModel("https://www.25cineframes.com/images/gallery/2015/04/kajal-agarwal-jilla-movie-latest-photos-stills-vijay-brahmanandam/04-Kajal_Agarwal_Jilla_Movie_Latest_Photos__Stills_Vijay_Brahmanandam.jpg", FeedData.ContentType.IMAGE));
        data.add(new FeedDataItemModel(getResources().getString(R.string.kajal_description4), FeedData.ContentType.TEXT));

        PostStructureModel postStructureModel = new PostStructureModel(data, FeedData.FEED_TYPE_ARTICLE);
        rendererView.render(postStructureModel);


    }
}
