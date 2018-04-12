package com.hapramp.utils;

import com.hapramp.editor.models.EditorTextStyle;
import com.hapramp.editor.models.Node;
import com.hapramp.steem.FeedData;
import com.hapramp.steem.models.data.FeedDataItemModel;
import java.util.ArrayList;
import java.util.List;
import static com.hapramp.editor.models.EditorTextStyle.H1;
import static com.hapramp.editor.models.EditorTextStyle.H2;
import static com.hapramp.editor.models.EditorTextStyle.H3;
import static com.hapramp.editor.models.EditorTextStyle.NORMAL;

/**
 * Created by Ankit on 4/12/2018.
 */

public class EditorDataFormatter {

    public static FeedDataItemModel getFormatedItem(Node node) {

        FeedDataItemModel feedDataItemModel = new FeedDataItemModel("", FeedData.ContentType.TEXT);
        String content = "";

        switch (node.type) {
            //case for all kind of formatted strings
            case INPUT: // Text, H1 , H2 , H3
                // there is single item in this array
                content = node.content.get(0);
                // Check for text, H1 ,H2 , H3
                String type = getContentType(node.contentStyles);
                feedDataItemModel = new FeedDataItemModel(content, type);
                break;

            //image
            case img:
                // image path
                content = node.content.get(0);
                //we can extract image description
                feedDataItemModel = new FeedDataItemModel(content, FeedData.ContentType.IMAGE);
                break;

            case hr:
                feedDataItemModel = new FeedDataItemModel("", FeedData.ContentType.HR);
                break;
            case ol:
            case ul:
                content = getBulletListContent(node.content);
                feedDataItemModel = new FeedDataItemModel(content, FeedData.ContentType.BULLET);
                break;

                // TODO: 4/12/2018 Need Blockquote, Youtube type
        }

        return feedDataItemModel;
    }

    private static String getBulletListContent(ArrayList<String> content) {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < content.size(); i++) {

            stringBuilder
                    .append(content.get(i))
                    .append("\n");

        }
        return stringBuilder.toString();

    }

    private static String getContentType(List<EditorTextStyle> contentStyles) {

        String type;
        if (contentStyles.contains(H1)) {
            type = FeedData.ContentType.H1;
        } else if (contentStyles.contains(H2)) {
            type = FeedData.ContentType.H2;
        } else if (contentStyles.contains(H3)) {
            type = FeedData.ContentType.H3;
        } else if (contentStyles.contains(NORMAL)) {
            type = FeedData.ContentType.TEXT;
        } else {
            type = "text";
        }
        return type;

    }

}
