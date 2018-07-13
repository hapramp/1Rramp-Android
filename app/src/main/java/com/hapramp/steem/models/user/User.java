package com.hapramp.steem.models.user;

import com.google.gson.annotations.SerializedName;

/**
	* Created by Ankit on 3/27/2018.
	*/

public class User {
		@SerializedName("username")
		private String username;
		@SerializedName("full_name")
		private String fullname;
		@SerializedName("location")
		private String location;
		@SerializedName("profile_image")
		private String profile_image;
		@SerializedName("cover_image")
		private String cover_image;
		@SerializedName("about")
		private String about;
		@SerializedName("post_count")
		private int postCount;
  @SerializedName("reputation")
  private String reputation;

		public String getUsername() {
				return username;
		}

		public void setUsername(String username) {
				this.username = username;
		}

		public String getFullname() {
				return fullname;
		}

		public void setFullname(String fullname) {
				this.fullname = fullname;
		}

		public String getLocation() {
				return location;
		}

		public void setLocation(String location) {
				this.location = location;
		}

		public String getProfile_image() {
				return profile_image;
		}

		public String getReputation() {
				return reputation;
		}

		public void setReputation(String reputation) {
				this.reputation = reputation;
		}

		public void setProfile_image(String profile_image) {
				this.profile_image = profile_image;
		}

		public String getCover_image() {
				return cover_image;
		}

		public int getPostCount() {
				return postCount;
		}

		public void setPostCount(int postCount) {
				this.postCount = postCount;
		}

		public void setCover_image(String cover_image) {
				this.cover_image = cover_image;
		}

		public String getAbout() {
				return about;
		}

		public void setAbout(String about) {
				this.about = about;
		}

		@Override
		public String toString() {
				return "User{" +
						"username='" + username + '\'' +
						", fullname='" + fullname + '\'' +
						", location='" + location + '\'' +
						", profile_image='" + profile_image + '\'' +
						", cover_image='" + cover_image + '\'' +
						", about='" + about + '\'' +
						", postCount=" + postCount +
						", reputation='" + reputation + '\'' +
						'}';
		}
}