package com.chenshinan.exercises.imagemark;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
        String logs = "";
        try {
            logs = IOUtils.toString(new FileInputStream(folderUrl + "/log.txt"), Charsets.UTF_8);
            System.out.println(logs);
            logOut = new FileOutputStream(folderUrl + "/log.txt");
            String historyData = IOUtils.toString(new FileInputStream(folderUrl + "/historyData.txt"), Charsets.UTF_8);
            historyDataOut = new FileOutputStream(folderUrl + "/historyData.txt");
            String prefixCodeStr = logs.split("\n")[0].split("：")[1];
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
            logOut.write(handleLogs(logs, String.valueOf(globalPrefixCode), imageNumMap.size(), imageNumMap.entrySet().stream().map(x -> String.valueOf(x.getValue())).collect(Collectors.joining("|"))).getBytes());
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

    /**
     * 处理输出的日志
     *
     * @param logs
     * @param globalPrefixCode
     * @param styleNum
     * @param prefixCodeStr
     * @return
     */
    private static String handleLogs(String logs, String globalPrefixCode, Integer styleNum, String prefixCodeStr) {
        DateFormat df = new SimpleDateFormat("MM.dd");
        String log = "日期" + df.format(new Date()) + "，共" + styleNum + "款" + "{imageNum}张图片，编号前缀为" + prefixCodeStr;
        String[] lines = logs.split("\n");
        lines[0] = "当前编码前缀：" + globalPrefixCode;
        List<String> list = new ArrayList<>(Arrays.asList(lines));
        list.add(log);
        return list.stream().collect(Collectors.joining("\n"));
    }
}
