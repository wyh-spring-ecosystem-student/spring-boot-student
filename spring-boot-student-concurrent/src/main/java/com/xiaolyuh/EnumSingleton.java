package com.xiaolyuh;

/**
 * 线程安全的单例的模式
 *
 * @author yuhao.wang3
 * @since 2019/8/20 19:45
 */
public class EnumSingleton {
    private EnumSingleton() {
    }

    public static EnumSingleton getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE(new EnumSingleton());
        private EnumSingleton singleton;

        //JVM会保证此方法绝对只调用一次
        Singleton(EnumSingleton singleton) {
            this.singleton = singleton;
        }

        public EnumSingleton getInstance() {
            return singleton;
        }
    }
}


