package com.xiaolyuh.sentinel.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;


@Configuration
public class SentinelDataSourceInitFuncConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void initSentinelDataSourceInitFuncConfig() {

        String appName = StringUtils.isNotBlank(System.getProperty("project.name")) ? System.getProperty("project.name") : applicationName;

        final String remoteAddress = "127.0.0.1:2181";

        // 流控规则
        final String flowPath = "/sentinel_rule_config/" + appName + "/flow";
        ReadableDataSource<String, List<FlowRule>> redisFlowDataSource = new ZookeeperDataSource<>(remoteAddress, flowPath,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(redisFlowDataSource.getProperty());

        // 降级规则
        final String degradePath = "/sentinel_rule_config/" + appName + "/degrade";
        ReadableDataSource<String, List<DegradeRule>> redisDegradeDataSource = new ZookeeperDataSource<>(remoteAddress, degradePath,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                }));
        DegradeRuleManager.register2Property(redisDegradeDataSource.getProperty());
    }

}