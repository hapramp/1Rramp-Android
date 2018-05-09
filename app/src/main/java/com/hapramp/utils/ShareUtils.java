package com.hapramp.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hapramp.datamodels.FeedRenderTypeModel;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.data.FeedDataItemModel;

import java.util.List;

/**
 * Created by Ankit on 5/9/2018.
 */

public class ShareUtils {
    public static void share(Context context,String content){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void shareMixedContent(Context context, Feed feed){

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        FeedRenderTypeModel feedRenderTypeModel = scanFeedContentsForRendering(feed.jsonMetadata.content.getData());

        if(feedRenderTypeModel.hasContent) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, getFormattedTextForSharing(feedRenderTypeModel.text,feed.url));
            shareIntent.setType("text/plain");
        }
        context.startActivity(shareIntent);
    }

    private static String getFormattedTextForSharing(StringBuilder text, String url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(text.toString()).append("\nhttps://alpha.hapramp.com"+url).append("\n\nHapramp, The social media platform for creative artists");
        return stringBuilder.toString();
    }

    private static FeedRenderTypeModel scanFeedContentsForRendering(List<FeedDataItemModel> data) {

        FeedRenderTypeModel feedRenderTypeModel = new FeedRenderTypeModel();

        //iterate through all the content
        for (int i = 0; i < data.size(); i++) {

            //for image
            if (data.get(i).type.equals(FeedDataConstants.ContentType.IMAGE)) {

                //neither video or image is detected prior
                if (!feedRenderTypeModel.isFirstMediaImage && !feedRenderTypeModel.isFirstMediaVideo) {
                    //set media
                    feedRenderTypeModel.setFirstImageUrl(data.get(i).content);
                    feedRenderTypeModel.setFirstMediaImage(true);
                }

            }

            //accumulate text
            if (data.get(i).type.equals(FeedDataConstants.ContentType.TEXT) || data.get(i).type.equals(FeedDataConstants.ContentType.H2) || data.get(i).type.equals(FeedDataConstants.ContentType.H3)) {

                feedRenderTypeModel
                        .appendText(data.get(i).getContent());

            }
        }

        Log.d("PostItemView", "scanned Model : " + feedRenderTypeModel.toString());
        return feedRenderTypeModel;

    }


}
