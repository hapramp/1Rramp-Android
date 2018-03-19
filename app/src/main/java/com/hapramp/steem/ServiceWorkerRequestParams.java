package com.hapramp.steem;

public class ServiceWorkerRequestParams {

    private int requestId;
    private String communityTag;
    private String subCategory;
    private String username;
    private int limit;

    public ServiceWorkerRequestParams(int requestId, String communityTag,String subCategory, String username, int limit) {
        this.requestId = requestId;
        this.communityTag = communityTag;
        this.subCategory = subCategory;
        this.username = username;
        this.limit = limit;
    }

    public boolean equals(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return requestId == serviceWorkerRequestParams.requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getCommunityTag() {
        return communityTag;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getUsername() {
        return username;
    }

    public int getLimit() {
        return limit;
    }
}
