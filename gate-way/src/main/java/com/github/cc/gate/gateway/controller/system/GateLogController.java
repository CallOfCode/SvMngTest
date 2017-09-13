package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.biz.BaseController;
import com.github.cc.gate.gateway.biz.GateLogBiz;
import com.github.cc.gate.gateway.entity.GateLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "gateLog")
public class GateLogController extends BaseController<GateLogBiz,GateLog> {
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String gateLog(){
        return "gateLog/list";
    }
}
