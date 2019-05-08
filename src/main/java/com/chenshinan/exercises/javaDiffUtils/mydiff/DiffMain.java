package com.chenshinan.exercises.javaDiffUtils.mydiff;

import java.util.Arrays;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/5/5
 */
public class DiffMain {
    public static void main(String[] args) {
        String oldText = "A\nB\nC\nA\nB\nB\nA\nX";
        String newText = "C\nB\nA\nB\nA\nC\nX1";
        List<String> oldList = Arrays.asList(oldText.split("\\n"));
        List<String> newList = Arrays.asList(newText.split("\\n"));
        MyersDiff<String> myersDiff = new MyersDiff<>();
        try {
            PathNode pathNode = myersDiff.buildPath(oldList, newList);
            System.out.println(pathNode);
            myersDiff.buildDiff(pathNode, oldList, newList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
