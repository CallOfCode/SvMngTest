package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.biz.BaseController;
import com.github.cc.gate.common.message.ObjectRestResponse;
import com.github.cc.gate.common.message.TableResultResponse;
import com.github.cc.gate.gateway.biz.ElementBiz;
import com.github.cc.gate.gateway.biz.UserBiz;
import com.github.cc.gate.gateway.constant.CacheConstant;
import com.github.cc.gate.gateway.entity.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Controller
@RequestMapping(value="/element")
public class ElementController extends BaseController<ElementBiz,Element> {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String elementEdit(){
        return "element/edit";
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    @CacheEvict(value = CacheConstant.CACHE_SERVICE_PERMISSIONS,key = "#entity.code",beforeInvocation = false)//新增时code可能重复，需要更新缓存
    public ObjectRestResponse<Element> add(Element entity){
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<Element>().rel(true);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstant.CACHE_SERVICE_PERMISSIONS,allEntries = true),
                    @CacheEvict(value = CacheConstant.CACHE_CLIENT_PERMISSIONS,allEntries = true)
            }
    )
    public ObjectRestResponse<Element> update(Element entity){
        baseBiz.updateById(entity);
        return new ObjectRestResponse<Element>().rel(true);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstant.CACHE_SERVICE_PERMISSIONS,allEntries = true),
                    @CacheEvict(value = CacheConstant.CACHE_CLIENT_PERMISSIONS,allEntries = true)
            }
    )
    public ObjectRestResponse<Element> remove(@PathVariable int id){
        baseBiz.deleteById(id);
        return new ObjectRestResponse<Element>().rel(true);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Element> page(@RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "1") int offset, String name, @RequestParam(defaultValue = "0") int menuId) {
        Example example = new Example(Element.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        if(StringUtils.isNotBlank(name)){
            criteria.andLike("name", "%" + name + "%");
        }
        List<Element> elements = baseBiz.selectByExample(example);
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Element> getAuthorityElement(String menuId) {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "",menuId);
        return new ObjectRestResponse<List<Element>>().rel(true).result(elements);
    }

    @RequestMapping(value = "/user/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<Element> getAuthorityElement() {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        List<Element> elements = baseBiz.getAuthorityElementByUserId(userId + "");
        return new ObjectRestResponse<List<Element>>().rel(true).result(elements);
    }

}
