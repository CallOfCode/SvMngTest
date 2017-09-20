package com.github.cc.gate.gateway.service;

import com.github.cc.gate.common.constant.CommonConstant;
import com.github.cc.gate.gateway.biz.ElementBiz;
import com.github.cc.gate.gateway.biz.GateClientBiz;
import com.github.cc.gate.gateway.constant.CacheConstant;
import com.github.cc.gate.gateway.entity.Element;
import com.github.cc.gate.gateway.entity.GateClient;
import com.github.cc.gate.gateway.vo.authority.PermissionInfo;
import com.github.cc.gate.gateway.vo.gate.ClientInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class GateClientService {
    @Autowired
    private GateClientBiz gateClientBiz;
    @Autowired
    private ElementBiz elmentBiz;

    public ClientInfo getGateClientInfo(String clientId){
        Example example = new Example(GateClient.class);
        example.createCriteria().andEqualTo("code",clientId);
        ClientInfo clientInfo = new ClientInfo();
        GateClient gateClient = gateClientBiz.selectByExample(example).get(0);
        BeanUtils.copyProperties(gateClient,clientInfo);
        clientInfo.setLocked(CommonConstant.BOOLEAN_NUMBER_TRUE.equals(gateClient.getLocked()));
        return clientInfo;
    }

    @Cacheable(value = CacheConstant.CACHE_CLIENT_PERMISSIONS,key = "#clientId")
    public List<PermissionInfo> getGateServiceInfo(String clientId) {
        GateClient gateClient = new GateClient();
        gateClient.setCode(clientId);
        gateClient = gateClientBiz.selectOne(gateClient);
        List<PermissionInfo> infos = new ArrayList<PermissionInfo>();
        List<Element> elements = gateClientBiz.getClientServices(gateClient.getId());
        convert(infos, elements);
        return infos;
    }

    private void convert(List<PermissionInfo> infos, List<Element> elements) {
        PermissionInfo info;
        for (Element element : elements) {
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            infos.add(info);
        }
    }

}
