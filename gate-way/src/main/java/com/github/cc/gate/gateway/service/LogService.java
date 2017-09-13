package com.github.cc.gate.gateway.service;

import com.github.cc.gate.gateway.biz.GateLogBiz;
import com.github.cc.gate.gateway.entity.GateLog;
import com.github.cc.gate.gateway.vo.log.LogInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ${DESCRIPTION}
 *
 */
@Service
public class LogService {
    @Autowired
    private GateLogBiz gateLogBiz;

    public void saveLog(LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        gateLogBiz.insertSelective(log);
    }
}
