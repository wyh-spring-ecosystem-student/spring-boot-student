package com.xiaolyuh;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 与前端交互对象
 *
 * @param <T>
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 5925101851082556646L;
    private T data;
    @ApiModelProperty(value = "状态码",example = "SUCCESS")
    private Status status;
    @ApiModelProperty(value = "错误编码",example = "4001")
    private String code;
    private String message;

    public static Result success() {
        Result result = new Result<>();
        result.setCode("200");
        result.setStatus(Status.SUCCESS);
        result.setMessage(Status.SUCCESS.name());
        return result;
    }

    public static <T> Result success(T data) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setStatus(Status.SUCCESS);
        result.setMessage(Status.SUCCESS.name());
        result.setData(data);
        return result;
    }

    public static <T> Result error(String msg) {
        Result<T> result = new Result<>();
        result.setCode("500");
        result.setStatus(Status.ERROR);
        result.setMessage(msg);
        return result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static enum Status {
        SUCCESS("OK"),
        ERROR("ERROR");

        private String code;

        private Status(String code) {
            this.code = code;
        }

        public String code() {
            return this.code;
        }
    }

}