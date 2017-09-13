package com.github.cc.gate.gateway.biz;

import com.github.cc.gate.common.biz.BaseBiz;
import com.github.cc.gate.gateway.entity.GateLog;
import com.github.cc.gate.gateway.mapper.GateLogMapper;
import org.springframework.stereotype.Service;

/**
 * ${DESCRIPTION}
 *
 */
@Service
public class GateLogBiz extends BaseBiz<GateLogMapper,GateLog> {

    @Override
    public void insert(GateLog entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insertSelective(entity);
    }
}
