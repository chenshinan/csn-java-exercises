package com.chenshinan.exercises.openhtmltopdf;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.outputdevice.helper.FontFaceFontSupplier;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import sun.nio.cs.Surrogate;

import java.io.*;

/**
 * @author shinan.chen
 * @since 2019/6/5
 */
public class PdfMain {
    public static void main(String[] args) {
        try (OutputStream os = new FileOutputStream("./out10.pdf")) { // 输出的pdf
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //下面这个方法是要自己指定 字体文件   不然转出的pdf文件中 中文会变成####
            builder.useFont(()->{
                try {
                    return new FileInputStream("./NotoSansCJKtc-Regular.ttf");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }, "cjk", 400, BaseRendererBuilder.FontStyle.NORMAL, true); //第二个参数 一定要和文件名一样！！作用在html页面上
            String nonLatinFonts = "" +
                    "<style>\n" +
                    "@font-face {\n" +
                    "  font-family: 'simhei';\n" +
                    "  src: url('file:///Users/chenshinan/Downloads/simhei.ttf');\n" +
                    "  font-weight: normal;\n" +
                    "  font-style: normal;\n" +
                    "}\n" +
                    "body {\n" +
                    "    font-family: 'simhei';\n" +
                    "    overflow: hidden;\n" +
                    "    word-wrap: break-word;\n" +
                    "    font-size: 14px;\n" +
                    "}\n" +
                    "\n" +
                    "var,\n" +
                    "code,\n" +
                    "kbd,\n" +
                    "pre {\n" +
                    "    font: 0.9em 'noto-mono', Consolas, \"Liberation Mono\", Menlo, Courier, monospace;\n" +
                    "}\n" +
                    "</style>\n" +
                    "";

            String html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/>\n" +
                    "" +  nonLatinFonts +// add your stylesheets, scripts styles etc.
                    // uncomment line below for adding style for custom embedded fonts
                    // nonLatinFonts +
                    "</head><body>" + "中国人" + "\n" +
                    "</body></html>";
            builder.withHtmlContent(html,"./root.htm");
            builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
            builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
            builder.defaultTextDirection(BaseRendererBuilder.TextDirection.LTR);
//            builder.withUri("file:///Users/chenshinan/Downloads/root.htm");
            //第一个参数是html页面  第二个参数 是一个全空的文件 里面什么都没有 但是后缀一定要是。htm  作用类似于一个画板 如果不添加这个参数或者置为null 则html文件中的图片 不会转化 所以一定需要加
            builder.toStream(os);
            builder.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
