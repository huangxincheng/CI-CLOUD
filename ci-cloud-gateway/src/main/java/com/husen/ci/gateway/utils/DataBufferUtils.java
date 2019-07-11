package com.husen.ci.gateway.utils;

import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import io.netty.buffer.ByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.nio.charset.StandardCharsets;

/***
 @Author:MrHuang
 @Date: 2019/7/11 18:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class DataBufferUtils {

    /**
     * 将result转换成DataBuffer
     * @param response
     * @param result
     * @return
     */
    public static DataBuffer getDataBuffer(ServerHttpResponse response, GlobalApiResponse result) {
        return response.bufferFactory().wrap(JSONUtils.object2Bytes(result));
    }
    /**
     * 将result转换成DataBuffer
     * @param result
     * @return
     */
    public static DataBuffer getDataBuffer(Object result) {
        byte[] bytes = JSONUtils.object2Bytes(result);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    /**
     * 将result转换成DataBuffer
     * @param result
     * @return
     */
    public static DataBuffer getDataBuffer(String result) {
        byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }
}
