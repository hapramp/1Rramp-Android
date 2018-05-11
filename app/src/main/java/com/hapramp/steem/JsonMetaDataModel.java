package com.hapramp.steem;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 2/21/2018.
 */

public class JsonMetaDataModel {

    @SerializedName("tags")
    List<String> tags;
    @SerializedName("app")
    String app;
    @SerializedName("content")
    PostStructureModel postStructure;

    public JsonMetaDataModel(List<String> tags , PostStructureModel postStructure) {

        this.tags = tags;
        this.app = LocalConfig.APP_TAG;
        this.postStructure = postStructure;

    }

    public String getJson(){
        return new Gson().toJson(this,JsonMetaDataModel.class);
    }

}
