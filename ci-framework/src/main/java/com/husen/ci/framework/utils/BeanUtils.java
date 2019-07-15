package com.husen.ci.framework.utils;

import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.framework.net.bean.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/6/5 22:05
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class BeanUtils {

    public static <T, F> F copy(T t, F f) {
        if (t == null) {
            return null;
        }
        org.springframework.beans.BeanUtils.copyProperties(t, f);
        return f;
    }

    public static <T,F> F copy(T t, Class<F> clazz) {
        F f = null;
        try {
            f = clazz.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        org.springframework.beans.BeanUtils.copyProperties(t, f);
        return f;
    }

    public static Map<String, Object> bean2Map(Object bean) {
        return BeanMap.create(bean);
    }


    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        return JSONUtils.json2Bean(JSONUtils.object2Json(map), clazz);
    }

    public static void main(String[] args) {
        HttpResult result = new HttpResult().setCode(200);
//        Map<String, Object> a = new HashMap<>();
//        a.put("code", 2000);
//        HttpResult result = map2Bean(a, HttpResult.class);
//        System.out.println(result);
//        Map map = bean2Map(result);
//        System.out.println(map);
        HttpResult copy = BeanUtils.copy(result, HttpResult.class);
        System.out.println(copy);
    }
}

