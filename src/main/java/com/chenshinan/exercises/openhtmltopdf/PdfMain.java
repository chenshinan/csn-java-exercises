package com.chenshinan.exercises.openhtmltopdf;

import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;

/**
 * @author shinan.chen
 * @since 2019/6/5
 */
public class PdfMain {
    public static void main(String[] args) {
        try (OutputStream os = new FileOutputStream("./out.pdf")) { // 输出的pdf
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            //下面这个方法是要自己指定 字体文件   不然转出的pdf文件中 中文会变成####
            builder.useFont(() -> {
                try {
                    return new FileInputStream("./NotoSansCJKtc-Regular.ttf");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }, "cjk", 400, BaseRendererBuilder.FontStyle.NORMAL, true); //第二个参数 一定要和文件名一样！！作用在html页面上
            String style = "" +
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
                    "table {\n" +
                    "            margin: 2px 0 14px;\n" +
                    "            color: #555;\n" +
                    "            width: auto;\n" +
                    "            border-collapse: collapse;\n" +
                    "            box-sizing: border-box;\n" +
                    "        }\n" +
                    "\n" +
                    "        table tr {\n" +
                    "            background-color: #eaeaea;\n" +
                    "        }\n" +
                    "\n" +
                    "        table td {\n" +
                    "            border: 1px solid #eaeaea;\n" +
                    "            display: table-cell;\n" +
                    "            white-space: nowrap;\n" +
                    "        }\n" +
                    "\n" +
                    "        table th {\n" +
                    "            border: 1px solid #eaeaea;\n" +
                    "            border-top: 0;\n" +
                    "            background-color: #7b8184;\n" +
                    "            font-weight: 300;\n" +
                    "            color: #fff;\n" +
                    "            height: 32px;\n" +
                    "            white-space: nowrap;\n" +
                    "            padding: 5px 14px 5px 12px;\n" +
                    "            display: table-cell;\n" +
                    "        }"+
                    "</style>\n" +
                    "";

            String html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/>\n" +
                    "" + style +// add your stylesheets, scripts styles etc.
                    // uncomment line below for adding style for custom embedded fonts
                    // nonLatinFonts +
                    "</head><body>" + "中国人" + "\n" +
                    "<table><thead><tr><th>标题1</th><th>标题2</th><th>标题3</th><th>标题4</th></tr></thead><tbody><tr><td>1</td><td rowspan=\"3\">asasas</td><td colspan=\"2\">33</td></tr><tr><td>1</td><td>1</td><td>1|x:a1w1w9(</td></tr><tr><td>1</td><td></td><td>1</td></tr></tbody></table>"+
                    "</body></html>";
            builder.withHtmlContent(html, "./root.htm");
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
