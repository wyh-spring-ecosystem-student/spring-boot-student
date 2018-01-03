package com.xiaolyuh;

import com.xiaolyuh.model.Message;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.io.UnsupportedEncodingException;


public class DdLoadTest {

    public static void main(String[] args) {
        //rule,rule2可以放在数据库中，有个唯一code和他们对于，代码要执行规则的时候，根据code从数据库获取出来就OK了，这样自己开发的规则管理系统那边对数据库里的规则进行维护就行了
        String rule = "package com.xiaolyuh.drools\r\n";
        rule += "import com.xiaolyuh.model.Message;\r\n";
        rule += "rule \"rule1\"\r\n";
        rule += "\twhen\r\n";
        rule += "Message( status == 1, myMessage : msg )";
        rule += "\tthen\r\n";
        rule += "\t\tSystem.out.println( 1+\":\"+myMessage );\r\n";
        rule += "end\r\n";

        String rule2 = "package com.xiaolyuh.drools\r\n";
        rule += "import com.xiaolyuh.model.Message;\r\n";
        rule += "rule \"rule2\"\r\n";
        rule += "\twhen\r\n";
        rule += "Message( status == 2, myMessage : msg )";
        rule += "\tthen\r\n";
        rule += "\t\tSystem.out.println( 2+\":\"+myMessage );\r\n";
        rule += "end\r\n";

        StatefulKnowledgeSession kSession = null;
        try {
            long startTime = System.currentTimeMillis();
            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            //装入规则，可以装入多个
            kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
            kb.add(ResourceFactory.newByteArrayResource(rule2.getBytes("utf-8")), ResourceType.DRL);

            KnowledgeBuilderErrors errors = kb.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
            kBase.addKnowledgePackages(kb.getKnowledgePackages());

            kSession = kBase.newStatefulKnowledgeSession();
            long endTime = System.currentTimeMillis();
            System.out.println("获取kSession耗时：" + (endTime - startTime));

            Message message1 = new Message();
            message1.setStatus(1);
            message1.setMsg("hello world!");

            Message message2 = new Message();
            message2.setStatus(2);
            message2.setMsg("hi world!");

            kSession.insert(message1);
            kSession.insert(message2);
            kSession.fireAllRules();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (kSession != null)
                kSession.dispose();
        }

    }
}
