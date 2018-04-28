package com.xiaolyuh.handler;


import com.xiaolyuh.wrapper.WrapMapper;
import com.xiaolyuh.wrapper.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author yuhao.wang
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 统一处理 BindException 错误
     *
     * @param ex 参数验证失败错误
     * @return 参数验证失败响应
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Wrapper<?> processBindException(BindException ex, HttpSession httpSession, HttpServletRequest request) {
        // 获取错误信息
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // 目前消息只返回一条错误信息，所以只需要取第一条错误信息即可
        Map<String, String> errorMap = null;
        if (fieldErrors.size() > 0) {
            errorMap = new HashMap<>(fieldErrors.size());
            for (FieldError fieldError : fieldErrors) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        // 请求路径
        String url = request.getRequestURI();

        logger.warn("请求sessionId：{}，请求接口：{}，请求参数：{}, 异常信息: {}", httpSession.getId(), url, errorMap, ex.getMessage(), ex);

        return WrapMapper.wrap(10000, "参数校验异常", errorMap);
    }


}
