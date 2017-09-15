# 1.简介
网关管理系统 是一个基于spring cloud的用户资源授权、api管理授权的网关系统，以jwt交互的鉴权token来实施，支持基于Eureka注册中心下的服务鉴权和拦截，同时扩展Eureka下服务失效的通知扩展。
# 2.功能列表
- 用户管理
- 角色管理
- 菜单、动作管理
- 操作日志
- 系统监控
- 客户端注册
- 服务管理和授权
- 网关黑白名单和IP限制（待完善）
- 服务状态监控与提醒（待完善）
- 服务追踪（待完善）

# 3.系统定位
网关管理系统 主要是给服务之间做JWT鉴权和对外api的鉴权。

# 4.鉴权原则
- 需要被保护的服务才需要进行注册。


![img](images/frame.png)

# 5.外部服务访问鉴权配置
## 5.1.内部服务配置
### 5.1.1 maven配置
```
<dependency>
    <groupId>com.github.cc</groupId>
    <artifactId>gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 5.1.2 注解配置
```
@ApiGateSecurity
```
### 5.1.3 拦截器配置
```
@Configuration
public class ApiWebAppConfig extends WebMvcConfigurerAdapter {
    @Value("${gate.client.authHost}")
    private String authHost;
    @Value("${gate.client.authHeader}")
    private String authHeader;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(authHost,authHeader)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
```
### 5.1.4 属性配置
```
gate:
    client:
        authHeader: access-token
        authHost: http://localhost:8762/api/authen
```
## 5.2.外部服务client和内部服务service注册及配置
网关管理-服务注册，注册服务，权限编码处填写服务的ID
网关管理-客户端注册，注册客户端，并分配服务权限


# 6.内部服务访问鉴权
## 6.1 服务端配置
参照 5.1 内部服务配置
## 6.2 客户端配置
### 6.2.1 maven配置
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
<dependency>
    <groupId>com.github.cc</groupId>
    <artifactId>gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 6.2.2 Feign拦截器设置
```
@Configuration
public class FeignApiConfig {
    @Value("${gate.client.clientId}")
    private String clientId;
    @Value("${gate.client.secret}")
    private String secret;
    @Value("${gate.client.authHeader}")
    private String authHeader;
    @Value("${gate.client.authHost}")
    private String authHost;

    @Bean
    public FeignInterceptor getFeignInterceptor(){
        return new FeignInterceptor(clientId,secret,authHeader,authHost);
    }
}
```
### 6.2.3 服务端与客户端注册
网关管理-服务注册，注册服务，权限编码处填写服务端的ServiceId
网关管理-客户端注册，注册客户端，并分配服务权限，填写客户端ServiceId

### 6.2.3 属性配置
clientId和secret需要提前在客户端中注册
```
gate:
    client:
        authHeader: access-token
        authHost: http://localhost:8762/api/authen
        clientId: gate-demo-person
        secret: 123456

feign:
  hystrix:
      enabled: true
  compression:
    request:
      enabled: true
      mime-types: text/xml,application/xml,application/json
      min-request-size: 2048
    response:
      enabled: true

hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 5000
```

# 7.服务调用测试
# 7.1 获取token
```
#URL: http://localhost:8762/api/authen/auth
#METHOD: post
#BODY:
{
  "clientId": "demoClient",
  "secret": "123456"
}
```
# 7.2 访问服务
```
#HEADER: access-token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vQ2xpZW50IiwiY3JlYXRlZCI6MTUwNTI5MzkwNzI4OCwiZXhwIjoxNTA1MzAxMTA3fQ.vfo8ZQeszYomMjenlMze0GqqBuM9MHQRZ0ep_JNVSjQCQ3su1r_Dfly_-hlvF5sSUicb8jM-wdW5P1VtTA86Gw
```