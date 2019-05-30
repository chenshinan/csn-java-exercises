package com.chenshinan.exercises.commonmark;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

/**
 * @author shinan.chen
 * @since 2019/5/29
 */
public class MarkMain {
    public static final String IMG_BEGIN = "<img src=";
    public static final String IMG_END = "alt='img' >";
    public static final String IMG_TAG_BEGIN = "<span style='border: 4px solid rgba(0,191,165,0.16);display: inline-block'>";
    public static final String IMG_TAG_END = "</span>";
    public static void main(String[] args) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("# xxx\n" +
                "This is *Sparta*\n" +
                "* axax\n" +
                "谢谢`你`\n" +
                "![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190309111746.png)");
//        HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
//        System.out.println(htmlRenderer.render(document));
        TextContentRenderer textContentRenderer = TextContentRenderer.builder().build();
        System.out.println(textContentRenderer.render(document));

        String line = "<img src='https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190309111746.png' alt='img' >";
        line = line.replaceAll(IMG_BEGIN, IMG_TAG_BEGIN + IMG_BEGIN);
        line = line.replaceAll(IMG_END, IMG_END + IMG_TAG_END);
        System.out.println(line);
    }
}
