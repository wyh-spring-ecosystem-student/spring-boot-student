package com.xiaolyuh.iimport;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yuhao.wang3
 * @since 2019/8/26 10:58
 */
public class AnimalImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 不能返回NULL，否则会报空指针异常，打断点可以看到源码
        return new String[]{"com.xiaolyuh.iimport.bean.FishTestBean",
                "com.xiaolyuh.iimport.bean.TigerTestBean" };
    }
}
