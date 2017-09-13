package com.github.cc.gate.gateway.filter;

import com.github.cc.gate.common.util.ClientUtil;
import com.github.cc.gate.gateway.service.AuthService;
import com.github.cc.gate.gateway.service.GateService;
import com.github.cc.gate.gateway.vo.authority.PermissionInfo;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ZuulAccessFilter extends ZuulFilter {

    @Autowired
    GateService gateService;
    @Autowired
    AuthService authService;

    @Value("${zuul.prefix}")
    private String prefix;
    @Value("${gate.api.authHeader}")
    private String tokenHead;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //TODO: 2017/7/9  黑白名单、ip限制、前端有效用户
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestUri = request.getRequestURI();
        final String method = request.getMethod();
        log.info("IP：{}，访问资源：{}，请求方式：{}", ClientUtil.getClientIp(request),requestUri,method);
        requestUri = requestUri.substring(prefix.length()+1);
        final String  finalRequestUri = requestUri.substring(requestUri.indexOf("/"));
        //List<PermissionInfo> serviceInfo = gateService.getGateServiceInfo();
        String code = requestUri.substring(0,requestUri.indexOf("/"));
        List<PermissionInfo> serviceInfo = gateService.getGateServiceInfoByCode(code);
        // 判断资源是否启用权限约束
        Collection<PermissionInfo> result = Collections2.filter(serviceInfo, new Predicate<PermissionInfo>() {
            @Override
            public boolean apply(PermissionInfo permissionInfo) {
                String url = permissionInfo.getUri();
                String uri = url.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                String regEx = "^" + uri + "$";
                return (Pattern.compile(regEx).matcher(finalRequestUri).find() || finalRequestUri.startsWith(url + "/"))
                        && method.equals(permissionInfo.getMethod());
            }
        });
        if(result.size()>0){
            String token = request.getHeader(tokenHead);
            if(Strings.isNullOrEmpty(token)){
                setFailedRequest("Unauthorized",401);
            }else if(!authService.validate(token,finalRequestUri+":"+method)){
                setFailedRequest("Unauthorized",401);
            }
        }

        return null;
    }

    private void setFailedRequest(String body, int code) {
        log.warn("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
//            throw new RuntimeException("Code: " + code + ", " + body); //optional
        }
    }
}
