package com.chenshinan.exercises.fastJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import difflib.Chunk;
import difflib.Delta;

import java.io.IOException;

/**
 * @author shinan.chen
 * @since 2019/4/25
 */
public class Test {
    public static void main(String[] args) throws IOException {
//        ParserConfig.getGlobalInstance().putDeserializer(Chunk.class, new OrderActionEnumDeser());
        String xx = "{\"changeData\":[],\"deleteData\":[],\"insertData\":[{\"original\":{\"lines\":[],\"position\":1},\"revised\":{\"lines\":[\"\",\"碰巧有风吹散他的头发\"],\"position\":1},\"type\":\"INSERT\"}]}";
//        Delta<String> x = JSON.parseObject("{\"original\":{\"lines\":[],\"position\":1},\"revised\":{\"lines\":[\"\",\"碰巧有风吹散他的头发\"],\"position\":1},\"type\":\"INSERT\"}", new TypeReference<Delta<String>>() {});
        TextDiffDTO xxa = JSON.parseObject(xx, TextDiffDTO.class);
//        System.out.println(x);
        System.out.println(xx);
        System.out.println(xxa);
        JSONObject xa = JSON.parseObject(xx);

        TextDiffDTO xxaa = TextDiffDTO.jsonToDTO(JSON.parseObject(xx));
        System.out.println(xxaa);
    }
}
