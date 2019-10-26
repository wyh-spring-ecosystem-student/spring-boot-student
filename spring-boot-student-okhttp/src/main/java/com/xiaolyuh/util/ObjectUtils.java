package com.xiaolyuh.util;

import java.util.Objects;

/**
 * @author yuhao.wang3
 * @since 2019/10/26 15:15
 */
public final class ObjectUtils {
    /**
     * 判读是否是复杂对象
     * 如果是：（Boolean， Character， Byte， Short， Integer， Long， Float， Double， String）返回true
     *
     * @param target
     * @return
     */
    public static boolean isComplexObject(Object target) {
        if (Objects.isNull(target)) {
            return false;
        }
        if (target instanceof String) {
            return true;
        }
        if (target instanceof Integer) {
            return true;
        }
        if (target instanceof Long) {
            return true;
        }
        if (target instanceof Boolean) {
            return true;
        }
        if (target instanceof Float) {
            return true;
        }
        if (target instanceof Double) {
            return true;
        }
        if (target instanceof Byte) {
            return true;
        }
        if (target instanceof Short) {
            return true;
        }
        if (target instanceof Character) {
            return true;
        }
        return false;
    }

    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }
}

