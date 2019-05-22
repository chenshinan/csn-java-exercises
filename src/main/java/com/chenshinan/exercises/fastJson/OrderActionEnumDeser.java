package com.chenshinan.exercises.fastJson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import difflib.Chunk;

import java.lang.reflect.Type;

public class OrderActionEnumDeser implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Chunk chunk = parser.parseObject(Chunk.class);
        return (T) chunk;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

}