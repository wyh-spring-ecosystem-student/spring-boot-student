package com.xiaolyuh.controller;

import com.xiaolyuh.Result;
import com.xiaolyuh.vo.PersonRequest;
import com.xiaolyuh.vo.PersonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhao.wang
 */
@Api(tags = "用户信息查询", hidden = false)
@RestController
public class PersonController {

    @PostMapping("save")
    @ApiOperation(value = "保存用户信息", httpMethod = "POST")
    public Result<PersonResponse> save(@RequestBody PersonRequest person) {
        person.setId(12L);
        PersonResponse personResponse = new PersonResponse();
        BeanUtils.copyProperties(person, personResponse);
        return Result.success(personResponse);
    }

    @PostMapping(value = "update")
    @ApiOperation(value = "更新用户信息", httpMethod = "POST")
    public Result<PersonResponse> update(@RequestBody PersonRequest person) {
        person.setId(12L);
        PersonResponse personResponse = new PersonResponse();
        BeanUtils.copyProperties(person, personResponse);
        return Result.success(personResponse);
    }

}