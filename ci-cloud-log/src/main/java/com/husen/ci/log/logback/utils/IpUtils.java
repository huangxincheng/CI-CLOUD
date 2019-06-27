package com.husen.ci.log.logback.utils;

import java.net.InetAddress;

/***
 @Author:MrHuang
 @Date: 2019/6/6 14:27
 @DESC: TODO
 @VERSION: 1.0
 ***/
public final class IpUtils {

    private IpUtils(){}

    private static final IpUtils INSTANCE = new IpUtils();

    public static IpUtils getInstance() {
        return INSTANCE;
    }
    /**
     * 获取服务器IP
     * @return
     */
    public String getServerIP() {
        String hostAddress = "unKnown";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (null == localHost.getHostAddress()) {
                hostAddress = "undefine";
            } else {
                hostAddress = localHost.getHostAddress();
            }
        } catch (Exception ignored) {}
        return hostAddress;
    }
}
