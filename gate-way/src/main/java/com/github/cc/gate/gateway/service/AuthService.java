package com.github.cc.gate.gateway.service;

import com.github.cc.gate.gateway.constant.UserConstant;
import com.github.cc.gate.gateway.vo.authority.PermissionInfo;
import com.github.cc.gate.gateway.vo.gate.ClientInfo;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AuthService {
    @Autowired
    private GateClientService gateClientService;
    @Autowired
    private TokenService tokenService;

//    @Value("${gate.api.tokenHead}")
//    private String tokenHead;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT);

    public String login(String clientId,String secret){
        ClientInfo info = gateClientService.getGateClientInfo(clientId);
        String token = "";
        if(encoder.matches(secret,info.getSecret())){
            token = tokenService.generateToken(info);
        }
        return token;
    }

    public String refresh(String token){
//        final String token = oldToken.substring(tokenHead.length());//TODO why?
        String clientId = tokenService.getClientIdFromToken(token);
        ClientInfo info = gateClientService.getGateClientInfo(clientId);
        if (tokenService.canTokenBeRefreshed(token,info.getUpdTime())){
            return tokenService.refreshToken(token);
        }
        return null;
    }

    public Boolean validate(String token,String resource) {
//        if(!oldToken.startsWith(tokenHead))
//            return false;
//        final String token = oldToken.substring(tokenHead.length());
        String clientId = tokenService.getClientIdFromToken(token);
        ClientInfo info = gateClientService.getGateClientInfo(clientId);
        return info.getCode().equals(clientId)&&!tokenService.isTokenExpired(token)&&validateResource(clientId,resource);
    }

    public Boolean validateResource(String clientId, String resource){
        String [] res = resource.split(":");
        final String requestUri = res[0];
        final String method = res[1];
        List<PermissionInfo> clientPermissionInfo = gateClientService.getGateServiceInfo(clientId);
        Collection<PermissionInfo> result = Collections2.filter(clientPermissionInfo, new Predicate<PermissionInfo>() {
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
        }
        return true;
    }

}
