package com.husen.ci.controller;

import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.response.SsoLoginRsp;
import com.husen.ci.sso.helper.SsoLoginHelper;
import com.husen.ci.sso.response.SsoRsp;
import com.husen.ci.sso.session.SsoSession;
import com.husen.ci.user.client.IUserClientService;
import com.husen.ci.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 @Author:MrHuang
 @Date: 2019/7/9 10:25
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RestController
@RequestMapping("/sso")
public class SsoController {

    @Autowired
    private IUserClientService userClientService;

    @RequestMapping("/login")
    public GlobalApiResponse login(String userName, String password) {
        // 1.校验用户
        User user = userClientService.getUserByName(userName);
        if (user == null) {
            return GlobalApiResponse.toSuccess(new SsoLoginRsp().setStatus(-1).setMsg("用户不存在"));
        }
        // 2.生成TokenSessionId
        String tokenSessionId = SsoLoginHelper.createTokenSessionId(user.getUserId());
        // 3.存入TokenSessionId
        SsoLoginHelper.login(tokenSessionId);
        return GlobalApiResponse.toSuccess(new SsoLoginRsp().setTokenSessionId(tokenSessionId).setStatus(0).setMsg("登陆成功"));
    }

    @RequestMapping("/logout")
    public GlobalApiResponse logout(String tokenSessionId) {
        SsoLoginHelper.logout(tokenSessionId);
        return GlobalApiResponse.toSuccess(new SsoRsp().setStatus(0).setMsg("退出登陆成功"));
    }

    @RequestMapping("/logincheck")
    public GlobalApiResponse logincheck(String tokenSessionId) {
        SsoSession ssoSession = SsoLoginHelper.loginCheck(tokenSessionId);
        return GlobalApiResponse.toSuccess(ssoSession);
    }
}
