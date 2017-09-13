package com.github.cc.gate.gateway.mapper;

import com.github.cc.gate.gateway.entity.ResourceAuthority;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ResourceAuthorityMapper extends Mapper<ResourceAuthority> {
    public void deleteByAuthorityIdAndResourceType(@Param("authorityId")String authorityId, @Param("resourceType") String resourceType);
}