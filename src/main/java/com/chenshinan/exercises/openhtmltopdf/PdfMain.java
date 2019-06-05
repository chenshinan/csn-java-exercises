package com.chenshinan.exercises.openhtmltopdf;

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
        try (OutputStream os = new FileOutputStream("./out5.pdf")) { // 输出的pdf
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //下面这个方法是要自己指定 字体文件   不然转出的pdf文件中 中文会变成####
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    try {
                        return new FileInputStream("./simhei.ttf");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, "simhei", 400, BaseRendererBuilder.FontStyle.NORMAL, true); //第二个参数 一定要和文件名一样！！作用在html页面上
//            builder.withHtmlContent("<style>\n" +
//                    "* {\n" +
//                    "  font-family: 'simhei';   //value是指定字体时 第二个参数\n" +
//                    "}\n" +
//                    "</style><h2>acaca1在哪里</h2>","./root.htm");
//            builder.useFastMode();
            builder.withUri("file:///Users/chenshinan/Downloads/root.htm");
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
