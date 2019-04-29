package com.chenshinan.exercises.javaDiffUtils;

import difflib.Chunk;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class RecoverUtil {
    static String parseText(String oldText, ChangeDto changeDto){
        /**
         * 先插入后删除再修改
         */
        List<Chunk> insertData = changeDto.getChangeData();
        List<Chunk> deleteData = changeDto.getDeleteData();
        List<Chunk> changeData = changeDto.getChangeData();
        return null;
    }
}
