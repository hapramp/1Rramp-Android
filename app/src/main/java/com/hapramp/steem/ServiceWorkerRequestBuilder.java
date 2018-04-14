package com.hapramp.steem;

import android.os.SystemClock;


public class ServiceWorkerRequestBuilder {

    private int requestId;
    private String communityTag;
    private String subCategory;
    private String username;
    private int limit;

    public ServiceWorkerRequestBuilder() {
        this.requestId = (int) System.currentTimeMillis();
    }

    public ServiceWorkerRequestBuilder serCommunityTag(String tag){
        this.communityTag = tag;
        return this;
    }



    public ServiceWorkerRequestBuilder setUserName(String username){
        this.username = username;
        return this;
    }

    public ServiceWorkerRequestBuilder setLimit(int limit){
        this.limit = limit;
        return this;
    }

    public ServiceWorkerRequestParams createRequestParam(){
        return new ServiceWorkerRequestParams(requestId,communityTag,"",username,limit);
    }

}
