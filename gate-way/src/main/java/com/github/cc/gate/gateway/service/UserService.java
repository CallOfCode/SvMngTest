package com.github.cc.gate.gateway.service;

import com.github.cc.gate.gateway.biz.UserBiz;
import com.github.cc.gate.gateway.entity.User;
import com.github.cc.gate.gateway.vo.user.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserBiz userBiz;

    public UserInfo getUserByUsername(String username) {
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        BeanUtils.copyProperties(user,info);
        info.setId(user.getId().toString());
        return info;
    }

}
