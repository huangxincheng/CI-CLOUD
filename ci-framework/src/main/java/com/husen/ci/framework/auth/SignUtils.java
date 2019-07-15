package com.husen.ci.framework.auth;

import com.husen.ci.framework.utils.CryptoUtils;
import org.apache.commons.codec.binary.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: huangxincheng
 * <p>
 * <p>
 *    签名Utils
 **/
public class SignUtils {

    private static final String API_KEY = "API_KEY";

    public static String sign(Map<String, Serializable> keyValueMap, String apikey) {
        List<String> keyList  = new ArrayList<>(keyValueMap.keySet());
        Collections.sort(keyList);
        StringBuilder sb = new StringBuilder();
        for (String key : keyList) {
            if (keyValueMap.get(key) != null) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(keyValueMap.get(key));
            }
        }
        sb.append("&" + API_KEY + "=").append(apikey);
        return CryptoUtils.encodeMD5(sb.toString()).toUpperCase();
    }


    public static boolean checkSign(Map<String, Serializable> keyValueMap, String apikey, String requestSign) {
        String sign = sign(keyValueMap, apikey);
        return StringUtils.equals(sign, requestSign);
    }


    public static void main(String[] args) {
        HashMap<String, Serializable> kv = new HashMap<>();
        kv.put("k1", "v1");
        kv.put("k2", "v2");
        kv.put("k3", "v3");
        kv.put("k4", "v4");
        kv.put("k5", "v5");
        String sign = sign(kv, "apikey");
        System.out.println(sign);

        kv.put("k1", "v1");
        System.out.println(checkSign(kv, "apikey", sign));
    }





}