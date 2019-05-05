package com.chenshinan.exercises.javaDiffUtils.mydiff;

import java.util.Arrays;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/5/5
 */
public class DiffMain {
    public static void main(String[] args) {
        String oldText = "A\nB\nC\nA\nB\nB\nA";
        String newText = "C\nB\nA\nB\nA\nC";
        List<String> oldList = Arrays.asList(oldText.split("\\n"));
        List<String> newList = Arrays.asList(newText.split("\\n"));
        MyersDiff<String> myersDiff = new MyersDiff<>();
        try {
            PathNode pathNode = myersDiff.buildPath(oldList, newList);
            System.out.println(pathNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
