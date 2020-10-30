package com.xiaolyuh.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Kafka中的消息消费是一个不断轮询的过程，消费者所要做的就是重复地调用poll（）方法，而poll（）方法返回的是所订阅的主题（分区）上的一组消息。
 *
 * @author olafwang
 * @since 2020/9/29 4:17 下午
 */
@Component
public class KafkaConsumerDemo {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1));

    @PostConstruct
    public void startConsumer() {
        executor.submit(() -> {
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");

            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
            properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            // 请求超时时间
            properties.setProperty(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "60000");
            Deserializer<String> keyDeserializer = new StringDeserializer();
            Deserializer<String> valueDeserializer = new StringDeserializer();
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties, keyDeserializer, valueDeserializer);

            consumer.subscribe(Arrays.asList("test_cluster_topic"));

            // KafkaConsumer的assignment（）方法来判定是否分配到了相应的分区，如果为空表示没有分配到分区
            Set<TopicPartition> assignment = consumer.assignment();
            while (assignment.isEmpty()) {
                // 阻塞1秒
                consumer.poll(1000);
                assignment = consumer.assignment();
            }

            // KafkaConsumer 分配到了分区，开始消费
            while (true) {
                // 拉取记录，如果没有记录则柱塞1000ms。
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    String traceId = new String(record.headers().lastHeader("traceId").value());
                    System.out.printf("traceId = %s, offset = %d, key = %s, value = %s%n", traceId, record.offset(), record.key(), record.value());
                }

                // 异步确认提交
                consumer.commitAsync((offsets, exception) -> {
                    if (Objects.isNull(exception)) {
                        // TODO 告警、落盘、重试
                    }
                });
            }
        });

    }


}
