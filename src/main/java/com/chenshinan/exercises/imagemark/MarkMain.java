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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shinan.chen
 * @since 2019/6/22
 */
public class MarkMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(MarkMain.class);
    private static final String FONT_NAME = "宋体";
    private static final int FONT_STYLE = Font.BOLD;
    private static final int FONT_SIZE = 200;
    private static final Color SIZE_FONT_COLOR = Color.decode("#000000");
    private static final Color CODE_FONT_COLOR = Color.decode("#28E613");
    private static final float ALPHA = 1;

    public static void main(String[] args) {
        String folderUrl = "/Users/chenshinan/Downloads/watermark";
        File folder = new File(folderUrl);
        long startTime = System.currentTimeMillis();
        OutputStream os = null;
        int count = 0;
        try {
            String data = IOUtils.toString(new FileInputStream(folderUrl + "/data.txt"), Charsets.UTF_8);
            Map<String, ImageData> map = handleData(data);
            LOGGER.info("开始批量上码");
            String outStr = folderUrl + "/out/";
            File out = new File(outStr);
            if(!out.exists()){
                out.mkdir();
            }
            for (File imageFolder : folder.listFiles()) {
                if (imageFolder.isDirectory()) {
                    String imageNum = imageFolder.getName();
                    ImageData imageData = map.get(imageNum);
                    if (imageData != null) {
                        for (File file : imageFolder.listFiles()) {
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
                                //绘制size
                                printSize(g, height, width, imageData.getSize());
                                //绘制code
                                printCode(g, height, width, imageData.getCode());
                                //释放工具
                                g.dispose();
                                //创建文件输出流，指向最终的目标文件
                                String outFolderStr = folderUrl + "/out/" + imageNum;
                                File outFolder = new File(outFolderStr);
                                if(!outFolder.exists()){
                                    outFolder.mkdir();
                                }
                                String url = outFolderStr + "/" + fildName;
                                File file1 = new File(url);
                                file1.createNewFile();
                                os = new FileOutputStream(file1,false);
                                //创建图像文件编码工具类
                                JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
                                //使用图像编码工具类，输出缓存图像到目标文件
                                en.encode(bufferedImage1);
                                count++;
                            }
                        }
                        LOGGER.info("完成【{}】批量上码", imageNum);
                    }else{
                        LOGGER.info("没找到【{}】的数据，跳过", imageNum);
                    }
                }

            }
            LOGGER.info("完成所有批量上码，共计：{}张图片，耗时：{}s", count, (float) (System.currentTimeMillis() - startTime) / 1000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();    //关闭流
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
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
    private static void printSize(Graphics2D g, int height, int width, String markText) {
        //使用绘图工具对象将水印（文字/图片）绘制到缓存图片
        g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
        g.setColor(SIZE_FONT_COLOR);

        //获取文字水印的宽度和高度值
        //文字水印宽度
        int sizeWidth = FONT_SIZE * getTextLength(markText);
        //文字水印高度
        int sizeHeight = FONT_SIZE;
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
    private static void printCode(Graphics2D g, int height, int width, String markText) {
        g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
        g.setColor(CODE_FONT_COLOR);
        //获取文字水印的宽度和高度值
        //文字水印宽度
        int codeWidth = FONT_SIZE * getTextLength(markText);
        //文字水印高度
        int codeHeight = FONT_SIZE;
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
        Map<String, ImageData> map = new HashMap<>();
        String[] lines = data.split("\\|\\|\\|");
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
            map.put(imageData.getImageNum(), imageData);
        }
        return map;
    }


}
