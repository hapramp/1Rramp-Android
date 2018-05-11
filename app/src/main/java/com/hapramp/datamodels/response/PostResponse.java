package com.hapramp.datamodels.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hapramp.steem.models.Feed;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostResponse {


    @SerializedName("start")
    public int start;
    @SerializedName("limit")
    public int limit;
    @SerializedName("count")
    public int count;
    @SerializedName("next")
    public String next;
    @SerializedName("previous")
    public String previous;
    @SerializedName("results")
    public List<Feed> results;

    public static class User implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("username")
        public String username;
        @SerializedName("full_name")
        public String full_name;
        @SerializedName("karma")
        public int karma;
        @SerializedName("image_uri")
        public String image_uri;

        protected User(Parcel in) {
            id = in.readInt();
            username = in.readString();
            full_name = in.readString();
            karma = in.readInt();
            image_uri = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(username);
            dest.writeString(full_name);
            dest.writeInt(karma);
            dest.writeString(image_uri);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }

    public static class Skills implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

        protected Skills(Parcel in) {
            id = in.readInt();
            name = in.readString();
            image_uri = in.readString();
            description = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(image_uri);
            dest.writeString(description);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Skills> CREATOR = new Parcelable.Creator<Skills>() {
            @Override
            public Skills createFromParcel(Parcel in) {
                return new Skills(in);
            }

            @Override
            public Skills[] newArray(int size) {
                return new Skills[size];
            }
        };
    }

    public static class Skill implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

        protected Skill(Parcel in) {
            id = in.readInt();
            name = in.readString();
            image_uri = in.readString();
            description = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(image_uri);
            dest.writeString(description);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Skill> CREATOR = new Parcelable.Creator<Skill>() {
            @Override
            public Skill createFromParcel(Parcel in) {
                return new Skill(in);
            }

            @Override
            public Skill[] newArray(int size) {
                return new Skill[size];
            }
        };
    }

    public static class Organization implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("username")
        public String username;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

        protected Organization(Parcel in) {
            id = in.readInt();
            name = in.readString();
            username = in.readString();
            image_uri = in.readString();
            description = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(username);
            dest.writeString(image_uri);
            dest.writeString(description);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Organization> CREATOR = new Parcelable.Creator<Organization>() {
            @Override
            public Organization createFromParcel(Parcel in) {
                return new Organization(in);
            }

            @Override
            public Organization[] newArray(int size) {
                return new Organization[size];
            }
        };
    }

    public static class Club implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("skill")
        public Skill skill;
        @SerializedName("organization")
        public Organization organization;

        protected Club(Parcel in) {
            id = in.readInt();
            name = in.readString();
            handle = in.readString();
            description = in.readString();
            created_at = in.readString();
            logo_uri = in.readString();
            skill = (Skill) in.readValue(Skill.class.getClassLoader());
            organization = (Organization) in.readValue(Organization.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(handle);
            dest.writeString(description);
            dest.writeString(created_at);
            dest.writeString(logo_uri);
            dest.writeValue(skill);
            dest.writeValue(organization);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Club> CREATOR = new Parcelable.Creator<Club>() {
            @Override
            public Club createFromParcel(Parcel in) {
                return new Club(in);
            }

            @Override
            public Club[] newArray(int size) {
                return new Club[size];
            }
        };
    }

    public static class Contest_post implements Parcelable {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("start_time")
        public String start_time;
        @SerializedName("end_time")
        public String end_time;
        @SerializedName("entry_fee")
        public int entry_fee;
        @SerializedName("give_out_ratio")
        public int give_out_ratio;
        @SerializedName("club")
        public Club club;
        @SerializedName("participant_count")
        public int participant_count;

        protected Contest_post(Parcel in) {
            id = in.readInt();
            name = in.readString();
            handle = in.readString();
            description = in.readString();
            logo_uri = in.readString();
            created_at = in.readString();
            start_time = in.readString();
            end_time = in.readString();
            entry_fee = in.readInt();
            give_out_ratio = in.readInt();
            club = (Club) in.readValue(Club.class.getClassLoader());
            participant_count = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(handle);
            dest.writeString(description);
            dest.writeString(logo_uri);
            dest.writeString(created_at);
            dest.writeString(start_time);
            dest.writeString(end_time);
            dest.writeInt(entry_fee);
            dest.writeInt(give_out_ratio);
            dest.writeValue(club);
            dest.writeInt(participant_count);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Contest_post> CREATOR = new Parcelable.Creator<Contest_post>() {
            @Override
            public Contest_post createFromParcel(Parcel in) {
                return new Contest_post(in);
            }

            @Override
            public Contest_post[] newArray(int size) {
                return new Contest_post[size];
            }
        };
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "start=" + start +
                ", limit=" + limit +
                ", count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", results=" + results +
                '}';
    }

}
