package com.hapramp.steem.models.user;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 3/27/2018.
 */


public class Profile {

    private static final String TAG = Profile.class.getSimpleName();

    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("website")
    @Expose
    public String website;
    @SerializedName("about")
    @Expose
    public String about;

    public Profile(String profileImage, String name, String location, String website, String about) {
        this.profileImage = profileImage;
        this.name = name;
        this.location = location;
        this.website = website;
        this.about = about;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getAbout() {
        return about;
    }

    public static void fetchUserProfilesFor(final List<Feed> feeds) {

        Log.d(TAG, "Fetching user profiles");
        new Thread() {
            @Override
            public void run() {
                final List<String> users = new ArrayList<>();
                for (int i = 0; i < feeds.size(); i++) {
                    if (!users.contains(feeds.get(i).getAuthor())) {
                        users.add(feeds.get(i).getAuthor());
                    }
                }
                Log.d(TAG, "Request for : " + users.toString());
                fetchUserProfileImages(users);
            }
        }.start();

    }

    private static void fetchUserProfileImages(final List<String> users) {

        Log.d(TAG, "Fetching User Profile Images");

        new Thread() {
            @Override
            public void run() {
                try {
                    SteemJ steemJ = new SteemJ();
                    List<String> userData = steemJ.getUserProfileImages(users);
                    Log.d(TAG, userData.toString());
                    // save to pref
                    for (int i = 0; i < userData.size(); i++) {
                        Log.d(TAG, "key:"+users.get(i)+" value:"+userData.get(i));;
                        HaprampPreferenceManager.getInstance().saveUserProfile(users.get(i), userData.get(i));
                    }

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public static Profile getDefaultProfile() {

        Profile dummy = new Profile("https://user-images.githubusercontent.com/10809719/38206885-b36c8a66-36c9-11e8-9c7a-3bba603b4994.png",
                "name",
                "location",
                "website",
                "about");
        return dummy;

    }

    public static String getDefaultProfileAsJson() {

        Profile dummy = new Profile("https://user-images.githubusercontent.com/10809719/38206885-b36c8a66-36c9-11e8-9c7a-3bba603b4994.png",
                "name",
                "location",
                "website",
                "about");
        return new Gson().toJson(dummy);

    }

}