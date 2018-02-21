package com.hapramp.steem;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Ankit on 2/21/2018.
 */

public class JsonMetaDataModel {

    List<String> tags;
    String app;

    public JsonMetaDataModel(List<String> tags) {
        this.tags = tags;
        this.app = LocalConfig.APP_TAG;
    }

    public String getJson(){
        return new Gson().toJson(this,JsonMetaDataModel.class);
    }

}
