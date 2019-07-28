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
        OutputStream historyDataOut = null;
        OutputStream logOut = null;
        String prefixCodeFile;
        try {
            prefixCodeFile = IOUtils.toString(new FileInputStream(folderUrl + "/prefixCode.txt"), Charsets.UTF_8);
            System.out.println(prefixCodeFile);
            logOut = new FileOutputStream(folderUrl + "/prefixCode.txt");
            String historyData = IOUtils.toString(new FileInputStream(folderUrl + "/historyData.txt"), Charsets.UTF_8);
            historyDataOut = new FileOutputStream(folderUrl + "/historyData.txt");
            String prefixCodeStr = prefixCodeFile.split("\n")[0].split("：")[1];
            int globalPrefixCode = Integer.parseInt(prefixCodeStr);
            String origin = IOUtils.toString(new FileInputStream(folderUrl + "/origin.txt"), Charsets.UTF_8);
            dataOut = new FileOutputStream(folderUrl + "/data.txt");
            String[] lines = origin.split("\\|\\|\\|");
            Map<String, Integer> imageNumMap = new HashMap<>();
            System.out.println("开始处理预数据");
            String datas = "";
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
                String description = lineData[3].trim();
                String newDescription = "\uD83C\uDE34️\uD83C\uDE34️\uD83C\uDE34️\uD83C\uDE34️\n"
                        + "批:" + price + "元\n"
                        + description + "\n"
                        + code;
                String dataLine = "【" + imageNum + "】\n" + "||" + size + "||" + code + "||\n" + newDescription + "\n|||\n";
                datas += dataLine;
                //存储当前imageNum对应的prefixCode
                imageNumMap.put(imageNumKey, prefixCode);
            }
            logOut.write(("当前编码前缀：" + globalPrefixCode).getBytes());
            dataOut.write(datas.getBytes());
            //输出历史记录
            historyData = historyData + "=============================================\n" + datas;
            historyDataOut.write(historyData.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeOut(dataOut);
            closeOut(logOut);
            closeOut(historyDataOut);
            System.out.println("完成数据处理");
        }
    }

    private static void closeOut(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getImageNumKey(String imageNum) {
        String[] rel = imageNum.split("\\.");
        return Arrays.asList(rel[0], rel[1], rel[2]).stream().collect(Collectors.joining("."));
    }
}
