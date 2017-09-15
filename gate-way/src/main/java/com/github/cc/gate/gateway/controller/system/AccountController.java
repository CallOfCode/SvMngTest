package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.message.ObjectRestResponse;
import com.github.cc.gate.gateway.secruity.SecurityUser;
import com.github.cc.gate.gateway.service.UserService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!= null)
            new SecurityContextLogoutHandler().logout(request,response,auth);

        return new ObjectRestResponse<>().msg("logout").rel(false);
    }

    @RequestMapping(value = "/unlock",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse unLock(HttpServletRequest request, HttpServletResponse response, @RequestParam String username,@RequestParam String password){

        if(Strings.isNullOrEmpty(username)||Strings.isNullOrEmpty(password))
            return new ObjectRestResponse<>().msg("用户名或密码不正确").rel(false);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(new BCryptPasswordEncoder().matches(password,userDetails.getPassword())){
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());

            return new ObjectRestResponse<>().msg("登录成功").rel(true);
        }else{
            return new ObjectRestResponse<>().msg("用户名或密码不正确").rel(false);
        }
    }

    @RequestMapping(value = "/chgpwd",method = RequestMethod.GET)
    public String chgpwd(){
        return "user/chgpwd";
    }

    @RequestMapping(value = "/chgpwd",method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse update(@RequestParam String pwd){
        if(SecurityContextHolder.getContext().getAuthentication()!= null){
            SecurityUser user = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.updatePassword(user.getUsername(),pwd);
        }
        return new ObjectRestResponse<>().rel(false);
    }

}
