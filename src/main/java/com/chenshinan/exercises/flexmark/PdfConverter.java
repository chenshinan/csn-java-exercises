package com.chenshinan.exercises.flexmark;

import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class PdfConverter {
    static final MutableDataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
            , TocExtension.create()).toMutable()
            .set(TocExtension.LIST_CLASS, PdfConverterExtension.DEFAULT_TOC_LIST_CLASS)
            //.set(HtmlRenderer.GENERATE_HEADER_ID, true)
            //.set(HtmlRenderer.RENDER_HEADER_ID, true)
            ;

    static String getResourceFileContent(String resourcePath) {
        StringWriter writer = new StringWriter();
        getResourceFileContent(writer, resourcePath);
        return writer.toString();
    }

    private static void getResourceFileContent(final StringWriter writer, final String resourcePath) {
        InputStream inputStream = PdfConverter.class.getResourceAsStream(resourcePath);
        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final String pegdown = "" +
                "[TOC]\n" +
                "\n" +
                "#Heading\n" +
                "-----\n" +
                "paragraph text \n" +
                "lazy continuation\n" +
                "\n" +
                "* list item\n" +
                "    > block quote\n" +
                "    lazy continuation\n" +
                "\n" +
                "\n## Heading 2" +
                "\n" +
                "~~~info\n" +
                "   with uneven indent\n" +
                "      with uneven indent\n" +
                "indented code\n" +
                "~~~ \n" +
                "\n" +
                "\n## Heading 3" +
                "\n" +
                "       with uneven indent\n" +
                "          with uneven indent\n" +
                "    indented code\n" +
                "1. numbered item 1   \n" +
                "1. numbered item 2   \n" +
                "1. numbered item 3   \n" +
                "    - bullet item 1   \n" +
                "    - bullet `美国` 2   \n" +
                "    - bullet `item` 3   \n" +
                "        1. numbered 中国人   \n" +
                "        1. numbered sub-item 2   \n" +
                "        1. numbered sub-item 3   \n" +
                "```java\n"+
                "int x =1;\n"+
                "```\n"+
//                "![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190604195238.png)\n"+
                "![image](https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D450%2C600%3B/sign=6f934d315bfbb2fb347e50167a7a0c92/a71ea8d3fd1f4134a17740122b1f95cad1c85e1d.jpg)"+
                "    \n" +
                "    ~~~info\n" +
                "       with uneven indent\n" +
                "          with uneven indent\n" +
                "    indented code\n" +
                "    ~~~ \n" +
                "    \n" +
                "           with uneven indent\n" +
                "              with uneven indent\n" +
                "        indented code\n" +
                "";
        System.out.println("pegdown\n");
        System.out.println(pegdown);

        final Parser PARSER = Parser.builder(OPTIONS).build();
        final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();

        Node document = PARSER.parse(pegdown);
        String html = RENDERER.render(document);

        // add embedded fonts for non-latin character set rendering
        // change file:///usr/local/fonts/ to your system's path for font installation Unix path or
        // on windows the path should start with `file:/X:/...` where `X:/...` is the drive
        // letter followed by the full installation path.
        //
        // Google Noto fonts can be downloaded from https://www.google.com/get/noto/
        // `arialuni.ttf` from https://www.wfonts.com/font/arial-unicode-ms
//        String url = "'file:///Users/chenshinan/Downloads/arialuni.ttf'";
        String url = "'"+PdfConverter.class.getResource("/arialuni.ttf")+"'";
        System.out.println(url);
        String nonLatinFonts = "" +
                "<style>\n" +
                "@font-face {\n" +
                "  font-family: 'font';\n" +
                "  src: url("+url+");\n" +
                "}\n" +
                "* {\n" +
                "    font-family: 'font';\n" +
                "}\n" +
                "var,\n" +
                "code,\n" +
                "kbd,\n" +
                "pre {\n" +
                "    font: 0.9em 'font';\n" +
                "}\n" +
                "code {\n" +
                "    color:#c1788b;\n" +
                "}\n" +
                "pre {\n" +
                "    background-color:#f5f7f8; padding:18px\n" +
                "}\n" +
                "pre code{\n" +
                "    color:#000000;\n" +
                "}\n" +
                "img {\n" +
                "    max-width: 100%\n" +
                "}\n" +
                "</style>\n" +
                "";

        html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">\n" +
                "" +  nonLatinFonts +// add your stylesheets, scripts styles etc.
                // uncomment line below for adding style for custom embedded fonts
                // nonLatinFonts +
                "</head><body>" + html + "\n" +
                "</body></html>";

        PdfConverterExtension.exportToPdf("./out2.pdf", html, "", OPTIONS);
    }
}