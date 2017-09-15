package com.github.cc.gate.gateway.biz;

import com.github.cc.gate.common.biz.BaseBiz;
import com.github.cc.gate.gateway.constant.UserConstant;
import com.github.cc.gate.gateway.entity.User;
import com.github.cc.gate.gateway.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * ${DESCRIPTION}
 */
@Service
public class UserBiz extends BaseBiz<UserMapper,User> {

    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    public void updateById(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.updateById(entity);
    }

    public void updatePwdByUsername(String username,String pwd){
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(pwd);
        mapper.updatePwdByUsername(username,password);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }

    /**
     * 获取spring security中的实际用户ID
     * @param securityContextImpl
     * @return
     */
/*    public int getSecurityUserId(SecurityContextImpl securityContextImpl) {
        org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) securityContextImpl.getAuthentication().getPrincipal();
        return this.getUserByUsername(securityUser.getUsername()).getId();
    }*/
}
