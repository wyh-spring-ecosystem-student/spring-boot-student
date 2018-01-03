package com.xiaolyuh.service.impl;

import com.xiaolyuh.domain.mapper.RuleMapper;
import com.xiaolyuh.domain.model.Rule;
import com.xiaolyuh.service.RuleService;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yuhao.wang on 2017/6/19.
 */
@Service
public class RuleServiceImpl implements RuleService {

    public static final Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);

    public static KieContainer kieContainer;

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
    public KieSession getKieSessionByName() {
        if (kieContainer == null) {
            synchronized (RuleServiceImpl.class) {
                if (kieContainer == null) {
                    reloadRule();
                }
            }
        }
        long startTime = System.currentTimeMillis();
        KieSession kieSession;
        try {
            kieSession = kieContainer.newKieSession();
        } catch (Exception e) {
            logger.error("创建kiesession异常");
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("获取kieSession耗时 : " + (endTime - startTime) + " ms");
        return kieSession;
    }

    @Override
    public void reloadRule() {
        KieContainer kieContainer = loadContainerFromString(findAll());
        RuleServiceImpl.kieContainer = kieContainer;
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
