package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.biz.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/service")
public class GateServiceController{
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String service(){
        return "service/list";
    }
}
