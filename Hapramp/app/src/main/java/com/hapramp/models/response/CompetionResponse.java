package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CompetionResponse {

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

    public static class Skill {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

        @Override
        public String toString() {
            return "Skill{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", image_uri='" + image_uri + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class Organization {
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

        @Override
        public String toString() {
            return "Organization{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", image_uri='" + image_uri + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class Club {
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

        @Override
        public String toString() {
            return "Club{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", handle='" + handle + '\'' +
                    ", description='" + description + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", logo_uri='" + logo_uri + '\'' +
                    ", skill=" + skill +
                    ", organization=" + organization +
                    '}';
        }
    }

    public CompetionResponse(int id, String name, String handle, String description, String logo_uri, String created_at, String start_time, String end_time, int entry_fee, int give_out_ratio, Club club) {
        this.id = id;
        this.name = name;
        this.handle = handle;
        this.description = description;
        this.logo_uri = logo_uri;
        this.created_at = created_at;
        this.start_time = start_time;
        this.end_time = end_time;
        this.entry_fee = entry_fee;
        this.give_out_ratio = give_out_ratio;
        this.club = club;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo_uri() {
        return logo_uri;
    }

    public void setLogo_uri(String logo_uri) {
        this.logo_uri = logo_uri;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getEntry_fee() {
        return entry_fee;
    }

    public void setEntry_fee(int entry_fee) {
        this.entry_fee = entry_fee;
    }

    public int getGive_out_ratio() {
        return give_out_ratio;
    }

    public void setGive_out_ratio(int give_out_ratio) {
        this.give_out_ratio = give_out_ratio;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public String toString() {
        return "CompetionResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", handle='" + handle + '\'' +
                ", description='" + description + '\'' +
                ", logo_uri='" + logo_uri + '\'' +
                ", created_at='" + created_at + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", entry_fee=" + entry_fee +
                ", give_out_ratio=" + give_out_ratio +
                ", club=" + club +
                '}';
    }
}
