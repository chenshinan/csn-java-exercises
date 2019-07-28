package com.chenshinan.exercises.array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shinan.chen
 * @since 2019/5/30
 */
public class ArrayMain {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("agile");
        test(list);
        System.out.println(list);
        Set<String> sets = new HashSet<>();
        sets.add("1");
        sets.add("1");
        System.out.println(sets);

        String type = "7.11.1.1";
        if (type.split("\\.").length == 4) {
            type = (String) type.subSequence(0, type.lastIndexOf("."));
        }
        System.out.println(type);
    }

    public static void test(List<String> list) {
        list = new ArrayList<>();
        list.add("xxx");
    }
}
