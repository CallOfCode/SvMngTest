package com.github.cc.gate.gateway.controller.api;

import com.github.cc.gate.gateway.secruity.ApiAuthenticationRequest;
import com.github.cc.gate.gateway.secruity.ApiAuthenticationResponse;
import com.github.cc.gate.gateway.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/authen")
public class AuthController {
    @Autowired
    AuthService authService;

    @ApiOperation(value="获取有效时长的token", notes="传入网关中心发布的客户端clientId和secret")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody ApiAuthenticationRequest authenticationRequest){
        final String token = authService.login(authenticationRequest.getClientId(),authenticationRequest.getSecret());
        return ResponseEntity.ok(new ApiAuthenticationResponse(token));
    }

    @ApiOperation(value="刷新并获取新的token", notes="传入旧的token")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(String token){
        String refreshToken = authService.refresh(token);
        if(refreshToken==null){
            return ResponseEntity.badRequest().body(null);
        }else{
            return ResponseEntity.ok(new ApiAuthenticationResponse(token));
        }
    }

    @ApiOperation(value="验证token是否有效", notes="传入申请的token")
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public ResponseEntity<?> verify(String token,String resource){
        if(authService.validate(token,resource)){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.status(401).body(false);
        }
    }

}
