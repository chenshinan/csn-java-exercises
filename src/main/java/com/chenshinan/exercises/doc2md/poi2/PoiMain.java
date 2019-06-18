package com.chenshinan.exercises.doc2md.poi2;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * @author shinan.chen
 * @since 2019/6/14
 */
public class PoiMain {
    public static void main(String[] args) {
        String tagPath = "./";
        String sourcePath = "./xx2.docx";
        String outPath = "poi-out.html";
        try {
//            doc03ToHtml(tagPath, sourcePath, outPath);
            doc07ToHtml(tagPath, sourcePath, outPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String doc03ToHtml(String tagPath,
                                     String sourceFileName, String outPath) throws Exception {
        File file = new File(tagPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
        org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
        //保存图片，并返回图片的相对路径
        wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
            try (FileOutputStream out = new FileOutputStream(tagPath + name)) {
                out.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "image/" + name;
        });
        wordToHtmlConverter.processDocument(wordDocument);
        org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(outPath));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        return outPath;
    }

    public static void doc07ToHtml(String imageFile, String fileName, String htmFile) throws IOException {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("sorry file does not exists");
        } else {
            if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX") || f.getName().endsWith(".doc")) {
                //1:加载文档到XWPFDocument
                InputStream in = new FileInputStream(f);
                XWPFDocument document = new XWPFDocument(in);
                //2：加载图片到指定文件夹
                File imgFile = new File(imageFile);
                XHTMLOptions options = XHTMLOptions.create();
                options.setExtractor(new FileImageExtractor(imgFile));

                //3：转换XWPFDocument to XHTML
                OutputStream out = new FileOutputStream(new File(htmFile));
                XHTMLConverter.getInstance().convert(document, out, options);
            } else {
                System.out.println("Enter only MS Office 2007+ files");
            }
        }
    }
}
