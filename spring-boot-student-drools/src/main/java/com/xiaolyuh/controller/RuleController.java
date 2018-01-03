package com.xiaolyuh.controller;

import com.xiaolyuh.domain.fact.AddressCheckResult;
import com.xiaolyuh.domain.model.Address;
import com.xiaolyuh.domain.model.Rule;
import com.xiaolyuh.service.RuleService;
import com.xiaolyuh.service.impl.RuleServiceImpl;
import com.xiaolyuh.utils.DroolsUtil;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("rules")
public class RuleController {
    public static final Logger logger = LoggerFactory.getLogger(RuleController.class);

    @Autowired
    RuleService ruleService;

    @ResponseBody
    @RequestMapping("address")
    public Object test(int num) {
        AddressCheckResult result = new AddressCheckResult();
        Address address = new Address();
        address.setPostcode(generateRandom(num));

        KieSession kieSession = ruleService.getKieSessionByName();
        int ruleFiredCount = 0;
        try {
            kieSession.insert(address);
            kieSession.insert(result);
            ruleFiredCount = kieSession.fireAllRules();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            if (kieSession != null) {
                kieSession.destroy();
            }
        }
        System.out.println("触发了" + ruleFiredCount + "条规则");

        if (result.isPostCodeResult()) {
            System.out.println("规则校验通过");
        }

        return "ok";
    }

    /**
     * 从数据加载最新规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reloadRule")
    public String reloadRule() throws IOException {
        ruleService.reloadRule();
        return "ok";
    }

    /**
     * 查询规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("query")
    public List<Rule> query() throws IOException {

        return ruleService.findAll();
    }

    /**
     * 修改规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("update")
    public Rule update(Rule rule) throws IOException {

        return ruleService.update(rule);
    }

    /**
     * 新增规则
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("add")
    public Rule add(Rule rule) throws IOException {

        return ruleService.insert(rule);
    }


    /**
     * 生成随机数
     *
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number = number.append(chars.charAt(rand));
        }
        return number.toString();
    }

}