package com.github.cc.gate.gateway.controller.system;

import com.github.cc.gate.common.biz.BaseController;
import com.github.cc.gate.common.message.TableResultResponse;
import com.github.cc.gate.gateway.biz.UserBiz;
import com.github.cc.gate.gateway.entity.User;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

/**
 * ${DESCRIPTION}
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController<UserBiz,User> {

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String user(){
        return "user/list";
    }
    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public String userEdit(){
        return "user/edit";
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<User> page(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "1")int offset, String name){
        Example example = new Example(User.class);
        if(StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
            example.createCriteria().andLike("username", "%" + name + "%");
        }
        int count = baseBiz.selectCountByExample(example);
        PageHelper.startPage(offset, limit);
        return new TableResultResponse<User>(count,baseBiz.selectByExample(example));
    }


}
