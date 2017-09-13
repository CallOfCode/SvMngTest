package com.github.cc.gate.gateway.interceptor;

import com.github.cc.gate.gateway.annotation.ApiGateSecurity;
import com.github.cc.gate.gateway.exception.AuthenticationServerErrorException;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

public class ApiInterceptor extends HandlerInterceptorAdapter {
    private String authHost;
    private String authHeader;

    public ApiInterceptor(String authHost, String authHeader) {
        this.authHost = authHost;
        this.authHeader = authHeader;
    }

    public ApiInterceptor(String authHost) {
        this.authHost = authHost;
        this.authHeader = "access-token";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof Controller)
            return super.preHandle(request, response, handler);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ApiGateSecurity methodAnnotation = handlerMethod.getBeanType().getAnnotation(ApiGateSecurity.class);
        if(methodAnnotation==null)
            methodAnnotation = handlerMethod.getMethodAnnotation(ApiGateSecurity.class);

        if(methodAnnotation!=null){
            String token = request.getHeader(authHeader);
            int statusCode = getStatusCode(token,request.getRequestURI()+":"+request.getMethod()).get();
            if(statusCode==200){
                return super.preHandle(request, response, handler);
            }else if(statusCode==401){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
                return false;
            }else{
                throw new AuthenticationServerErrorException("Authentication ERROR！");
            }
        }
        return super.preHandle(request, response, handler);
    }

    private CompletableFuture<Integer> getStatusCode(String token,String resource){
        return CompletableFuture.supplyAsync(()->{
            HttpResponse response = HttpRequest.get(authHost + "/verify").query("token",token).query("resource",resource).send();
            int code = response.statusCode();
            return code;
        });
    }
}
