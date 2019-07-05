package com.husen.ci.user.client;

import com.husen.ci.framework.common.ProjectCommon;
import org.springframework.cloud.openfeign.FeignClient;

/***
 @Author:MrHuang
 @Date: 2019/7/5 11:13
 @DESC: TODO
 @VERSION: 1.0
 ***/
@FeignClient(name = ProjectCommon.USER_SERVICE, fallbackFactory = UserClientFallbackFactory.class)
public interface IUserClient extends IUserClientService {

}
