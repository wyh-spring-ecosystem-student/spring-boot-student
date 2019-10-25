package com.xiaolyuh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpClient工具
 *
 * @author yuhao.wang3
 */
public abstract class OkHttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtil.class);

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    /**
     * 发起 application/json 的 post 请求
     *
     * @param url           地址
     * @param param         参数
     * @param interfaceName 接口名称
     * @return
     * @throws Exception
     */
    public static <T> T postApplicationJson(String url, Object param, String interfaceName, Class<T> clazz) {
        // 生成requestBody
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , JSON.toJSONString(param));

        return post(url, interfaceName, requestBody, param, clazz);
    }

    /**
     * 发起 x-www-form-urlencoded 的 post 请求
     *
     * @param url           地址
     * @param param         参数
     * @param interfaceName 接口名称
     * @return
     * @throws Exception
     */
    public static <T> T postApplicationXWwwFormUrlencoded(String url, Object param, String interfaceName, Class<T> clazz) {
        Map<String, String> paramMap = JSON.parseObject(JSON.toJSONString(param), new TypeReference<Map<String, String>>() {
        });
        // 生成requestBody
        StringBuilder content = new StringBuilder(128);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            content.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        content.deleteCharAt(content.length() - 1);

        RequestBody requestBody = FormBody.create(MediaType.parse("application/x-www-form-urlencoded"), content.toString());

        return post(url, interfaceName, requestBody, param, clazz);
    }

    /**
     * 发起post请求，不做任何签名
     *
     * @param url           发送请求的URL
     * @param interfaceName 接口名称
     * @param requestBody   请求体
     * @param param         参数
     * @throws IOException
     */
    public static <T> T post(String url, String interfaceName, RequestBody requestBody, Object param, Class<T> clazz) {
        Request request = new Request.Builder()
                //请求的url
                .url(url)
                .post(requestBody)
                .build();

        Response response = null;
        String result = "";
        try {
            //创建/Call
            response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                logger.error("访问外部系统异常 {}: {}", url, response.toString());
                throw new RemoteAccessException("访问外部系统异常 " + response.toString());
            }
            result = response.body().string();
        } catch (RemoteAccessException e) {
            throw e;
        } catch (Exception e) {
            if (Objects.isNull(response)) {
                throw new RemoteAccessException("访问外部系统异常: " + e.getMessage(), e);
            }
            throw new RemoteAccessException("访问外部系统异常: " + response.toString(), e);
        } finally {
            logger.info("请求 {}  {}，请求参数：{}， 返回参数：{}", interfaceName, url, JSON.toJSONString(param),
                    StringUtils.isEmpty(result) ? response.toString() : request);
        }

        return JSON.parseObject(result, clazz);
    }
}
