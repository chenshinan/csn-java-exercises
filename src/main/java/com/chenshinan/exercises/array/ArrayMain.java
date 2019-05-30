package com.chenshinan.exercises.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/5/30
 */
public class ArrayMain {
    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("agile");
        test(list);
        System.out.println(list);

    }

    public static void test(List<String> list){
        list = new ArrayList<>();
        list.add("xxx");
    }
}
