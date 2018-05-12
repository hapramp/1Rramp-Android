package com.hapramp.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/12/2018.
 */

public class DeviceRegistrationReponse {
    @SerializedName("id")
    public int id;
    @SerializedName("username")
    public String username;

    public DeviceRegistrationReponse(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
