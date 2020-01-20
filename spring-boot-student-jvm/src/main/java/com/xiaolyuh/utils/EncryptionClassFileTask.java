package com.xiaolyuh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.RecursiveAction;

/**
 * 加密Class文件
 *
 * @author yuhao.wang3
 * @since 2020/1/20 10:39
 */
public class EncryptionClassFileTask extends RecursiveAction {

    Logger logger = LoggerFactory.getLogger(EncryptionClassFileTask.class);

    /**
     * 公钥
     */
    String publicKey;

    /**
     * 需要加密的目录
     */
    private File file;

    /**
     * 需要加密的包名
     */
    private List<String> packages;

    /**
     * 需要排除的Class名称
     */
    private List<String> excludeClass;


    /**
     * @param file      需要加密文件的目录
     * @param packages  需要加密的包名
     * @param publicKey 公钥
     */
    public EncryptionClassFileTask(File file, List<String> packages, String publicKey) {
        this(file, packages, null, publicKey);
    }

    /**
     * @param file         需要加密文件的目录
     * @param packages     需要加密的包名
     * @param excludeClass 需要排除的类名称
     * @param publicKey    公钥
     */
    public EncryptionClassFileTask(File file, List<String> packages, List<String> excludeClass, String publicKey) {
        this.file = file;
        this.excludeClass = excludeClass;
        this.publicKey = publicKey;
        this.packages = new ArrayList<>();

        if (Objects.nonNull(packages)) {
            for (String packageName : packages) {
                this.packages.add(packageName.replace('.', File.separatorChar));
            }
        }

        if (Objects.isNull(excludeClass)) {
            this.excludeClass = new ArrayList<>();
        }

        this.excludeClass.add("RsaClassLoader");
    }

    @Override
    protected void compute() {
        if (Objects.isNull(file)) {
            return;
        }

        File[] files = file.listFiles();
        List<EncryptionClassFileTask> fileTasks = new ArrayList<>();
        if (Objects.nonNull(files)) {
            for (File f : files) {
                // 拆分任务
                if (f.isDirectory()) {
                    fileTasks.add(new EncryptionClassFileTask(f, packages, excludeClass, publicKey));
                } else {
                    if (f.getAbsolutePath().endsWith(".class")) {
                        if (!CollectionUtils.isEmpty(excludeClass) && excludeClass.contains(f.getName().substring(0, f.getName().indexOf(".")))) {
                            continue;
                        }
                        // 如果packages为空直接加密文件夹下所有文件
                        if (CollectionUtils.isEmpty(packages)) {
                            encryptFile(f);
                            return;
                        }
                        // 如果packages不为空则加密指定报名下面的文件
                        for (String packageName : packages) {
                            if (f.getPath().contains(packageName.replace('.', File.separatorChar))) {
                                encryptFile(f);
                                return;
                            }
                        }
                    }
                }
            }
            // 提交并执行任务
            invokeAll(fileTasks);
            for (EncryptionClassFileTask fileTask : fileTasks) {
                // 等待任务执行完成
                fileTask.join();
            }
        }
    }

    private void encryptFile(File file) {
        try {
            logger.info("加密[{}] 文件 开始", file.getPath());
            byte[] bytes = RSAUtil.encryptByPublicKey(RSAUtil.toByteArray(file), publicKey);

            try (FileChannel fc = new FileOutputStream(file.getPath()).getChannel()) {
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                fc.write(bb);
                logger.info("加密[{}] 文件 结束", file.getPath());
            }
        } catch (IOException e) {
            logger.error("加密文件 {} 异常：{}", file.getPath(), e.getMessage(), e);
        }
    }
}
