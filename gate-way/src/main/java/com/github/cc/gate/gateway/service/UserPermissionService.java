package com.github.cc.gate.gateway.service;

import com.alibaba.fastjson.JSONObject;
import com.github.cc.gate.common.constant.CommonConstant;
import com.github.cc.gate.common.util.TreeUtil;
import com.github.cc.gate.gateway.biz.ElementBiz;
import com.github.cc.gate.gateway.biz.MenuBiz;
import com.github.cc.gate.gateway.biz.UserBiz;
import com.github.cc.gate.gateway.entity.Element;
import com.github.cc.gate.gateway.entity.Menu;
import com.github.cc.gate.gateway.entity.User;
import com.github.cc.gate.gateway.vo.authority.PermissionInfo;
import com.github.cc.gate.gateway.vo.treeNode.MenuTree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPermissionService {
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private ElementBiz elementBiz;

    public List<PermissionInfo> getPermissionByUsername(String username){
        User user = userBiz.getUserByUsername(username);
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        for(Menu menu:menus){
            if(StringUtils.isBlank(menu.getHref()))
                continue;
            info = new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(CommonConstant.RESOURCE_TYPE_MENU);
            info.setName(CommonConstant.RESOURCE_ACTION_VISIT);
            String uri = menu.getHref();
            if(!uri.startsWith("/"))
                uri = "/"+uri;
            info.setUri(uri);
            info.setMethod(CommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info
            );
            info.setMenu(menu.getTitle());
        }
        List<Element> elements = elementBiz.getAuthorityElementByUserId(user.getId()+"");
        for(Element element:elements){
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            result.add(info);
        }
        return result;
    }

    public String getSystemsByUsername(String username){
        int userId = userBiz.getUserByUsername(username).getId();
        return JSONObject.toJSONString(menuBiz.getUserAuthoritySystemByUserId(userId));
    }

    public String getMenusByUsername(String username,Integer parentId){
        int userId = userBiz.getUserByUsername(username).getId();
        try {
            if (parentId == null||parentId<0) {
                parentId = menuBiz.getUserAuthoritySystemByUserId(userId).get(0).getId();
            }
        } catch (Exception e) {
            return JSONObject.toJSONString(new ArrayList<MenuTree>());
        }
        return JSONObject.toJSONString(getMenuTree(menuBiz.getUserAuthorityMenuByUserId(userId), parentId));
    }

    private List<MenuTree> getMenuTree(List<Menu> menus,int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root) ;
    }
}
