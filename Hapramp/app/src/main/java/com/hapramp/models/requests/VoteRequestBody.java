package com.hapramp.models.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/18/2017.
 */

public class VoteRequestBody {

    @SerializedName("vote")
    public int vote;

    public VoteRequestBody(int vote) {
        this.vote = vote;
    }

}
