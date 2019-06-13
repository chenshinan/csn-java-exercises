package com.chenshinan.exercises.doc2md.poi;

import java.io.FileInputStream;

/**
 * @author shinan.chen
 * @since 2019/6/13
 */
public class DocMain {
    public static void main(String[] args) {
        System.out.println("属性文件路径:");
        try {
//            doc2mdInit();
            FileInputStream fileStream = FileOperator.readFile();
            Doc2MD doc2MDParser = new Doc2MD(fileStream);
            doc2MDParser.parseSingleDoc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
