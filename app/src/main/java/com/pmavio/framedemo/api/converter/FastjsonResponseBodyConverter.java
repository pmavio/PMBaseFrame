package com.strongit.framedemo.api.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.pmavio.pmbaseframe.test.BuildConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * WQ on 2016/1/11
 * wendell.std@gmail.com
 */
public class FastjsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    FastjsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String result = new String(value.bytes(), "UTF-8");
            if (BuildConfig.DEBUG){
                System.out.println(result);
            }
            return JSON.parseObject(result, type);
        } catch (UnsupportedEncodingException exception) {
            throw new JSONException("parseObject error", exception);
        }
    }
}

