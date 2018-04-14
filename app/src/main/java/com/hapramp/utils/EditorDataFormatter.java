package com.hapramp.utils;

import com.hapramp.editor.models.EditorTextStyle;
import com.hapramp.editor.models.Node;
import com.hapramp.steem.FeedData;
import com.hapramp.steem.models.data.FeedDataItemModel;

import java.util.ArrayList;
import java.util.List;

import static com.hapramp.editor.models.EditorTextStyle.BOLD;
import static com.hapramp.editor.models.EditorTextStyle.BOLDITALIC;
import static com.hapramp.editor.models.EditorTextStyle.H1;
import static com.hapramp.editor.models.EditorTextStyle.H2;
import static com.hapramp.editor.models.EditorTextStyle.H3;
import static com.hapramp.editor.models.EditorTextStyle.ITALIC;
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
                // Check for text, H1 ,H2 , H3
                //we have content styles in node: we can make use of that
                String type = getContentType(node.contentStyles);
                content = getContent(node.content.get(0), node.contentStyles);
                feedDataItemModel = new FeedDataItemModel(content, type);
                break;

            //image
            case img:
                // image path at index:0
                // image desc at index: 1
                //put desc to caption of data item
                content = node.content.get(0);
                //we can extract image description
                feedDataItemModel = new FeedDataItemModel(content, FeedData.ContentType.IMAGE);

                if(node.content.size()>1) {
                    feedDataItemModel.setCaption(node.content.get(1));
                }

                break;

            case hr:
                feedDataItemModel = new FeedDataItemModel("", FeedData.ContentType.HR);
                break;

            case ol:
                content = getBulletListContent(node.content);
                feedDataItemModel = new FeedDataItemModel(content, FeedData.ContentType.OL);
                break;

            case ul:
                content = getBulletListContent(node.content);
                feedDataItemModel = new FeedDataItemModel(content, FeedData.ContentType.UL);
                break;

            // TODO: 4/12/2018 Need Blockquote, Youtube type
        }

        return feedDataItemModel;
    }

    private static String getContent(String content, List<EditorTextStyle> contentStyles) {

        String __content;
        if (contentStyles.contains(BOLDITALIC)) {
            __content = "<b><i>" + content + "</i></b>";
        } else if (contentStyles.contains(BOLD)) {
            __content = "<b>" + content + "</b>";
        } else if (contentStyles.contains(ITALIC)) {
            __content = "<i>" + content + "</i>";
        } else {
            __content = content;
        }

        return __content;
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
            type = FeedData.ContentType.H1; //rendered as custom view + html
        } else if (contentStyles.contains(H2)) {
            type = FeedData.ContentType.H2; //rendered as custom view + html
        } else if (contentStyles.contains(H3)) {
            type = FeedData.ContentType.H3; //rendered as custom view + html
        } else if (contentStyles.contains(NORMAL)) {
            type = FeedData.ContentType.TEXT; //rendered as html
        } else if (contentStyles.contains(BOLDITALIC)) {
            type = "text"; //rendered as html
        } else if (contentStyles.contains(BOLD)) {
            type = "text"; //rendered as html
        } else {
            type = "text"; //rendered as html
        }
        return type;

    }

}
