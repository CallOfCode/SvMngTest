package com.github.cc.gate.gateway.biz;

import com.github.cc.gate.common.biz.BaseBiz;
import com.github.cc.gate.gateway.entity.Element;
import com.github.cc.gate.gateway.mapper.ElementMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementBiz extends BaseBiz<ElementMapper,Element> {
    public List<Element> getAuthorityElementByUserId(String userId){
        return mapper.selectAuthorityElementByUserId(userId);
    }
    public List<Element> getAuthorityElementByUserId(String userId,String menuId){
        return mapper.selectAuthorityMenuElementByUserId(userId,menuId);
    }
}
