package com.xiaolyuh.service.impl;

import com.xiaolyuh.domain.mapper.RuleMapper;
import com.xiaolyuh.domain.model.Rule;
import com.xiaolyuh.service.RuleService;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by yuhao.wang on 2017/6/19.
 */
@Service
public class RuleServiceImpl implements RuleService {

    public static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    @Autowired
    private RuleMapper ruleMapper;

    @Override
    public List<Rule> findAll() {

        return ruleMapper.findAll();
    }

    @Override
    public Rule update(Rule rule) {
        rule.setUpdateTime(new Date());
        ruleMapper.updateByPrimaryKey(rule);
        return rule;
    }

    @Override
    public Rule insert(Rule rule) {
        rule.setCreateTime(new Date());
        rule.setUpdateTime(new Date());
        ruleMapper.insertSelective(rule);
        return rule;
    }

    @Override
    public KieSession getKieSessionByName(String ruleKey) {
        StatefulKnowledgeSession kSession = null;
        try {
            long startTime = System.currentTimeMillis();
            // TODO 可以缓存到本地，不用每次都去查数据库
            Rule rule = ruleMapper.findByRuleKey(ruleKey);

            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            // 装入规则，可以装入多个
            kb.add(ResourceFactory.newByteArrayResource(rule.getContent().getBytes("utf-8")), ResourceType.DRL);

            // 检查错误
            KnowledgeBuilderErrors errors = kb.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }

            // TODO 没有找到替代方法
            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
            kBase.addKnowledgePackages(kb.getKnowledgePackages());

            // 创建kSession
            kSession = kBase.newStatefulKnowledgeSession();
            long endTime = System.currentTimeMillis();
            logger.info("获取kSession耗时：{}", endTime - startTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return kSession;
    }

    // 不使用kieContainer这种方式，因为创建kSession的时候只能用kieContainer.newKieSession()，
    // 不能指定要创建那个kSession，在执行规则的时候会把所有的规则都执行一次
    @Override
    public void reloadRule() {
        KieContainer kieContainer = loadContainerFromString(findAll());
    }

    private KieContainer loadContainerFromString(List<Rule> rules) {
        long startTime = System.currentTimeMillis();
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        KieFileSystem kfs = ks.newKieFileSystem();

        for (Rule rule : rules) {
            String drl = rule.getContent();
            kfs.write("src/main/resources/" + drl.hashCode() + ".drl", drl);
        }

        KieBuilder kb = ks.newKieBuilder(kfs);
        kb.buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time to build rules : " + (endTime - startTime) + " ms");
        startTime = System.currentTimeMillis();
        KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
        endTime = System.currentTimeMillis();
        System.out.println("Time to load container: " + (endTime - startTime) + " ms");
        return kContainer;
    }
}
