package com.hapramp.steem;

public class ServiceWorkerRequestParams {

    private int requestId;
    private String communityId;
    private String username;
    private int limit;

    public ServiceWorkerRequestParams(int requestId, String communityId, String username, int limit) {
        this.requestId = requestId;
        this.communityId = communityId;
        this.username = username;
        this.limit = limit;
    }

    public boolean equals(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return requestId == serviceWorkerRequestParams.requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public String getUsername() {
        return username;
    }

    public int getLimit() {
        return limit;
    }
}
