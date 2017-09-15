package com.github.cc.gate.gateway.filter;


import com.github.cc.gate.common.util.ClientUtil;
import com.github.cc.gate.gateway.secruity.SecurityUser;
import com.github.cc.gate.gateway.service.LogService;
import com.github.cc.gate.gateway.service.UserPermissionService;
import com.github.cc.gate.gateway.service.UserService;
import com.github.cc.gate.gateway.util.DBLog;
import com.github.cc.gate.gateway.vo.authority.PermissionInfo;
import com.github.cc.gate.gateway.vo.log.LogInfo;
import com.github.cc.gate.gateway.vo.user.UserInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@WebFilter(filterName="resourceAccessFilter",urlPatterns="/*")
@Slf4j
public class ResourceAccessFilter implements Filter {

    @Value("${gate.ignore.startWith}")
    private String startWith;
    @Value("${gate.ignore.contain}")
    private String contain;
    @Autowired
    UserPermissionService permissionService;
    @Autowired
    UserService userService;
    @Autowired
    LogService logService;

    private static final String ROOT_URI = "/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String requestUri = request.getRequestURI();
        // 不进行拦截的地址
        if (ROOT_URI.equals(requestUri)||isStartWith(requestUri) || isContains(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }else if(SecurityContextHolder.getContext().getAuthentication()!= null&&!(SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof String)){
            final String method = request.getMethod();
            try{
                SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
                String username = userDetails.getUsername();
                request.getSession().setAttribute("user",userDetails);
                if (!checkAllow(requestUri, method, request, username)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized");
                    return ;
                }
            }catch(Exception e){
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否包含某种特征
     * @param requestUri
     * @return
     */
    private boolean isContains(String requestUri) {
        boolean flag = false;
        for (String s : contain.split(",")) {
            if (requestUri.contains(s))
                return true;
        }
        return flag;
    }

    /**
     * URI是否以什么打头
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s))
                return true;
        }
        return flag;
    }

    /**
     * 权限校验
     * @param requestUri
     * @param method
     */
    private boolean checkAllow(final String requestUri, final String method , HttpServletRequest request, String username) {
        log.debug("uri：" + requestUri + "----method：" + method);
        List<PermissionInfo> permissionInfos = getPermissionInfos(request, username) ;
        Collection<PermissionInfo> result =
                Collections2.filter(permissionInfos, new Predicate<PermissionInfo>() {
                    @Override
                    public boolean apply(PermissionInfo permissionInfo) {
                        String url = permissionInfo.getUri();
                        String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                        String regEx = "^" + uri + "$";

                        return (Pattern.compile(regEx).matcher(requestUri).find() || requestUri.startsWith(url + "/"))
                                && method.equals(permissionInfo.getMethod());

                    }
                });
        if (result.size() <= 0) {
            return false;
        } else{
            PermissionInfo[] pms =  result.toArray(new PermissionInfo[]{});
            PermissionInfo pm = pms[0];
            if(!method.equals("GET")){
                setCurrentUserInfoAndLog(request, username, pm);
            }
            return true;
        }
    }

    /**
     * 读取权限
     * @param request
     * @param username
     * @return
     */
    private List<PermissionInfo> getPermissionInfos(HttpServletRequest request, String username) {
        List<PermissionInfo> permissionInfos;
        if (request.getSession().getAttribute("permission") == null) {
            permissionInfos = permissionService.getPermissionByUsername(username);
            request.getSession().setAttribute("permission", permissionInfos);
        } else {
            permissionInfos = (List<PermissionInfo>) request.getSession().getAttribute("permission");
        }
        return permissionInfos;
    }

    private void setCurrentUserInfoAndLog(HttpServletRequest request, String username, PermissionInfo pm) {
        UserInfo info = userService.getUserByUsername(username);
        String host =  ClientUtil.getClientIp(request);
        request.setAttribute("userId", info.getId());
        try {
            request.setAttribute("userName", URLEncoder.encode(info.getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            request.setAttribute("userName", info.getName());
            e.printStackTrace();
        }
        request.setAttribute("userHost", ClientUtil.getClientIp(request));
        LogInfo logInfo = new LogInfo(pm.getMenu(),pm.getName(),pm.getUri(),new Date(),info.getId(),info.getName(),host);
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }

}
