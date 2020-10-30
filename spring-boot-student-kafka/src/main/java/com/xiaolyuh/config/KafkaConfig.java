package com.xiaolyuh.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * kafka配置类
 *
 * @author olafwang
 * @since 2020/9/29 2:45 下午
 */
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducer<String, String> producerRecord() {

        Properties properties = new Properties();
        // 配置kafka集群地址，不用将全部机器都写上，zk会自动发现全部的kafka broke
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        // 设置发送消息的应答方式
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        // 重试次数
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        // 重试间隔时间
        properties.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");
        // 一批次发送的消息大小 16KB
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "16348");
        // 一个批次等待时间,10ms
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "10");
        // RecordAccumulator 缓冲区大小  32M，如果缓冲区满了会阻塞发送端
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG, "33554432");
        // 配置拦截器, 多个逗号隔开
        properties.setProperty(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.xiaolyuh.interceptor.TraceInterceptor");

        Serializer<String> keySerializer = new StringSerializer();
        Serializer<String> valueSerializer = new StringSerializer();

        return new KafkaProducer<>(properties, keySerializer, valueSerializer);
    }

}
