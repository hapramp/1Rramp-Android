package com.hapramp.datamodels.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/16/2017.
 */

public class UserDpUpdateRequestBody {

    @SerializedName("image_uri")
    public String image_uri;

    public UserDpUpdateRequestBody(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    @Override
    public String toString() {
        return "UserDpUpdateRequestBody{" +
                "image_uri='" + image_uri + '\'' +
                '}';
    }

}
