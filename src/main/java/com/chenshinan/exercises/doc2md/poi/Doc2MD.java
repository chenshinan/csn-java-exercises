package com.chenshinan.exercises.doc2md.poi;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by JackWang on 2015/5/12.
 * 核心类 将doc文件转换为md文件
 */
public class Doc2MD {
    /**
     * 输入单个文件时的文件流
     */
    private FileInputStream inputDoc = null;
    /**
     * 输入文件集时的文件流
     * Key：文件名字，去除了后缀名
     * Value：文件流
     */
    private HashMap<String, FileInputStream> inputDocMap = null;
    /**
     * 读到的文件流
     */
    private XWPFDocument document = null;
    /**
     * 当前文档中所有段落行；
     */
    private List<XWPFParagraph> paragraphList = null;
    /**
     * 当前文档中所有表格对象
     */
    private List<XWPFTable> tableList = null;
    /**
     * 当前文档中所有图片数目
     */
    private List<XWPFPictureData> pictureList = null;
    /**
     * 当前文档中所有超链接
     */
    private XWPFHyperlink[] hyperlinks = null;
    /**
     * 当前文档中链接数据，放在一个字符串写入
     */
    private int LinksCount = 1;
    /**
     * 当前文档图片数据，放在一个字符串写入
     */
    private int pictureCount = 1;

    /**
     * 多个文件流构造函数
     *
     * @param inputDocMap 多个文件流的Map
     */
    public Doc2MD(HashMap<String, FileInputStream> inputDocMap) {
        this.inputDocMap = inputDocMap;
    }

    /**
     * 单个文件流构造函数
     *
     * @param inputDoc
     */
    public Doc2MD(FileInputStream inputDoc) {
        this.inputDoc = inputDoc;
    }

    /**
     * 读取文档流
     *
     * @param stream
     * @return
     * @throws Exception
     */
    private XWPFDocument readDocument(FileInputStream stream) throws Exception {
        return new XWPFDocument(stream);
    }

    /**
     * 计算当前文档中所有非空行
     *
     * @return
     */
    private int countObjects() {
        return paragraphList.size() + tableList.size() + pictureList.size() + LinksCount;
    }

    /**
     * 核心方法，转换Doc
     *
     * @return
     * @throws Exception
     */
    public boolean parseDoc() throws Exception {
        Set<String> keySet = inputDocMap.keySet();
        for (String title : keySet) {
            document = readDocument(inputDocMap.get(title));
            paragraphList = document.getParagraphs(); //所有回车行对象
            tableList = document.getTables();  //所有表格对象
            hyperlinks = document.getHyperlinks(); //所有超链接对象
            pictureList = document.getAllPictures(); //所有图片对象
            System.out.println(title + " Creating... ");
            int ObjectCount = countObjects();
            FileWriter fileOut = new FileWriter(new File(title));
            String[] temp2Write = new String[ObjectCount];
            //处理段落、标题、列表
            for (int index = 0; index < paragraphList.size(); index++) {
                XWPFParagraph paragraph = paragraphList.get(index);
                String tempValue = parseParagraph(paragraph);
                temp2Write[document.getPosOfParagraph(paragraph)] = tempValue;
            }
            //处理表格
            for (int index = 0; index < tableList.size(); index++) {
                XWPFTable table = tableList.get(index);
                String tempValue = parseTable(table);
                temp2Write[document.getPosOfTable(table)] = tempValue;
            }
            //处理图片
            {
                String tempValue = parsePictures(pictureList);
                temp2Write[ObjectCount - 2] = tempValue;
            }

            //处理超链接
            {
                String tempValue = parseHyperLinks(hyperlinks);
                temp2Write[ObjectCount - 1] = tempValue;
            }
            FileOperator.writeFile(fileOut, temp2Write);
        }
        return true;
    }

    /**
     * 核心方法，转换Doc
     *
     * @return
     * @throws Exception
     */
    public boolean parseSingleDoc() throws Exception {
        document = readDocument(inputDoc);
        paragraphList = document.getParagraphs(); //所有回车行对象
        tableList = document.getTables();  //所有表格对象
        hyperlinks = document.getHyperlinks(); //所有超链接对象
        pictureList = document.getAllPictures(); //所有图片对象
        System.out.println(" Creating... ");
        int ObjectCount = countObjects();
        FileWriter fileOut = new FileWriter(new File("xx"));
        String[] temp2Write = new String[ObjectCount];
        //处理段落、标题、列表
        for (int index = 0; index < paragraphList.size(); index++) {
            XWPFParagraph paragraph = paragraphList.get(index);
            String tempValue = parseParagraph(paragraph);
            temp2Write[document.getPosOfParagraph(paragraph)] = tempValue;
        }
        //处理表格
        for (int index = 0; index < tableList.size(); index++) {
            XWPFTable table = tableList.get(index);
            String tempValue = parseTable(table);
            temp2Write[document.getPosOfTable(table)] = tempValue;
        }
        //处理图片
        {
            String tempValue = parsePictures(pictureList);
            temp2Write[ObjectCount - 2] = tempValue;
        }

        //处理超链接
        {
            String tempValue = parseHyperLinks(hyperlinks);
            temp2Write[ObjectCount - 1] = tempValue;
        }
        FileOperator.writeFile(fileOut, temp2Write);
        return true;
    }

    /**
     * 处理图片
     *
     * @param pictureDatas
     * @return
     */
    private String parsePictures(List<XWPFPictureData> pictureDatas) throws IOException {
        String tempValue = "<br />**发布前请删除**<br>";
        if (pictureDatas == null) {
            return tempValue;
        }
        tempValue += "本文档所有图片：<br />";
        for (int index = 0; index < pictureDatas.size(); index++) {
            XWPFPictureData pictureData = pictureDatas.get(index);
            tempValue += (index + 1) + ". ";
            tempValue += pictureData.getFileName();
            File imgFile = new File(FileOperator.getImgFilePath() + File.separator + pictureData.getFileName());
            FileOutputStream outStream = new FileOutputStream(imgFile);
            try {
                FileOperator.writeImage(outStream, pictureData.getData());
            } finally {
                FileOperator.closeFileOutStream(outStream);
            }
            tempValue += "<br />";
        }
        tempValue += "<br />\n手动设置图片，方法：![图片文本](图片地址)<br />**发布前请删除**";
        return tempValue;
    }

    /**
     * 暂时不能识别超链接位置，统一放在文后
     *
     * @param links
     * @return
     */
    private String parseHyperLinks(XWPFHyperlink[] links) {
        String tempValue = "";
        if (links == null) {
            return tempValue;
        }
        tempValue = "<br />**发布前请删除**<br />本文档所有超链接地址：<br />";
        for (int index = 0; index < links.length; index++) {

            tempValue += (index + 1) + ". ";
            tempValue += links[index].getURL();
            tempValue += "<br />";
        }
        tempValue += "<br />需要手动设置超链接，方法：[链接文本](链接地址)<br />**发布前请删除**";
        return tempValue;
    }

    /**
     * 转换表格
     *
     * @param table
     * @return
     */
    private String parseTable(XWPFTable table) {
        String tempValue = "";
        if (table == null) {
            return tempValue;
        }
        tempValue += "<table>";
        for (int index = 0; index < table.getNumberOfRows(); index++) {
            XWPFTableRow row = table.getRow(index);
            tempValue += "<tr>";
            List<XWPFTableCell> CellList = row.getTableCells();
            for (XWPFTableCell cell : CellList) {
                if (index != 0) {
                    tempValue += "<td>";
                    tempValue += cell.getText();
                    tempValue += "</td>";
                } else {
                    tempValue += "<th>";
                    tempValue += cell.getText();
                    tempValue += "</th>";
                }

            }
            tempValue += "</tr>";
        }
        tempValue += "</table>";
        return tempValue;
    }

    /**
     * 转换段落，标题，列表
     *
     * @param paragraph
     * @return
     */
    private String parseParagraph(XWPFParagraph paragraph) {
        if (paragraph.isEmpty()) {
            return "";
        }
        String tempValue = "";
        String style = paragraph.getStyle();
        if (style != null) {
            style.toLowerCase();
            if ("a3".equals(style)) {
                tempValue += "# ";
            } else if ("1".equals(style)) {
                tempValue += "## ";
            } else if ("2".equals(style)) {
                tempValue += "### ";
            } else if ("3".equals(style)) {
                tempValue += "#### ";
            } else if ("a4".equals(style)) {
                tempValue += " + ";
            }
        } else {

        }
        tempValue += paragraph.getParagraphText();
        return tempValue;
    }

    /**
     * 获取所有非空对象
     *
     * @return
     */
    private List<XWPFParagraph> getNotNullParas() {
        List<XWPFParagraph> tempParags = document.getParagraphs();
        List<XWPFParagraph> paragraphs = new ArrayList<XWPFParagraph>();
        for (XWPFParagraph paragraph : tempParags) {
            if (!("".equals(paragraph.getText()) || null == paragraph.getText())) {
                paragraphs.add(paragraph);
            }
        }
        return paragraphs;
    }
}
