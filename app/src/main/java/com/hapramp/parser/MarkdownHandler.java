package com.hapramp.parser;

import com.hapramp.utils.RegexUtils;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

public class MarkdownHandler {
  public static String getHtmlFromMarkdown(String md){
    MutableDataSet options = new MutableDataSet();

    // uncomment to set optional extensions
    //options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));

    // uncomment to convert soft-breaks to hard breaks
    //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();
    Node document = parser.parse(md);
    String html = renderer.render(document);
    //do some more formatting
    html = RegexUtils.replaceMarkdownImage(html);
    html = RegexUtils.replacePlainImageLinks(html);
    return html;
  }
}
