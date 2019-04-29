package com.chenshinan.exercises.javaDiffUtils;

import com.alibaba.fastjson.JSONObject;
import difflib.PatchFailedException;

import java.io.IOException;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        String newText = "Line 2\n" +
//                "Line 3 with changes\n" +
//                "Line 4\n" +
//                "Line 5 with changes and\n" +
//                "a new line\n" +
//                "Line 6\n" +
//                "new line 6.1\n" +
//                "Line 7\n" +
//                "Line 8\n" +
//                "Line 9\n" +
//                "Line 10 with changes";
//        String oldText = "Line 1\n" +
//                "Line 2\n" +
//                "Line 3\n" +
//                "Line 4\n" +
//                "Line 5\n" +
//                "Line 6\n" +
//                "Line 7\n" +
//                "Line 8\n" +
//                "Line 9\n" +
//                "Line 10";
        String oldText = "A\nB\nC\nA\nB\nB\nA";
        String newText = "C\nB\nA\nB\nA\nC";
        TextComparator comparator = new TextComparator(oldText, newText);
        ChangeDto changeDto = new ChangeDto();
        changeDto.setChangeData(comparator.getChangesFromOriginal());
        changeDto.setDeleteData(comparator.getDeletesFromOriginal());
        changeDto.setInsertData(comparator.getInsertsFromOriginal());
        String changeJson = JSONObject.toJSONString(changeDto);
        System.out.println(changeJson);
        ChangeDto changeDtox = JSONObject.parseObject(changeJson, ChangeDto.class);
        System.out.println(changeDtox);
        try {
            List<String> x = comparator.applyTo();
            System.out.println(x);
        } catch (PatchFailedException e) {
            e.printStackTrace();
        }
    }
}
