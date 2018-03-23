package com.hapramp;

import com.google.gson.Gson;
import com.hapramp.db.DatabaseHelper;
import com.hapramp.steem.models.Feed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = LOLLIPOP, packageName = "com.hapramp")
public class DatabaseUnitTest {

    public String feedJson = "{\n" +
            "    \"id\": 37455934,\n" +
            "    \"author\": \"bxute\",\n" +
            "    \"permlink\": \"n4a9crkd8l9vqyjwx200c8xoz23kbpo64ddwaws63a6beene6u5f0tmspmdmvrs420180308170823\",\n" +
            "    \"category\": \"hapramp-test\",\n" +
            "    \"parent_author\": \"\",\n" +
            "    \"parent_permlink\": \"hapramp-test\",\n" +
            "    \"title\": \"\",\n" +
            "    \"body\": \"#### View it on Hapramp\\nClick on the logo below.\\n[![](https://i.imgur.com/G38RGuq.png)](https://alpha.hapramp.com/@bxute/n4a9crkd8l9vqyjwx200c8xoz23kbpo64ddwaws63a6beene6u5f0tmspmdmvrs420180308170823)\",\n" +
            "    \"json_metadata\": {\n" +
            "      \"app\": \"hapramp/0.0.1\",\n" +
            "      \"content\": {\n" +
            "        \"data\": [\n" +
            "          {\n" +
            "            \"content\": \"https://firebasestorage.googleapis.com/v0/b/hapramp-625c8.appspot.com/o/post_images%2F40_20180308170754_7bk_3%3A%7D2%3Cg%5B5gna?alt=media&token=23146456-11c6-482e-9353-e81e3d5866a7\",\n" +
            "            \"type\": \"image\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"content\": \"holi festival\",\n" +
            "            \"type\": \"text\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"type\": \"post\"\n" +
            "      },\n" +
            "      \"tags\": [\n" +
            "        \"hapramp-art\"\n," +
            "        \"hapramp-dance\"\n," +
            "        \"hapramp-test\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"last_update\": \"2018-03-08T11:38:48\",\n" +
            "    \"created\": \"2018-03-08T11:38:48\",\n" +
            "    \"active\": \"2018-03-08T11:38:48\",\n" +
            "    \"last_payout\": \"1970-01-01T00:00:00\",\n" +
            "    \"depth\": 0,\n" +
            "    \"children\": 0,\n" +
            "    \"net_rshares\": 0,\n" +
            "    \"abs_rshares\": 0,\n" +
            "    \"vote_rshares\": 0,\n" +
            "    \"children_abs_rshares\": 0,\n" +
            "    \"cashout_time\": \"2018-03-15T11:38:48\",\n" +
            "    \"max_cashout_time\": \"1969-12-31T23:59:59\",\n" +
            "    \"total_vote_weight\": 0,\n" +
            "    \"reward_weight\": 10000,\n" +
            "    \"total_payout_value\": \"0.000 SBD\",\n" +
            "    \"curator_payout_value\": \"0.000 SBD\",\n" +
            "    \"author_rewards\": 0,\n" +
            "    \"net_votes\": 0,\n" +
            "    \"root_comment\": 37455934,\n" +
            "    \"max_accepted_payout\": \"1000000.000 SBD\",\n" +
            "    \"percent_steem_dollars\": 10000,\n" +
            "    \"allow_replies\": true,\n" +
            "    \"allow_votes\": true,\n" +
            "    \"allow_curation_rewards\": true,\n" +
            "    \"beneficiaries\": [\n" +
            "      {\n" +
            "        \"account\": \"hapramp\",\n" +
            "        \"weight\": 2000\n" +
            "      }\n" +
            "    ],\n" +
            "    \"url\": \"/hapramp-test/@bxute/n4a9crkd8l9vqyjwx200c8xoz23kbpo64ddwaws63a6beene6u5f0tmspmdmvrs420180308170823\",\n" +
            "    \"root_title\": \"\",\n" +
            "    \"pending_payout_value\": \"0.000 SBD\",\n" +
            "    \"total_pending_payout_value\": \"0.000 STEEM\",\n" +
            "    \"active_votes\": [],\n" +
            "    \"replies\": [],\n" +
            "    \"author_reputation\": 1848870,\n" +
            "    \"promoted\": \"0.000 SBD\",\n" +
            "    \"body_length\": 200,\n" +
            "    \"reblogged_by\": [],\n" +
            "    \"hapramp_rating\": 0,\n" +
            "    \"hapramp_votes\": 0\n" +
            "  }";

    private DatabaseHelper databaseHelper;

    @Before
    public void setup() {
        p("setting up db");
        databaseHelper = new DatabaseHelper(RuntimeEnvironment.application);
        databaseHelper.getWritableDatabase();
    }

    @Test
    public void testFeedInsertion() {
        // Make mock feeds
        ArrayList<Feed> steemFeedModels = new ArrayList<>();
        steemFeedModels.add(new Gson().fromJson(feedJson, Feed.class));

        long id = databaseHelper.insertFeeds(steemFeedModels);
        // Then
        p("inserted " + id);
        // Note: here the count of id depends upon the number of valid hapramp related tags in the feeds
        assert (id == 2);

    }

    @Test
    public void testFeedQuery() {

        testFeedInsertion();

        // Make mock feeds
        ArrayList<Feed> steemFeedModels = new ArrayList<>();
        steemFeedModels.add(new Gson().fromJson(feedJson, Feed.class));

        ArrayList<Feed> models = databaseHelper.getFeedsByCommunity("hapramp-art");


        p("Read: " + new Gson().toJson(models.get(0)));

        assert (models.size() > 0);

    }

    @Test
    public void testWasCachedMethod(){

        testFeedInsertion();

        boolean cached = databaseHelper.wasFeedCached("hapramp-art");
        p("Cacheed "+cached);
        assert (cached);

    }

    @Test
    public void testUpdateFeedMethod(){

        testFeedInsertion();

        // Make mock feeds
        ArrayList<Feed> steemFeedModels = new ArrayList<>();
        steemFeedModels.add(new Gson().fromJson(feedJson, Feed.class));

        long id = databaseHelper.updateFeed(steemFeedModels, "hapramp-art");
        // Then
        p("updated " + id);
        assert (id > -1);


    }

    @After
    public void clearDb() {
        p("clearing db");
        databaseHelper.deleteFeedsCache();
    }

    public void p(String msg) {
        System.out.println(msg);
    }
}
