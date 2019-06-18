package com.chenshinan.exercises.doc2md.poi1;

/**
 * Created by JackWang on 2015/5/10.
 * 自定义异常类
 */
public class POIException extends Exception {
    public POIException() {

    }
    public POIException(String message) {
        super(message);
    }
}
