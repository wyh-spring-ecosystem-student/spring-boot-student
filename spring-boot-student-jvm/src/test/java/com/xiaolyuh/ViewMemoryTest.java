package com.xiaolyuh;

import org.assertj.core.util.Lists;
import org.openjdk.jol.info.ClassLayout;

import java.util.List;

/**
 * 查看对象内存大小，openjdk的jol工具可以帮我们查看对象占用空间大小
 *
 * <dependency>
 * <groupId>org.openjdk.jol</groupId>
 * <artifactId>jol-core</artifactId>
 * <version>0.10</version>
 * </dependency>
 *
 * @author yuhao.wang3
 * @since 2020/3/2 10:04
 */
public class ViewMemoryTest {

    public static void main(String[] args) {
        // 输出Object对象的空间占用情况
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());

        List<String> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        // 输出List对象的空间占用情况
        System.out.println(ClassLayout.parseInstance(list).toPrintable());


    }
}
