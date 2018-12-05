package com.chenshinan.exercises.recycle;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试类实例的地址引用
 *
 * @author shinan.chen
 * @since 2018/12/3
 */
public class RecycleTest {
    public static void main(String[] args) {
        RecycleTest test = new RecycleTest();
        Map<String, RecycleTest> map = new HashMap<>();
        map.put("test", test);
//        test = null;
//        System.out.println(map.get("test"));
        RecycleTest get = map.get("test");
        get = null;
        System.out.println(map.get("test"));
        map.put("test", null);
        System.out.println(map.get("test"));
    }
}
