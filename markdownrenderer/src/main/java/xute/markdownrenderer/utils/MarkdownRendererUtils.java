package xute.markdownrenderer.utils;

import android.util.Log;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MarkdownRendererUtils {

  private static Pattern pattern;
  private static Matcher matcher;

  public static String getHtmlContent(String md) {
//    Log.d("Markdown", "Original|:" + md);
    Parser parser = Parser.builder().build();
    Node document = parser.parse(md);
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    String text = renderer.render(document);
//    Log.d("Markdown", "After Lib Parse |:" + text);
 //   text = doSomePreProcessing(text);
//    Log.d("Markdown", "After processing|:" + text);
    //http://player.vimeo.com/video/63534746
    //https://www.youtube.com/embed/YOUR_VIDEO_CODE
    return text;
  }

  public static String doSomePreProcessing(String body) {
    //body = replacePlainLinks(body);
    // body = replacePlainImageLinks(body);
    body = replaceMarkdownImage(body);
    // body = replaceMarkdownLinks(body);
    body = replaceYoutubeVideo(body);
    return body;
  }

  private static String replaceMarkdownImage(String body) {
    return body.replaceAll("!\\[(.*?)\\]\\((.*?)[)]", "<img alt=\"$1\" src=\"$2\"/>");
  }

  private static String replaceYoutubeVideo(String body) {
    String emb = "<iframe src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" width=\"560\" height=\"340\"" +
      " horizontalscrolling=\"no\" verticalscrolling=\"yes\"/>";
    //https:\/\/www\.youtube\.com\/watch\?v=(.*?)[\n| |<|.]
    return body.replaceAll("https:\\/\\/www\\.youtube\\.com\\/watch\\?v=([a-z0-9A-Z_]+)", emb);
  }

  private static String replacePlainLinks(String body) {
    pattern = Pattern.compile("[\\n|>| ](http(s|):\\/\\/.*?)[\\n|<| ]");
    matcher = pattern.matcher(body);
    if (matcher.find()) {
      body = new StringBuilder(body).replace(matcher.start(1), matcher.end(1),
        "<a href=\"" + matcher.group(1) + "\">" + matcher.group(1) + "</a>").toString();
    }
    return body;
  }

  private static String replacePlainImageLinks(String body) {
    pattern = Pattern.compile("[^\"](http(s|):.*?)(.png|.jpeg|.PNG|.gif|.jpg)");
    matcher = pattern.matcher(body);
    if (matcher.find() && matcher.group(2).length() > 0) {
      body = new StringBuilder(body).replace(matcher.start(1), matcher.end(1),
        "<img src=\"" + matcher.group(1) + matcher.group(2) + "\"/>").toString();
    }
    return body;
  }

  private static String replaceMarkdownLinks(String body) {
    return body.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "<a href=\"$2\">$1</a>");
  }

}
