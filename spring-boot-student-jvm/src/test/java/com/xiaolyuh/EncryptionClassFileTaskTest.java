package com.xiaolyuh;

import com.xiaolyuh.utils.EncryptionClassFileTask;
import org.assertj.core.util.Lists;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * 加密Class文件
 * {@link com.xiaolyuh.utils.EncryptionClassFileTask}
 *
 * @author yuhao.wang3
 * @since 2020/1/20 10:39
 */
public class EncryptionClassFileTaskTest {
    public static void main(String[] args) throws Exception {
        String testPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCE7vuntatVmQVp6xGlBa/U/cEkKtFjyhsTtn1inlYtw5KSasTfa/HMPwJKp1QchsGEt0usOkHHC9HuD8o1gKx/Dgjo6b/XGu6xhinyRjCJWLSHXGOq9VLryaThwZsRB4Bb5DU9NXkl8WE2ih8QEKO1143KeJ5SE38awi74im0dzQIDAQAB";

        List<String> excludeClass = Lists.newArrayList("MainController");
        List<String> packages = Lists.newArrayList("com.xiaolyuh.controller");

        encryptionClassFile("D:\\aes_class", packages, excludeClass, testPublicKey);
    }

    private static void encryptionClassFile(String filePath, List<String> packages, List<String> excludeClass, String publicKey) throws Exception {
        ForkJoinPool pool = new ForkJoinPool(16);

        File classFile = new File(filePath);
        if (!classFile.exists()) {
            throw new NoSuchFileException("File does not exist!");
        }
        pool.invoke(new EncryptionClassFileTask(classFile, packages, excludeClass, publicKey));
    }
}
