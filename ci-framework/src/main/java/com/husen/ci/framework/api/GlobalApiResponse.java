package com.husen.ci.framework.api;

import com.husen.ci.framework.json.JSONUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Optional;

import static com.husen.ci.framework.api.GlobalApiCode.*;

/***
 @Author:MrHuang
 @Date: 2019/6/13 15:03
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Slf4j
public class GlobalApiResponse<T> implements Serializable {

    /**
     * 业务响应Code
     */
    private int code;

    /**
     * 业务响应Msg与Code对应
     */
    private String msg;

    /**
     * 业务响应体payLoad
     */
    private T payLoad;

    /**
     * 链路追踪消息ID
     */
    private String traceId;

    /**
     * 成功时候返回
     *
     * @param payLoad
     * @param <T>
     * @return
     */
    public static <T> GlobalApiResponse<T> toSuccess(T payLoad) {
        log.info("The GlobalApiResponse toSuccess code = {} msg = {} payLoad = {}", SUCCESS_CODE, "SUCCESS", JSONUtils.object2Json(payLoad));
        return new GlobalApiResponse<T>()
                .setCode(SUCCESS_CODE)
                .setMsg("SUCCESS")
                .setPayLoad(payLoad)
                .setTraceId(MDC.get("X-B3-TraceId"));
    }

    /**
     * 失败时候返回
     * @return
     */
    public static GlobalApiResponse toFail(Exception e) {
        log.error("The GlobalApiResponse toFail code = "+UNKNOW_CODE+" msg = " + UNKONW_CODE_MSG, e);
        return new GlobalApiResponse()
                .setCode(UNKNOW_CODE)
                .setMsg(UNKONW_CODE_MSG)
                .setTraceId(MDC.get("X-B3-TraceId"));
    }

    /**
     * 失败时返回
     *
     * @param code
     * @param errorMsg
     * @return
     */
    public static GlobalApiResponse toFail(int code, String errorMsg) {
        log.error("The GlobalApiResponse toFail code = {} msg = {}", code, errorMsg);
        return new GlobalApiResponse().setCode(code).setMsg(errorMsg).setTraceId(MDC.get("X-B3-TraceId"));
    }


    /**
     * 失败时返回
     *
     * @param code
     * @param errorMsg
     * @return
     */
    public static GlobalApiResponse toFail(int code, String errorMsg, Throwable throwable) {
        log.error("The GlobalApiResponse toFail code = " + code + " msg = " + errorMsg, throwable);
        return new GlobalApiResponse().setCode(code).setMsg(errorMsg).setTraceId(MDC.get("X-B3-TraceId"));
    }
}
