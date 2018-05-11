package com.hapramp.viewmodel.trending;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hapramp.steem.models.FeedResponse;

/**
 * Created by Ankit on 5/9/2018.
 */

public class TrendingViewModel extends ViewModel {

    private MutableLiveData<FeedResponse> trendingFeeds;
    private MutableLiveData<FeedResponse> appendableFeeds;

    public MutableLiveData<FeedResponse> getTrendingFeeds(){
        if(trendingFeeds==null){
            trendingFeeds = new MutableLiveData<>();
        }
        return trendingFeeds;
    }

    public MutableLiveData<FeedResponse> getAppendableFeeds(){
        if(appendableFeeds==null){
            appendableFeeds = new MutableLiveData<>();
        }
        return appendableFeeds;
    }

}
