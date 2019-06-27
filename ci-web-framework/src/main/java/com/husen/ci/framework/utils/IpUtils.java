package com.husen.ci.framework.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 获取客户端IP
     * @return
     */
    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
