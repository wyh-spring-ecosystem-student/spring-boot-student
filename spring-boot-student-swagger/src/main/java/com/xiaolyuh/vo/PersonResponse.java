package com.xiaolyuh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author olafwang
 */
@Data
@ApiModel("保存用户信息接口返回参数")
public class PersonResponse {
    @ApiModelProperty(value = "ID",example = "13",  hidden = false)
    private Long id;

    @ApiModelProperty(value = "用户名", example = "用户名", allowEmptyValue = true, required = true)
    private String name;

    @ApiModelProperty(value = "年龄", example = "29", allowEmptyValue = false, required = true)
    private Integer age;

    @ApiModelProperty(value = "地址", example = "地球村", allowEmptyValue = false, required = true)
    private String address;


}
