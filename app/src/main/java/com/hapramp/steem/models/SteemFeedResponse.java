package com.hapramp.steem.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 3/8/2018.
 */

public class SteemFeedResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("result")
    @Expose
    private List<SteemFeedModel> result = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SteemFeedModel> getResult() {
        return result;
    }

    public void setResult(List<SteemFeedModel> result) {
        this.result = result;
    }

}
