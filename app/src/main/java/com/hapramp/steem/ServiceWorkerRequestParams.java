package com.hapramp.steem;

public class ServiceWorkerRequestParams {

    private int requestId;

    public ServiceWorkerRequestParams(int requestId) {
        this.requestId = requestId;
    }

    public boolean equals(ServiceWorkerRequestParams serviceWorkerRequestParams) {
        return requestId == serviceWorkerRequestParams.requestId;
    }

}
