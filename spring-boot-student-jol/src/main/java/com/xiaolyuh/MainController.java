package com.xiaolyuh;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author yuhao.wang
 */
@RestController
public class MainController {

    @GetMapping("/")
    public String helloWorld() {

        return "Hello World";
    }

    public static void main(String[] args) {
        HashMap<String,String> hashMap=new HashMap();
        hashMap.put("1","obj1");
        hashMap.put("2","obj2");
        System.out.println(ClassLayout.parseClass(Object.class).toPrintable());
        System.out.println(ClassLayout.parseInstance(new Integer(1)).toPrintable());
        //hashmap对象内部信息
        System.out.println(ClassLayout.parseInstance(hashMap).toPrintable());
        //查看hashmap对象外部信息
        System.out.println(GraphLayout.parseInstance(hashMap).toPrintable());
        //hashmap对象的总大小(包括里面的key)
        System.out.println(GraphLayout.parseInstance(hashMap).totalSize());
    }
}