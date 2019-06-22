package com.chenshinan.exercises.imagemark;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author shinan.chen
 * @since 2019/6/22
 */
public class DataMain {
    public static void main(String[] args) {
        String folderUrl = "/Users/chenshinan/Downloads/img-6.19";
        OutputStream dataOut = null;
        OutputStream prefixCodeOut = null;
        try {
            String prefixCodeStr = IOUtils.toString(new FileInputStream(folderUrl + "/code.txt"), Charsets.UTF_8);
            int prefixCode = Integer.parseInt(prefixCodeStr);
            String origin = IOUtils.toString(new FileInputStream(folderUrl + "/origin.txt"), Charsets.UTF_8);
            dataOut = new FileOutputStream(folderUrl + "/data.txt");
            prefixCodeOut = new FileOutputStream(folderUrl + "/code.txt");
            String[] lines = origin.split("\\|\\|\\|");
            for (String line : lines) {
                line = line.trim();
                if (line == null || "".equals(line)) {
                    continue;
                }
                String[] lineData = line.split("\\|\\|");
                String imageNum = lineData[0];
                String size = lineData[1];
                int price = Integer.parseInt(lineData[2]);

                String code = "编号" + prefixCode + String.valueOf(prefixCode * 3 - price);
                String description = lineData[3];
                String newDescription = "批:" + price + "元\n"
                        + description + "\n"
                        + code;
                String dataLine = "【" + imageNum + "】\n" + "||" + size + "||" + code + "||\n" + newDescription + "\n|||\n";
                dataOut.write(dataLine.getBytes());
                if (imageNum.split("\\.").length == 3) {
                    prefixCode++;
                }
            }
            prefixCodeOut.write(String.valueOf(prefixCode).getBytes());
            dataOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataOut != null) {
                try {
                    dataOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (prefixCodeOut != null) {
                try {
                    prefixCodeOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
