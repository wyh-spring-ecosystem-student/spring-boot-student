package com.xiaolyuh.component.scan;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 自定义过滤规则类
 *
 * @author yuhao.wang3
 * @since 2019/8/24 11:39
 */
public class CustomTypeFilter implements TypeFilter {

    /**
     * @param metadataReader        当前正在扫描的类信息(元数据信息)
     * @param metadataReaderFactory 可以获取到其他类的任何信息
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 获取当前类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        // 获取正在扫描类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        // 获取当前资源的类路径
        Resource resource = metadataReader.getResource();

        String classname = classMetadata.getClassName();
        System.out.println("------->  " + classname);
        // 加载CustomTypeFilterService类
        if (classname.contains("Custom")) {
            return true;
        }

        return false;
    }
}
