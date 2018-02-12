package com.hapramp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hapramp.models.response.PostResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2/12/2018.
 */

public class Feed  implements Parcelable {
    @SerializedName("id")
    public int id;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("content")
    public String content;
    @SerializedName("media_uri")
    public String media_uri;
    @SerializedName("post_type")
    public int post_type;
    @SerializedName("user")
    public PostResponse.User user;
    @SerializedName("skills")
    public List<PostResponse.Skills> skills;
    @SerializedName("vote_count")
    public int vote_count;
    @SerializedName("vote_sum")
    public int vote_sum;
    @SerializedName("is_voted")
    public boolean is_voted;
    @SerializedName("hapcoins")
    public float hapcoins;
    @SerializedName("current_vote")
    public int current_vote;
    @SerializedName("comment_count")
    public int comment_count;
    @SerializedName("contest_post")
    public PostResponse.Contest_post contest_post;

    protected Feed(Parcel in) {
        id = in.readInt();
        created_at = in.readString();
        content = in.readString();
        media_uri = in.readString();
        post_type = in.readInt();
        user = (PostResponse.User) in.readValue(PostResponse.User.class.getClassLoader());
        if (in.readByte() == 0x01) {
            skills = new ArrayList<PostResponse.Skills>();
            in.readList(skills, PostResponse.Skills.class.getClassLoader());
        } else {
            skills = null;
        }
        vote_count = in.readInt();
        vote_sum = in.readInt();
        is_voted = in.readByte() != 0x00;
        hapcoins = in.readFloat();
        current_vote = in.readInt();
        comment_count = in.readInt();
        contest_post = (PostResponse.Contest_post) in.readValue(PostResponse.Contest_post.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(created_at);
        dest.writeString(content);
        dest.writeString(media_uri);
        dest.writeInt(post_type);
        dest.writeValue(user);
        if (skills == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(skills);
        }
        dest.writeInt(vote_count);
        dest.writeInt(vote_sum);
        dest.writeByte((byte) (is_voted ? 0x01 : 0x00));
        dest.writeFloat(hapcoins);
        dest.writeInt(current_vote);
        dest.writeInt(comment_count);
        dest.writeValue(contest_post);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}