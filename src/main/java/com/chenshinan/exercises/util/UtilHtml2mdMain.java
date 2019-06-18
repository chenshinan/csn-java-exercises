package com.chenshinan.exercises.util;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author shinan.chen
 * @since 2019/6/4
 */
public class UtilHtml2mdMain {
    @Test
    public void testMultiValueMap() {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key", "value1");
        map.add("key", "value2");
        assertEquals(1, map.size());
        List<String> expected = new ArrayList<String>(2);
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, map.get("key"));
    }
}
