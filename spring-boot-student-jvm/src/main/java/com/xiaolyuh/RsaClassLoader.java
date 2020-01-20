package com.xiaolyuh;

import com.xiaolyuh.utils.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * RSA 加密的ClassLoader
 *
 * @author yuhao.wang3
 * @since 2020/1/19 17:06
 */
public class RsaClassLoader extends ClassLoader {
    private static final int MAGIC = 0xcafebabe;

    Logger logger = LoggerFactory.getLogger(RsaClassLoader.class);

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            // 先使用父加载器加载
            return getParent().loadClass(name);
        } catch (Throwable t) {
            return findClass(name);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            logger.warn("请输入解密私钥，否则无法启动服务");
            System.out.print("请输入解密私钥：：");
            String privateKey = br.readLine();
            logger.info("解密[{}] 文件 开始", name);
            byte[] bytes = RSAUtil.decryptByPrivateKey(RSAUtil.toByteArray(inputStream), privateKey);
            logger.info("解密[{}] 文件 结束", name);
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            logger.info("解密 [{}] 文件异常: {}", name, e.getMessage(), e);
            throw new ClassNotFoundException(String.format("解密 [%s] 文件异常: %s", name, e.getCause()));
        }
    }
}
