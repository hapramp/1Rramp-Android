package com.hapramp.utils;

import android.text.Html;
import android.text.SpannableString;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ankit on 1/1/2018.
 */

public class EditorUtils {

    public static String getHtml(EditText et){

        String htmlString= Html.toHtml(new SpannableString(et.getText()));
        Document doc = Jsoup.parseBodyFragment(htmlString);

        for (Element el : doc.getAllElements()) {
            if (hasSameTypeAncestor(el)) {
                el.unwrap();
            }
        }

        return doc.body().toString();

    }

    private static Set<String> tagsIDontWantToNest = new HashSet<>(Arrays.asList("b","i","u","span"));

    private static boolean hasSameTypeAncestor(Element element) {

        Tag tag = element.tag();
        if (tagsIDontWantToNest.contains(tag.getName())) {
            for (Element el : element.parents()) {
                if (el.tag().equals(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

}
