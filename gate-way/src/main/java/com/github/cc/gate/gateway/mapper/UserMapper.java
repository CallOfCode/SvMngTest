package com.github.cc.gate.gateway.mapper;

import com.github.cc.gate.gateway.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    public List<User> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<User> selectLeaderByGroupId(@Param("groupId") int groupId);
    public void updatePwdByUsername(@Param("username") String username,@Param("password") String password);
}
