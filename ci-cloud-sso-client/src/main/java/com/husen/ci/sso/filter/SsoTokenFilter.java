package com.husen.ci.sso.filter;

import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.sso.ClientConf;
import com.husen.ci.sso.SsoConstants;
import com.husen.ci.sso.helper.SsoLoginHelper;
import com.husen.ci.sso.response.SsoRsp;
import com.husen.ci.sso.session.SsoSession;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/***
 @Author:MrHuang
 @Date: 2019/7/9 14:19
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class SsoTokenFilter implements Filter {

    private String ssoServer;

    private String ssoExcluedPaths;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoServer = filterConfig.getInitParameter(ClientConf.SSO_SERVER);
        ssoExcluedPaths = filterConfig.getInitParameter(ClientConf.SSO_EXCLUDED_PATHS);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        String servletPath = req.getServletPath();
        if ("/favicon.ico".equals(servletPath)) {
            return;
        }
        System.out.println(servletPath);
        // TODO 判断是否是excluedPaths
        SsoSession ssoSession = SsoLoginHelper.loginCheck(req.getHeader(SsoConstants.RQ_HEADER_TOEKN_SESSION_ID));
        if (Objects.isNull(ssoSession)) {
            // 重新登陆
            rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            rsp.setContentType("application/json;charset=UTF-8");
            rsp.getWriter().println(JSONUtils.object2Json(GlobalApiResponse.toSuccess(new SsoRsp().setStatus(HttpServletResponse.SC_UNAUTHORIZED).setMsg("sso not login"))));
            return ;
        }
        request.setAttribute(ClientConf.SSO_SESSION, ssoSession);
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }
}
