package com.xiaolyuh.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yuhao.wang
 */
public class InputParam implements Serializable {
    private static final long serialVersionUID = 38346822983136301L;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为NULL", groups = {
            ParameterGroup1.class, ParameterGroup2.class})
    @Min(message = "活动ID要大于0", value = 1, groups = {
            ParameterGroup1.class, ParameterGroup2.class})
    private Long activityId;

    @NotNull(message = "名称不能为null", groups = {
            ParameterGroup1.class, ParameterGroup2.class})
    private String name;

    @NotNull(message = "年龄不能为null", groups = {ParameterGroup1.class})
    @Min(message = "年龄要大于0", value = 1, groups = {ParameterGroup1.class})
    @Max(message = "年龄要小于120", value = 120, groups = {ParameterGroup1.class})
    private Long age;

    @NotNull(message = "名称不能为null", groups = {ParameterGroup2.class})
    private String address;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 验证组1
     */
    public interface ParameterGroup1 {
    }

    /**
     * 验证组2
     */
    public interface ParameterGroup2 {
    }
}
