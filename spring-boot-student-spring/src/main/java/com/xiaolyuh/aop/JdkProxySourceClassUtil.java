package com.xiaolyuh.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 输出代理类的Class文件
 *
 * @author yuhao.wang3
 */
public class JdkProxySourceClassUtil {
    private static Logger logger = LoggerFactory.getLogger(JdkProxySourceClassUtil.class);

    private JdkProxySourceClassUtil() {
    }

    /**
     * @param proxyClassName 代理类名称（$proxy4）
     * @param aClass         目标类对象
     */
    public static void writeClassToDisk(String proxyClassName, Class aClass) {
        String path = ".\\" + proxyClassName + ".class";
        writeClassToDisk(path, proxyClassName, aClass);
    }

    /**
     * @param path           输出路径
     * @param proxyClassName 代理类名称（$proxy4）
     * @param aClass         目标类对象
     */
    public static void writeClassToDisk(String path, String proxyClassName, Class aClass) {

        byte[] classFile = ProxyGenerator.generateProxyClass(proxyClassName, new Class[]{aClass});
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            fos.write(classFile);
            fos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}

