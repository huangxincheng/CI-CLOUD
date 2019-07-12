package com.husen.ci.framework.api;

import com.husen.ci.framework.json.JSONUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.Serializable;

import static com.husen.ci.framework.api.GlobalApiCode.SUCCESS_CODE;
import static com.husen.ci.framework.api.GlobalApiCode.SUCCESS_CODE_MSG;

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
     * 异常信息内容
     */
    private String throwableMessage;

    /**
     * 链路追踪消息ID
     */
    private String traceId;

    /**
     * 响应Suucess
     * @param payLoad
     * @param <T>
     * @return
     */
    public static <T> GlobalApiResponse toSuccess(T payLoad) {
        return toSuccess(SUCCESS_CODE, SUCCESS_CODE_MSG, payLoad);
    }

    /**
     * 响应Suucess
     * @param code
     * @param msg
     * @return
     */
    public static GlobalApiResponse toSuccess(int code, String msg) {
        return toSuccess(code, msg, null);
    }


    /**
     * 响应Suucess
     * @param code
     * @param msg
     * @return
     */
    public static <T> GlobalApiResponse toSuccess(int code, String msg, T payLoad) {
        GlobalApiResponse rsp = new GlobalApiResponse().setCode(code).setMsg(msg).setPayLoad(payLoad).setTraceId(MDC.get("X-B3-TraceId"));
        log.info("{\"logType\":{},\"rsp\":{}}", "Rsp Data Info", JSONUtils.object2Json(rsp));
        return rsp;
    }


    /**
     * 失败时返回
     *
     * @param code
     * @param errorMsg
     * @return
     */
    public static GlobalApiResponse toFail(int code, String errorMsg, Throwable throwable) {
        GlobalApiResponse rsp = new GlobalApiResponse().setCode(code).setMsg(errorMsg).setThrowableMessage(throwable.getMessage()).setTraceId(MDC.get("X-B3-TraceId"));
        log.error("{\"logType\":\"Rsp Data Info\",\"rsp\":"+JSONUtils.object2Json(rsp)+"}", throwable);
        return rsp;
    }

    /**
     * 判断是否请求成功
     * @return
     */
    public boolean ok() {
        return this.code == SUCCESS_CODE;
    }
}
