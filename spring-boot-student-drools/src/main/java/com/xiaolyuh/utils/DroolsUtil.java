package com.xiaolyuh.utils;

import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuhao.wang
 */
public class DroolsUtil {
    public static final Logger log = LoggerFactory.getLogger(DroolsUtil.class);

    /**
     * 线程安全单例
     */
    private static volatile KieServices kieServices = KieServices.Factory.get();
    /**
     * KieBase容器，线程安全单例
     */
    private static volatile KieContainer kieContainer;

    /**
     * 加载KieContainer容器
     */
    public static KieContainer loadKieContainer() throws RuntimeException {
        //通过kmodule.xml 找到规则文件,这个文件默认放在resources/META-INF文件夹
        log.info("准备创建 KieContainer");

        if (kieContainer == null) {
            log.info("首次创建：KieContainer");
            // 设置drools的日期格式
            System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
            //线程安全
            synchronized (DroolsUtil.class) {
                if (kieContainer == null) {
                    // 创建Container
                    kieContainer = kieServices.getKieClasspathContainer();
                    // 检查规则文件是否有错
                    Results results = kieContainer.verify();
                    if (results.hasMessages(Message.Level.ERROR)) {
                        StringBuffer sb = new StringBuffer();
                        for (Message mes : results.getMessages()) {
                            sb.append("解析错误的规则：").append(mes.getPath()).append(" 错误位置：").append(mes.getLine()).append(";");
                        }
                        throw new RuntimeException(sb.toString());
                    }
                }
            }

        }
        log.info("KieContainer创建完毕");
        return kieContainer;
    }

    /**
     * 根据kiesession 名称创建KieSession ，每次调用都是一个新的KieSession
     * @param name kiesession的名称
     * @return 一个新的KieSession，每次使用后要销毁
     */
    public static KieSession getKieSessionByName(String name) {
        if (kieContainer == null) {
            kieContainer = loadKieContainer();
        }
        KieSession kieSession;
        try {
            kieSession = kieContainer.newKieSession(name);
        } catch (Exception e) {
            log.error("根据名称：" + name + " 创建kiesession异常");
            throw new RuntimeException(e);
        }
        return kieSession;
    }

}