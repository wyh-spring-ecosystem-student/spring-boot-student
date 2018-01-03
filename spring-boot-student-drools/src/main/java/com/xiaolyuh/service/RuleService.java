package com.xiaolyuh.service;

import com.xiaolyuh.domain.model.Rule;
import org.kie.api.runtime.KieSession;

import java.util.List;

/**
 * Created by yuhao.wang on 2017/6/19.
 */
public interface RuleService {
    List<Rule> findAll();

    Rule update(Rule rule);

    Rule insert(Rule rule);

    void reloadRule();

    /**
     * 根据ruleKey获取规则
     * @param ruleKey
     * @return
     */
    KieSession getKieSessionByName(String ruleKey);
}
