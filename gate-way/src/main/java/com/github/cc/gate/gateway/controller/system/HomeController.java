package com.github.cc.gate.gateway.controller.system;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * ${DESCRIPTION}
 */
@Controller
public class HomeController {
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index(Map<String,Object> map){
        map.put("user",SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal());
        return "index";
    }
    @RequestMapping(value = "about",method = RequestMethod.GET)
    public String about(){
        return "about";
    }

}
