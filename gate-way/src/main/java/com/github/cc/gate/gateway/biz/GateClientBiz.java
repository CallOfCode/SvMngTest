package com.github.cc.gate.gateway.biz;

import com.github.cc.gate.common.biz.BaseBiz;
import com.github.cc.gate.common.constant.CommonConstant;
import com.github.cc.gate.gateway.constant.UserConstant;
import com.github.cc.gate.gateway.entity.Element;
import com.github.cc.gate.gateway.entity.GateClient;
import com.github.cc.gate.gateway.mapper.ElementMapper;
import com.github.cc.gate.gateway.mapper.GateClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GateClientBiz extends BaseBiz<GateClientMapper,GateClient> {
    @Autowired
    private ElementMapper elementMapper;
    @Override
    public void insertSelective(GateClient entity) {
        String secret = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getSecret());
        entity.setSecret(secret);
        entity.setLocked(CommonConstant.BOOLEAN_NUMBER_FALSE);
        super.insertSelective(entity);
    }

    @Override
    public void updateById(GateClient entity) {
        GateClient old =  new GateClient();
        old.setId(entity.getId());
        old = mapper.selectOne(old);
        if(!entity.getSecret().equals(old.getSecret())){
            String secret = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getSecret());
            entity.setSecret(secret);
        }
        super.updateById(entity);
    }

    public void updateLockById(int id,String locked){
        mapper.updateClientClockById(id,locked);
    }

    public void modifyClientServices(int id, String services) {
        mapper.deleteClientServiceById(id);
        if(!StringUtils.isEmpty(services)){
            String[] mem = services.split(",");
            for(String m:mem){
                mapper.insertClientServiceById(id, Integer.parseInt(m));
            }
        }
    }

    public List<Element> getClientServices(int id) {
        return elementMapper.selectAuthorityElementByClientId(id+"");
    }
}
