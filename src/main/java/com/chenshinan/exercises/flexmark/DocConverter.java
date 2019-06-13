package com.chenshinan.exercises.flexmark;

import com.vladsch.flexmark.docx.converter.DocxRenderer;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.pdf.converter.PdfConverterExtension;
import com.vladsch.flexmark.profiles.pegdown.Extensions;
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class DocConverter {
    static final MutableDataHolder OPTIONS = PegdownOptionsAdapter.flexmarkOptions(
            Extensions.ALL & ~(Extensions.ANCHORLINKS | Extensions.EXTANCHORLINKS_WRAP)
            , TocExtension.create()).toMutable()
            .set(TocExtension.LIST_CLASS, PdfConverterExtension.DEFAULT_TOC_LIST_CLASS)
            .set(HtmlRenderer.GENERATE_HEADER_ID, true)
            //.set(HtmlRenderer.RENDER_HEADER_ID, true)
            ;

    static String getResourceFileContent(String resourcePath) {
        StringWriter writer = new StringWriter();
        getResourceFileContent(writer, resourcePath);
        return writer.toString();
    }

    private static void getResourceFileContent(final StringWriter writer, final String resourcePath) {
        InputStream inputStream = DocConverter.class.getResourceAsStream(resourcePath);
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
                "        1. numbered sub-item 3   \n\n" +
                "| 项目        | 价格   |  数量休息休息休息休息  |\n" +
                "| --------   | -----  | ----  |\n" +
                "| 计算机|1600|   5     |\n" +
                "```java\n" +
                "int x =1;\n" +
                "```\n" +
//                "![image](https://csn-images.oss-cn-shenzhen.aliyuncs.com/markdown/20190604195238.png)\n"+
                "![image](https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D450%2C600%3B/sign=6f934d315bfbb2fb347e50167a7a0c92/a71ea8d3fd1f4134a17740122b1f95cad1c85e1d.jpg)" +
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
                "\n" +
                "\n## Heading 4" +
                "";
//        String pegdown = "### 标题1\n" +
//                "\n" +
//                "<span style=\"color:#353535\">1976年7月，20多名科学家、新闻记者组成了考察队，对长江上源大大小小十几条河流进行了仔细考察，为确定长江新源头寻找证据。查勘结果表明：长江的源头不在巴颜喀拉山南麓，而是在唐古拉山主峰西南侧的沱沱河；长江全长不止5800多公里，而是6300多公里，是世界第三长河流。</span>  \n" +
//                "![](https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D450%2C600%3B/sign=6f934d315bfbb2fb347e50167a7a0c92/a71ea8d3fd1f4134a17740122b1f95cad1c85e1d.jpg)\n" +
//                "<span style=\"color:#b8b8b8\">*我的图片*</span>  \n" +
//                "<span style=\"color:#353535\">而在接下来的考察中，科学家还揭示了一个惊人的事实：在遥远的地质年代，中国大陆最早诞生的河流是汉江，而不是长江与黄河。大约在汉江诞生7亿年后，长江才出现。而且在青藏高原崛起之前，长江还一度借汉江河道，西流青藏地区注入地中海和印度洋。</span>\n" +
//                "\n" +
//                "### <span style=\"color:#353535\">标题2</span>\n" +
//                "\n" +
//                "<span style=\"color:#353535\">汉江，这条中国内陆最古老的河流，在漫长的岁月里清流奔涌，浩浩荡荡，不仅孕育出一片片平畴沃野，也见证了多民族的争战与融合，书写了中国历史上的辉煌篇章。</span>  \n" +
//                "<span style=\"color:#353535\">沿途众纳百川秦岭和巴山的奉献</span>  \n" +
//                "<span style=\"color:#353535\">从古至今，汉江之水之所以浩荡不息，是因为汉江拥有众多悠长的支流，它们用连绵不断的水流滋养着这条古老的大河。</span>  \n" +
//                "<span style=\"color:#353535\">从源头到武汉汉口，到底有多少河流投入汉江的怀抱，恐怕很难计算。不过，在我围绕汉江干流奔走时，每天都会和不计其数、大小不一的河流相遇。![](https://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D450%2C600%3B/sign=0d5bdb380d4f78f0805e92f74c012663/0df431adcbef7609bd80dcf320dda3cc7dd99ed7.jpg)</span>\n" +
//                "\n" +
//                "### <span style=\"color:#353535\">标题3</span>\n" +
//                "\n" +
//                "<span style=\"color:#353535\">现在我们看到的汉江，从陕西宁强县起步，在陕西境内接纳的有名有姓，且流程较长、流量较大的支流，就有褒河、湑水河、金水河、子午河、月河、旬河、蜀河、冷水河、南沙河、牧马河、任河、岚河、坝河。这些河流的源头均在秦岭南坡或大巴山北麓，从高山、丛林跌宕而来，不仅让古老的汉江拥有了撼天动地的气势，也形成了汉江流域最为密集的河网。这些于千山万壑中劈山为道的河流，一旦将滚滚水源呈献给汉江，生命便宣告终结。然而很少有人注意到，在这些较大的支流背后的群山中，还有千万条较小的溪流汇入。</span>\n" +
//                "\n" +
//                "### <span style=\"color:#353535\">标题4</span>\n" +
//                "\n" +
//                "<span style=\"color:#353535\">在汉江上游的陕西境内，最大的支流当属在紫阳县汇入汉江的任河。</span>\n" +
//                "<span style=\"color:#353535\"></span>";
        System.out.println("pegdown\n");
        System.out.println(pegdown);

        final Parser PARSER = Parser.builder(OPTIONS).build();
        final HtmlRenderer RENDERER = HtmlRenderer.builder(OPTIONS).build();
        Node document = PARSER.parse(pegdown);
        String htmlBody = RENDERER.render(document);

    }
}