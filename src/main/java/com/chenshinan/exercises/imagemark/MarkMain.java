package com.chenshinan.exercises.imagemark;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.util.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/6/22
 */
public class MarkMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(MarkMain.class);
    private static final String FONT_NAME = "宋体";
    private static final int FONT_STYLE = Font.BOLD;
    private static final Color SIZE_FONT_COLOR = Color.decode("#000000");
    private static final Color CODE_FONT_COLOR = Color.decode("#28E613");
    private static final float ALPHA = 1;
    private static final List<String> functions = Arrays.asList(FunctionType.WATERMARK_SIZE, FunctionType.WATERMARK_CODE, FunctionType.SPLIT_FOLDER, FunctionType.CHECK_COUNT);
    private static List<String> mainImgFolderUrls = new ArrayList<>();

    public static void main(String[] args) {
        String folderUrl = "/Users/chenshinan/Downloads/watermark";
        List<String> imageUrlList = new ArrayList<>();
        List<String> imageUrlSingleList = new ArrayList<>();
        File folder = new File(folderUrl);
        long startTime = System.currentTimeMillis();
        OutputStream imageOut = null;
        int count = 0;
        try {
            String data = IOUtils.toString(new FileInputStream(folderUrl + "/data.txt"), Charsets.UTF_8);
            Map<String, ImageData> map = handleData(data);
            LOGGER.info("开始批量上码");
            String outStr = folderUrl + "/out/";
            File out = new File(outStr);
            if (!out.exists()) {
                out.mkdir();
            }
            if (functions.contains(FunctionType.CHECK_COUNT)) {
                checkFileCount(folder, map, folderUrl);
            }
            List<File> folders = Arrays.stream(folder.listFiles()).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
            for (File imageFolder : folders) {
                if (imageFolder.isDirectory()) {
                    String imageNum = imageFolder.getName();
                    ImageData imageData = map.get(imageNum);
                    if (imageData != null) {
                        LOGGER.info("开始【{}】批量上码...", imageNum);
                        //创建文件输出流，指向最终的目标文件
                        String outFolderStr = folderUrl + "/out/" + imageNum;
                        getFolder(outFolderStr);
                        //生成主图文件夹
                        String mainImgFolder = outFolderStr + "/主图";
                        getFolder(mainImgFolder);
                        mainImgFolderUrls.add(mainImgFolder);
                        int folderNum = 1;
                        List<File> fileList = Arrays.stream(imageFolder.listFiles()).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
                        for (File file : fileList) {
                            String fildName = file.getName();
                            if (fildName.split("\\.")[1].equalsIgnoreCase("jpg")) {
                                //创建图片缓存对象
                                //解码原图
                                Image image = ImageIO.read(file);
                                //获取原图的宽度
                                int width = image.getWidth(null);
                                //获取原图的高度
                                int height = image.getHeight(null);
                                //图像颜色的设置
                                BufferedImage bufferedImage1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                                //创建Java绘图工具对象
                                Graphics2D g = bufferedImage1.createGraphics();
                                //使用绘图工具对象将原图绘制到缓存图片对象
                                g.drawImage(image, 0, 0, width, height, null);
                                //如果为x说明不需要加水印
                                if (!imageData.getSize().equals("x")) {
                                    //由于w/h/fs=6000/4000/200=3000/2000/100，所以fs=w/30
                                    int fontSize = width / 30;
                                    if (functions.contains(FunctionType.WATERMARK_SIZE)) {
                                        //绘制size
                                        printSize(g, height, width, imageData.getSize(), fontSize);
                                    }
                                    int fontCodeSize = width / 35;
                                    if (functions.contains(FunctionType.WATERMARK_CODE)) {
                                        //绘制code
                                        printCode(g, height, width, imageData.getCode(), fontCodeSize);
                                    }
                                }
                                //释放工具
                                g.dispose();
                                //输出到整体文件夹
                                String url2 = outFolderStr + "/" + fildName;
                                outputImage(imageOut, bufferedImage1, url2);
                                count++;
                                if (functions.contains(FunctionType.SPLIT_FOLDER)) {
                                    //每个颜色单独文件夹
                                    String outFolderColorStr = outFolderStr + "/" + folderNum;
                                    getFolder(outFolderColorStr);
                                    //输出到单独文件夹
                                    String url1 = outFolderColorStr + "/" + fildName;
                                    outputImage(imageOut, bufferedImage1, url1);
                                    //如果9张了，则创建一个新文件夹
                                    if (count % 9 == 0) {
                                        folderNum++;
                                    }
                                    //如果是第一张则再输出到单图文件夹
                                    if (count % 9 == 1) {
                                        String urlSingle = mainImgFolder + "/" + fildName;
                                        outputImage(imageOut, bufferedImage1, urlSingle);
                                        imageUrlList.add(outFolderColorStr);
                                        imageUrlList.add(urlSingle);
                                        imageUrlSingleList.add(urlSingle);
                                    }
                                }
                            }
                        }
                        //如果是某款的最后文件夹，则创建主图汇总
                        if (imageData.getLastImageNum()) {
                            copyMainFolderImage(outFolderStr);
                            imageUrlList.addAll(imageUrlSingleList);
                            //清空单独图表
                            imageUrlSingleList = new ArrayList<>();
                        }
                        LOGGER.info("完成【{}】批量上码", imageNum);
                    } else {
                        LOGGER.info("没找到【{}】的数据，跳过", imageNum);
                    }
                }
            }
            Float time = (float) (System.currentTimeMillis() - startTime) / 1000;
            recordLog(folderUrl, count, time, map);
            outputImageUrl(folderUrl, imageUrlList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            closeOut(imageOut);
        }
    }

    private static void outputImageUrl(String folderUrl, List<String> imageUrlList) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(folderUrl + "/imageUrl.txt");
            output.write(imageUrlList.stream().collect(Collectors.joining("\n")).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeOut(output);
        }

    }

    /**
     * 文本长度的处理：文字水印的中英文字符的宽度转换
     *
     * @param text
     * @return
     */
    public static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            //中文字符
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        //中文和英文字符的转换
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * 添加鞋码水印
     *
     * @param g
     * @param height
     * @param width
     * @param markText
     */
    private static void printSize(Graphics2D g, int height, int width, String markText, int fontSize) {
        //使用绘图工具对象将水印（文字/图片）绘制到缓存图片
        g.setFont(new Font(FONT_NAME, FONT_STYLE, fontSize));
        g.setColor(SIZE_FONT_COLOR);

        //获取文字水印的宽度和高度值
        //文字水印宽度
        int sizeWidth = fontSize * getTextLength(markText);
        //文字水印高度
        int sizeHeight = fontSize;
        //横坐标
        int x = (width - sizeWidth) / 2;
        //纵坐标
        int y = height - sizeHeight / 2;
        //水印透明度的设置
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
        //绘制文字
        g.drawString(markText, x, y);
    }

    /**
     * 添加编号水印
     *
     * @param g
     * @param height
     * @param width
     * @param markText
     */
    private static void printCode(Graphics2D g, int height, int width, String markText, int fontSize) {
        g.setFont(new Font(FONT_NAME, FONT_STYLE, fontSize));
        g.setColor(CODE_FONT_COLOR);
        //获取文字水印的宽度和高度值
        //文字水印宽度
        int codeWidth = fontSize * getTextLength(markText);
        //文字水印高度
        int codeHeight = fontSize;
        //横坐标
        int codeX = width - codeWidth - codeHeight / 2 * 3;
        //纵坐标
        int codeY = height - codeHeight / 2;
        //水印透明度的设置
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
        //绘制文字
        g.drawString(markText, codeX, codeY);
    }

    /**
     * 处理生成的数据
     *
     * @param data
     * @return
     */
    private static Map<String, ImageData> handleData(String data) {
        String[] lines = data.split("\\|\\|\\|");
        Map<String, ImageData> map = new HashMap<>(lines.length);
        String lastImageNum = null;
        for (String line : lines) {
            ImageData imageData = new ImageData();
            line = line.trim();
            if (line == null || "".equals(line)) {
                continue;
            }
            String[] lineDate = line.split("\\|\\|");
            String imageNum = lineDate[0].trim();
            imageData.setImageNum(imageNum.substring(1, imageNum.length() - 1));
            imageData.setSize(lineDate[1]);
            imageData.setCode(lineDate[2]);
            imageData.setLastImageNum(false);
            map.put(imageData.getImageNum(), imageData);
            //判断上一个imageNum与当前的imageNum是否属于同一组，若不是则更新上一个isLastImageNum=true
            if (lastImageNum != null && !lastImageNum.split("\\.")[2].equals(imageData.getImageNum().split("\\.")[2])) {
                map.get(lastImageNum).setLastImageNum(true);
            }
            lastImageNum = imageData.getImageNum();
        }
        map.get(lastImageNum).setLastImageNum(true);
        return map;
    }

    /**
     * 校验图片数量是否规范
     *
     * @param folder
     * @param map
     * @param folderUrl
     * @throws IOException
     */
    private static void checkFileCount(File folder, Map<String, ImageData> map, String folderUrl) throws IOException {
        LOGGER.info("开始校验文件夹图片数量，必须为9的倍数");
        for (File imageFolder : folder.listFiles()) {
            if (imageFolder.isDirectory()) {
                String imageNum = imageFolder.getName();
                ImageData imageData = map.get(imageNum);
                if (imageData != null) {
                    int count = 0;
                    for (File file : imageFolder.listFiles()) {
                        if (file.getName().split("\\.")[1].equalsIgnoreCase("jpg")) {
                            count++;
                        }
                    }
                    if (count % 9 != 0) {
                        throw new IllegalArgumentException(imageNum + "图片数量必须为9的倍数");
                    }
                }
            }
        }
        LOGGER.info("完成文件夹图片数量校验");
    }

    private static void getFolder(String url) {
        File folder = new File(url);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    private static void outputImage(OutputStream imageOut, BufferedImage bufferedImage, String url) throws IOException {
        File file1 = new File(url);
        file1.createNewFile();
        imageOut = new FileOutputStream(file1, false);
        //创建图像文件编码工具类
        JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(imageOut);
        //使用图像编码工具类，输出缓存图像到目标文件
        en.encode(bufferedImage);
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

    private static void closeWriter(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制主图到汇总文件夹
     *
     * @param outFolderStr
     */
    private static void copyMainFolderImage(String outFolderStr) {
        //生成汇总文件夹
        String mainImgFolderCollect = outFolderStr + "/汇总主图";
        getFolder(mainImgFolderCollect);
        for (String mainImgFolder : mainImgFolderUrls) {
            File file = new File(mainImgFolder);
            //文件名称列表
            String[] filePath = file.list();
            for (int i = 0; i < filePath.length; i++) {
                File oldFile = new File(mainImgFolder + "/" + filePath[i]);
                File newFile = new File(mainImgFolderCollect + "/" + filePath[i]);
                FileInputStream in;
                FileOutputStream out;
                try {
                    in = new FileInputStream(oldFile);
                    out = new FileOutputStream(newFile);
                    byte[] buffer = new byte[2097152];
                    while ((in.read(buffer)) != -1) {
                        out.write(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //清空主图缓存
        mainImgFolderUrls = new ArrayList<>();
    }

    /**
     * 生成日志记录
     *
     * @param folderUrl
     * @param count
     * @param time
     * @param map
     */
    private static void recordLog(String folderUrl, Integer count, Float time, Map<String, ImageData> map) {
        //日期
        DateFormat df = new SimpleDateFormat("MM.dd");
        String date = "日期" + df.format(new Date());
        //编码列表
        Set<String> codeList = new HashSet<>();
        //文件夹数量
        Integer folderCount = 0;
        Set<String> typeList = new HashSet<>();

        for (Map.Entry<String, ImageData> entry : map.entrySet()) {
            String type = entry.getKey();
            if (type.split("\\.").length == 4) {
                type = (String) type.subSequence(0, type.lastIndexOf("."));
            }
            typeList.add(type);
            String code = entry.getValue().getCode();
            codeList.add(code.length() > 2 ? code.substring(2, code.length()) : code);
            folderCount++;
        }
        //款数
        Integer typeCount = typeList.size();
        String log = date + "，共" + typeCount + "款" + folderCount + "个文件夹" + count + "张图片，耗时:" + time + "s，编号为" + codeList.stream().collect(Collectors.joining("|"));
        OutputStream logOut = null;
        OutputStreamWriter osw = null;
        try {
            logOut = new FileOutputStream(folderUrl + "/log.txt", true);
            osw = new OutputStreamWriter(logOut, Charsets.UTF_8);
            osw.write(log + "\n");
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeWriter(osw);
            closeOut(logOut);
        }
        LOGGER.info("完成所有批量上码，{}", log);
    }
}
