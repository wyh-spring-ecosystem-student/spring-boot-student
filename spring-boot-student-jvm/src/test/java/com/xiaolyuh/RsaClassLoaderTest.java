package com.xiaolyuh;

import com.alibaba.fastjson.JSON;

/**
 * RSA 加密的ClassLoader
 *
 * @author yuhao.wang3
 * @since 2020/1/19 17:06
 */
public class RsaClassLoaderTest extends ClassLoader {


    public static void main(String[] args) throws Exception {
        RsaClassLoader loader = new RsaClassLoader();
        Object object = loader.loadClass("com.xiaolyuh.controller.UserController").newInstance();
        System.out.println("使用默认类加载器： class :" + object.getClass() + "  ClassLoader:" + object.getClass().getClassLoader());

        Object object2 = loader.loadClass("com.xiaolyuh.UserController").newInstance();
        System.out.println("使用自定义类加载器：class :" + object2.getClass() + "  ClassLoader:" + object2.getClass().getClassLoader());
        System.out.println(JSON.toJSONString(object2));
    }
}
