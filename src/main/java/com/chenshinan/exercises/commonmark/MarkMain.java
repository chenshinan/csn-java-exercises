package com.chenshinan.exercises.commonmark;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * @author shinan.chen
 * @since 2019/5/29
 */
public class MarkMain {
    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Sparta*");
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
        System.out.println(htmlRenderer.render(document));
        TextContentRenderer textContentRenderer = TextContentRenderer.builder().build();
        System.out.println(textContentRenderer.render(document));
    }
}
