package com.chenshinan.exercises.imagemark;

/**
 * @author shinan.chen
 * @since 2019/6/22
 */
public class ImageData {
    private String imageNum;
    private String size;
    private String code;
    private String description;
    /**
     * 是否是同款的最后图编码，如果是最后图编码，则汇总之前的主图
     */
    private Boolean isLastImageNum;

    public Boolean getLastImageNum() {
        return isLastImageNum;
    }

    public void setLastImageNum(Boolean lastImageNum) {
        isLastImageNum = lastImageNum;
    }

    public String getImageNum() {
        return imageNum;
    }

    public void setImageNum(String imageNum) {
        this.imageNum = imageNum;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
