# 简介
Sv-Mng 是一个基于spring cloud的用户资源授权、api管理授权的网关系统，以jwt交互的鉴权token来实施，支持基于Eureka注册中心下的服务鉴权和拦截，同时扩展Eureka下服务失效的通知扩展。
# 功能列表
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

# 用户指南
Sv-Mng 主要是给服务之间做JWT鉴权和对外api的鉴权。

# 原则
- 需要被保护的服务才需要进行注册。

# 内部服务之间的鉴权
- 注册保护服务的地址和访问方式
- 访问客户端的授权（申请clientId和secret，同时授权可访问服务）

## 内部鉴权建议助手
**具体例子可以参考ace-gate-demo**
## 服务提供端如何开启权限拦截
### maven依赖，如果没有自行打包
```
<dependency>
    <groupId>com.github.wxiaoqi</groupId>
    <artifactId>ace-gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### 配置引入
```
gate:
  client:
    # 地址为ace-gate-way所在ip端口
    authHost: http://localhost:8765/api/authen
    # 鉴权头部标志，与客户端一致
    authHeader: access-token
```
### 配置启用
```
/**
 * Created by ace on 2017/7/6.
 */
@Configuration
public class ApiWebAppConfig extends WebMvcConfigurerAdapter {
    @Value("${gate.client.authHost}")
    private String authHost;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor(authHost)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}

```
### 服务拦截启用，rest方法加上`@ApiGateSecurity`即可
```
@ApiGateSecurity
@RequestMapping(value = "/user/un/{username}/system", method = RequestMethod.GET)
@ResponseBody
public String getSystemsByUsername(@PathVariable("username") String username){
    int userId = userBiz.getUserByUsername(username).getId();
    return JSONObject.toJSONString(menuBiz.getUserAuthoritySystemByUserId(userId));
}
```

## Feign客户端如何自动加上鉴权信息
### maven依赖，如果没有自行打包
```
<dependency>
    <groupId>com.github.wxiaoqi</groupId>
    <artifactId>ace-gate-agent</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 配置引入
```
gate:
  client:
  # 页面注册的客户信息
    clientId: admin-ui
    secret: test
    # 与服务端authHeader保持一致
    authHeader: access-token
    authHost: http://localhost:8765/api/authen
    # 与ace-gate-way gate.api.tokenHead保持一致
    tokenHead: ApiGateToken
```

### 配置启用
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
    @Value("${gate.client.tokenHead}")
    private String tokenHead;

    @Bean
    public FeignInterceptor authenticationInterceptor() {
        return new FeignInterceptor(clientId, secret, authHeader, authHost, tokenHead);
    }

}
```


# 对外api鉴权
- 注册保护服务的地址和访问方式
- 访问客户端的授权（申请clientId和secret，同时授权可访问服务）

## 外部客户端访问token方式
- 凭借客户端和密钥申请有效token
![img](http://ofsc32t59.bkt.clouddn.com/17-07-14/1499988134104.jpg)
#### 请求json方式
```
{
"clientId":"gate-demo-client",
"secret":"test"
}
```
#### 请求地址，ace-gate-way所释放的地址
```
http://[ace-gate-way部署服务器]:[配置端口]/api/authen/auth
```
- 凭借有效的token二次访问服务

![img](http://ofsc32t59.bkt.clouddn.com/17-07-14/1499988191461.jpg)

#### 请求头
以下配置信息来自ace-gate-way的application.yml
![img](http://ofsc32t59.bkt.clouddn.com/17-07-14/1499988024769.jpg)
```
[${gate.api.header}]:[${gate.api.tokenHead}][空格][神奇的token]
如默认格式：
access-token:ApiGateToken eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnYXRlLWRlbW8tY2xpZW50IiwiY3JlYXRlZCI6MTQ5OTk0NzAzNzE1NCwiZXhwIjoxNDk5OTU0MjM3fQ.eL3Ucd2Oh166PDcmHLsKK2A0uJZ6QPxLqRac6enQacBEgQwc2I0qJtkui1V0WjB70VWHpRbHgmzps_dM9jKg0A
```
## 外部访问IP黑白名单方式
带实现

# 欢迎交流
![img](http://ofsc32t59.bkt.clouddn.com/17-06-16/1497595760484.jpg)