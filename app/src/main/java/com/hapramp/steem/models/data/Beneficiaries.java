package com.hapramp.steem.models.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/9/2018.
 */

public class Beneficiaries implements Parcelable {
    @Expose
    @SerializedName("weight")
    public int weight;
    @Expose
    @SerializedName("account")
    public String account;

    protected Beneficiaries(Parcel in) {
        weight = in.readInt();
        account = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weight);
        dest.writeString(account);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Beneficiaries> CREATOR = new Parcelable.Creator<Beneficiaries>() {
        @Override
        public Beneficiaries createFromParcel(Parcel in) {
            return new Beneficiaries(in);
        }

        @Override
        public Beneficiaries[] newArray(int size) {
            return new Beneficiaries[size];
        }
    };

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

