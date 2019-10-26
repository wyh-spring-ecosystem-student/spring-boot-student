package com.xiaolyuh;

import com.xiaolyuh.util.ObjectUtils;
import org.junit.Test;

/**
 * @author yuhao.wang3
 * @since 2019/10/26 15:23
 */
public class ObjectUtilsTest {

    @Test
    public void testIsComplexObject() {
        System.out.println(ObjectUtils.isComplexObject(1));
        System.out.println(ObjectUtils.isComplexObject(new Integer(1)));
        System.out.println(ObjectUtils.isComplexObject(1.1));
        System.out.println(ObjectUtils.isComplexObject(1.1F));
        System.out.println(ObjectUtils.isComplexObject(1L));
        System.out.println(ObjectUtils.isComplexObject(true));
        System.out.println(ObjectUtils.isComplexObject("1"));
        int.class.isPrimitive();
    }
}
