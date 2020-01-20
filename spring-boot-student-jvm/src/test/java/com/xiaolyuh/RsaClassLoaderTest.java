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
        // 打包
        // jar -cvfM0 test.jar -C .\spring-boot-student-jvm-0.0.1-SNAPSHOT\ .

        // 运行jar
        // java -classpath -jar .\spring-boot-student-jvm-0.0.1-SNAPSHOT.jar com.xiaolyuh.RsaClassLoaderTest
        // java -cp -jar .\spring-boot-student-jvm-0.0.1-SNAPSHOT.jar com.xiaolyuh.RsaClassLoaderTest
        // String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAITu+6e1q1WZBWnrEaUFr9T9wSQq0WPKGxO2fWKeVi3DkpJqxN9r8cw/AkqnVByGwYS3S6w6QccL0e4PyjWArH8OCOjpv9ca7rGGKfJGMIlYtIdcY6r1UuvJpOHBmxEHgFvkNT01eSXxYTaKHxAQo7XXjcp4nlITfxrCLviKbR3NAgMBAAECgYAHUgXHuYhi4Vdb+tbw6HxDVWoCbN01CpctIbqL6L5ELOXwbDLFPvOE1N9ybv6Bx6X2ggWHyXl/1ZXM70+qXJijHZNcDWPocbJDrOOHwePyNreSU0UGNRcBJ7RVnH2YDWut9GZFv88lUp6/xNUJ8QG7a4kOcLsb5L1YrkV+aEeiXQJBAO0rkR0a/ZHUcKgIzF+OaGnwMqiZHIPIZe7xkTTaEk5Qw6HWNOWR6jbwF9bomVBSAM78IFRrD8yUAR6CsAbhUWsCQQCPfNQX1ikUlDutvPGXxqbzUmiBDH2x7xbvPIySfCaWTjYm+YQDzHtfW5lx96HyWcNp/4ryFP8vNZ8p8DCN5kOnAkBqxIUkRCVIxAkfLC7NCa/pmQ9FJQBYNxvkUG1dDJrXFLatIWBYxLJanwUsYzO5m+DvTUNEnZnUMAC8+npB7qcXAkAmfoiv9GaE/Ned3qi53TOA58TdiipWiBwRBp931RLNFCJ3Bk2ib0NR69MYviSWTfqc/0+ZboSfd7VBnQyJpRLVAkB1sskxSIKMt3vGws0vyfY1B2GwosRtRRl+h9uQxcvM6is39OpKFwAmurL9n0SjtvSdIT/XltoP69hcgz83hem0

        RsaClassLoader loader = new RsaClassLoader();
        Object object = loader.loadClass("com.xiaolyuh.controller.UserController2").newInstance();
        System.out.println("使用默认类加载器： class :" + object.getClass() + "  ClassLoader:" + object.getClass().getClassLoader());

        Object object2 = loader.loadClass("com.xiaolyuh.controller.UserController").newInstance();
        System.out.println("使用自定义类加载器：class :" + object2.getClass() + "  ClassLoader:" + object2.getClass().getClassLoader());
        System.out.println(JSON.toJSONString(object2));
    }
}
