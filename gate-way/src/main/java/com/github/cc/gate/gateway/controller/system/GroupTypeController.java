package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.biz.BaseController;
import com.github.cc.gate.common.message.TableResultResponse;
import com.github.cc.gate.gateway.biz.GroupTypeBiz;
import com.github.cc.gate.gateway.entity.GroupType;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping(value = "/groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz,GroupType> {
    @RequestMapping(value = "",method = RequestMethod.GET)
    public String groupType(){
        return "groupType/list";
    }

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String groupTypeEdit(){
        return "groupType/edit";
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<GroupType> page(int limit, int offset, String name){
        Example example = new Example(GroupType.class);
        if(StringUtils.isNotBlank(name))
            example.createCriteria().andLike("name", "%" + name + "%");
        int count = baseBiz.selectCountByExample(example);
        PageHelper.startPage(offset, limit);
        return new TableResultResponse<GroupType>(count,baseBiz.selectByExample(example));
    }
}
