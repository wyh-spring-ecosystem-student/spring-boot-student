package com.xiaolyuh;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootStudentKafkaApplicationTests {

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @Test
    public void testSyncKafkaSend() throws Exception {
        // 同步发送测试
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test_cluster_topic", "key-" + i, "value-" + i);
            // 同步发送，这里我们还可以指定发送到那个分区，还可以添加header
            kafkaProducer.send(producerRecord, new KafkaCallback<>(producerRecord)).get(50, TimeUnit.MINUTES);
        }

        System.out.println("ThreadName::" + Thread.currentThread().getName());
    }

    @Test
    public void testAsyncKafkaSend() {
        // 异步发送测试
        for (int i = 0; i < 100; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test_cluster_topic2", "key-" + i, "value-" + i);
            // 异步发送，这里我们还可以指定发送到那个分区，还可以添加header
            kafkaProducer.send(producerRecord, new KafkaCallback<>(producerRecord));
        }

        System.out.println("ThreadName::" + Thread.currentThread().getName());
        // 记得刷新，否则消息有可能没有发出去
        kafkaProducer.flush();
    }
}

/**
 * 异步回调函数，该方法会在 Producer 收到 ack 时调用，当Exception不为空表示发送消息失败。
 *
 * @param <K>
 * @param <V>
 */
class KafkaCallback<K, V> implements Callback {
    private final ProducerRecord<K, V> producerRecord;

    public KafkaCallback(ProducerRecord<K, V> producerRecord) {
        this.producerRecord = producerRecord;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        System.out.println("ThreadName::" + Thread.currentThread().getName());
        if (Objects.isNull(exception)) {
            System.out.println(metadata.partition() + "-" + metadata.offset() + ":::" + producerRecord.key() + "=" + producerRecord.value());
        }

        if (Objects.nonNull(exception)) {
            // TODO  告警，消息落库从发
        }
    }
}

