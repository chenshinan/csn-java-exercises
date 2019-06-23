package com.chenshinan.exercises.imagemark;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/6/22
 */
public class DataMain {
    public static void main(String[] args) {
        String folderUrl = "/Users/chenshinan/Downloads/watermark";
        OutputStream dataOut = null;
        OutputStream prefixCodeOut = null;
        try {
            String prefixCodeStr = IOUtils.toString(new FileInputStream(folderUrl + "/code.txt"), Charsets.UTF_8);
            int globalPrefixCode = Integer.parseInt(prefixCodeStr);
            String origin = IOUtils.toString(new FileInputStream(folderUrl + "/origin.txt"), Charsets.UTF_8);
            dataOut = new FileOutputStream(folderUrl + "/data.txt");
            prefixCodeOut = new FileOutputStream(folderUrl + "/code.txt");
            String[] lines = origin.split("\\|\\|\\|");
            Map<String, Integer> imageNumMap = new HashMap<>();
            for (String line : lines) {
                line = line.trim();
                if (line == null || "".equals(line)) {
                    continue;
                }
                String[] lineData = line.split("\\|\\|");
                String imageNum = lineData[0];
                String size = lineData[1];
                int price = Integer.parseInt(lineData[2]);
                //获取编号前缀
                String imageNumKey = getImageNumKey(imageNum);
                Integer prefixCode = imageNumMap.get(imageNumKey);
                if (prefixCode == null) {
                    prefixCode = globalPrefixCode;
                    globalPrefixCode++;
                }

                String code = "编号" + prefixCode + String.valueOf(prefixCode * 3 - price);
                String description = lineData[3];
                String newDescription = "\uD83C\uDE34️\uD83C\uDE34️\uD83C\uDE34️\uD83C\uDE34️\n"
                        + "批:" + price + "元\n"
                        + description + "\n"
                        + code;
                String dataLine = "【" + imageNum + "】\n" + "||" + size + "||" + code + "||\n" + newDescription + "\n|||\n";
                dataOut.write(dataLine.getBytes());

                //存储当前imageNum对应的prefixCode
                imageNumMap.put(imageNumKey, prefixCode);
            }
            prefixCodeOut.write(String.valueOf(globalPrefixCode).getBytes());
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

    private static String getImageNumKey(String imageNum) {
        String[] rel = imageNum.split("\\.");
        return Arrays.asList(rel[0], rel[1], rel[2]).stream().collect(Collectors.joining("."));
    }
}
