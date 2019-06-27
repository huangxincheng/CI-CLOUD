package com.husen.ci.framework.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.husen.ci.framework.utils.IpUtils;

/***
 @Author:MrHuang
 @Date: 2019/6/17 22:43
 @DESC: TODO
 @VERSION: 1.0
 转换器
 Logback通过
 获取：<conversionRule conversionWord="IP" converterClass="com.husen.ci.framework.logback.IpConvent"/>

 使用： %IP
 ***/
public class IpConvent extends ClassicConverter {

    /**
     * 获取服务端IP
     *
     * @param event
     * @return
     */
    @Override
    public String convert(ILoggingEvent event) {
        return IpUtils.getInstance().getServerIP();
    }
}
