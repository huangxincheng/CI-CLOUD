package com.husen.ci.sso.filter;

import com.husen.ci.framework.api.GlobalApiResponse;
import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.sso.ClientConf;
import com.husen.ci.sso.SsoConstants;
import com.husen.ci.sso.helper.SsoLoginHelper;
import com.husen.ci.sso.response.SsoRsp;
import com.husen.ci.sso.session.SsoSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private static final List<String> IGNORE_PATHS = Arrays.asList("/favicon.ico");

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
        if (IGNORE_PATHS.contains(servletPath)) {
            return;
        }
        System.out.println(servletPath);
        List<String> excludedPathList = this.excludedPathList(ssoExcluedPaths);
        if (!excludedPathList.contains(servletPath)) {
            SsoSession ssoSession = SsoLoginHelper.loginCheck(req.getHeader(SsoConstants.RQ_HEADER_TOEKN_SESSION_ID));
            if (Objects.isNull(ssoSession)) {
                String redirectUrl = req.getHeader(SsoConstants.RQ_HEADER_REDIRECT_URL);
                if (Objects.isNull(redirectUrl)) {
                    // 认证失败,下发协议
                    rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    rsp.setContentType("application/json;charset=UTF-8");
                    rsp.getWriter().println(JSONUtils.object2Json(GlobalApiResponse.toSuccess(new SsoRsp().setStatus(HttpServletResponse.SC_UNAUTHORIZED).setMsg("sso not login"))));
                    return ;
                } else {
                    // 认证失败，重定向Url
                    rsp.sendRedirect(redirectUrl);
                    return;
                }
            }
            request.setAttribute(ClientConf.ATTR_SSO_SESSION, ssoSession);
        }
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {

    }

    private List<String> excludedPathList(String paths) {
        if (StringUtils.isEmpty(paths)) {
            return new ArrayList<>();
        }
        return Arrays.asList(paths.split(","));
    }
}
