package com.chenshinan.exercises.html2md;

import com.vladsch.flexmark.convert.html.FlexmarkHtmlParser;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author shinan.chen
 * @since 2019/6/13
 */
public class Html2mdMain {
    public static void main(String[] args) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./docx4j-out.html");
            String html = IOUtils.toString(inputStream, String.valueOf(Charsets.UTF_8));
            String markdown = FlexmarkHtmlParser.parse(html);
            System.out.println(markdown);
//            new FileOutputStream(new File("./docx4j-out.html"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
