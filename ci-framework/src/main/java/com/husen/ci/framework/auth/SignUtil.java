package com.husen.ci.framework.auth;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Author: huangxincheng
 * <p>
 * <p>
 *     签名Util
 **/
public class SignUtil {

    public static String getSign(Map<String, Serializable> keyValueMap, String apikey) {
        List<String> keyList  = new ArrayList<>(keyValueMap.keySet());
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        for (String key : keyList) {
            if (keyValueMap.get(key) != null) {
                if (sb.length() >= 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(keyValueMap.get(key));
            }
        }
        sb.append("&apikey=").append(apikey);
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
    }


    public static boolean checkSign(Map<String, Serializable> keyValueMap, String apikey, String requestSign) {
        String sign = getSign(keyValueMap, apikey);
        return StringUtils.equals(sign, requestSign);
    }


    public static void main(String[] args) {
        HashMap<String, Serializable> kv = new HashMap<>();
        kv.put("k1", "v1");
        kv.put("k2", "v2");
        kv.put("k3", "v3");
        kv.put("k4", "v4");
        kv.put("k5", "v5");
        String sign = getSign(kv, "apikey");
        System.out.println(sign);

        kv.put("k1", "v1");
        System.out.println(checkSign(kv, "apikey", sign));
    }





}