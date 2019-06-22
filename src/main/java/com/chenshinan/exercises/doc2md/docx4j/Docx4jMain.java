package com.chenshinan.exercises.doc2md.docx4j;

import com.chenshinan.exercises.flexmark.HtmlUtil;
import com.vladsch.flexmark.convert.html.FlexmarkHtmlParser;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author shinan.chen
 * @since 2019/6/13
 */
public class Docx4jMain {
    public static void main(String[] args) {

        WordprocessingMLPackage wordMLPackage;
        try {
            wordMLPackage = Docx4J.load(new FileInputStream("./xx5.doc"));
            System.out.println(wordMLPackage);
            for (Object o : wordMLPackage.getMainDocumentPart().getContent()) {
                System.out.println(o);
            }
            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();

//            htmlSettings.setImageDirPath("./");
            htmlSettings.setImageTargetUri("./");
            htmlSettings.setWmlPackage(wordMLPackage);
//            String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  table, caption, tbody, tfoot, thead, tr, th, td "
//                    + "{ margin: 0; padding: 0; border: 0;}" + "body {line-height: 1;} ";
//
//            htmlSettings.setUserCSS(userCSS);

            Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);

            Docx4J.toHTML(htmlSettings, new FileOutputStream(new File("./docx4j-out.html")),
                    Docx4J.FLAG_EXPORT_PREFER_XSL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
