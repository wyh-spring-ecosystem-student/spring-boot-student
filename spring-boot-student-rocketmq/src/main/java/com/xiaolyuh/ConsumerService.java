package com.xiaolyuh;// package com.xiaolyuh;

import java.nio.charset.StandardCharsets;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "test-consumer-group", tag = "*")
public class ConsumerService implements RocketMQListener {

    @Override
    public ConsumeResult consume(MessageView messageView) {
        String message = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        System.out.println("Received message: " + messageView + "  ::: " + message);
        return ConsumeResult.SUCCESS;
    }

}
