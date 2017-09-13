package com.github.cc.gate.gateway.mapper;

import com.github.cc.gate.gateway.entity.GateClient;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GateClientMapper extends Mapper<GateClient> {
    public void insertClientServiceById(@Param("clientId") int clientId, @Param("serviceId") int serviceId);
    public void deleteClientServiceById(@Param("clientId") int clientId);
    public void updateClientClockById(@Param("id") int id,@Param("locked")String locked);
}