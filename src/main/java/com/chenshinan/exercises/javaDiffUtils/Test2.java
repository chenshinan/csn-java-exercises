package com.chenshinan.exercises.javaDiffUtils;

import com.alibaba.fastjson.JSONObject;
import difflib.PatchFailedException;
import difflib.myers.DifferentiationFailedException;
import difflib.myers.MyersDiff;
import difflib.myers.PathNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class Test2 {
    public static void main(String[] args) throws IOException, DifferentiationFailedException {
        String oldText = "美\n国\n人";
        String newText = "美\n国\n能\n力\n者";
        List<String> oldList = Arrays.asList(oldText.split("\\n"));
        List<String> newList = Arrays.asList(newText.split("\\n"));
        PathNode pathNode = new MyersDiff<String>().buildPath(oldList, newList);
        buildDiff(pathNode, oldList, newList);

    }

    public static void buildDiff(PathNode path, List<String> orig, List<String> rev) {
        List<String> result = new ArrayList<>();
        if (path == null)
            throw new IllegalArgumentException("path is null");
        if (orig == null)
            throw new IllegalArgumentException("original sequence is null");
        if (rev == null)
            throw new IllegalArgumentException("revised sequence is null");
        while (path != null && path.prev != null && path.prev.j >= 0) {
            if (path.isSnake()) {
                int endi = path.i;
                int begini = path.prev.i;
                for (int i = endi - 1; i >= begini; i--) {
                    result.add(" "+orig.get(i));
                }
            } else {
                int i = path.i;
                int j = path.j;
                int prei = path.prev.i;
                if (prei < i) {
                    result.add("【删除】" + orig.get(i - 1));
                } else {
                    result.add("【增加】" + rev.get(j - 1));
                }
            }
            path = path.prev;
        }
        Collections.reverse(result);
        for (String line : result) {
            System.out.println(line);
        }
    }
}
