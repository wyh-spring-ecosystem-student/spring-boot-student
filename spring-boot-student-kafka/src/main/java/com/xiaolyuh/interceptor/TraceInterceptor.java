package com.xiaolyuh.interceptor;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author olafwang
 * 链路ID
 */
public class TraceInterceptor implements ProducerInterceptor<String, String> {
    private int errorCounter = 0;
    private int successCounter = 0;

    /**
     * 最先调用，读取配置信息，只调用一次
     */
    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println(JSON.toJSONString(configs));
    }

    /**
     * 它运行在用户主线程中，在消息序列化和计算分区之前调用，这里最好不小修改topic 和分区参数，否则会出一些奇怪的现象。
     *
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {

        Headers headers = new RecordHeaders();
        headers.add("traceId", UUID.randomUUID().toString().getBytes(Charset.forName("UTF8")));
        // 修改消息
        return new ProducerRecord<>(record.topic(), record.partition(), record.key(), record.value(), headers);
    }

    /**
     * 该方法会在消息从 RecordAccumulator 成功发送到 Kafka Broker 之后，或者在发送过程 中失败时调用。
     * 并且通常都是在 producer 回调逻辑触发之前调用。
     * onAcknowledgement 运行在 producer 的 IO 线程中，因此不要在该方法中放入很重的逻辑，否则会拖慢 producer 的消息 发送效率。
     *
     * @param metadata
     * @param exception
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (Objects.isNull(exception)) {
            // TODO  出错了
        }
    }

    /**
     * 关闭 interceptor，主要用于执行一些资源清理工作，只调用一次
     */
    @Override
    public void close() {
        System.out.println("==========close============");
    }
}