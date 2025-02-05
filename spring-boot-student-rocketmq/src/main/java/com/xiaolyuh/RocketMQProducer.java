package com.xiaolyuh;// package com.xiaolyuh;
// import org.apache.rocketmq.client.producer.DefaultMQProducer;
// import org.apache.rocketmq.common.message.Message;
// import org.apache.rocketmq.remoting.common.RemotingHelper;
// import org.apache.rocketmq.client.producer.SendResult;
// import org.apache.rocketmq.client.exception.MQClientException;
//
// public class RocketMQProducer {
//     public static void main(String[] args) {
//         DefaultMQProducer producer = null;
//         try {
//             // 创建生产者，指定生产者组
//             producer = new DefaultMQProducer("test-producer-group");
//
//             // 设置代理地址（http://代理地址:端口），通过 rocketmq-proxy 与 http2 通信
//             producer.setNamesrvAddr("192.168.1.101:19877");  // 使用 HTTP/2 端口
//
//             // 启动生产者
//             producer.start();
//
//             for (int i = 0; i < 10; i++) {
//                 // 创建消息，指定 Topic、Tag 和消息体内容
//                 String messageContent = "Hello RocketMQ " + i;
//                 Message msg = new Message("test-topic", "TestTag", messageContent.getBytes(RemotingHelper.DEFAULT_CHARSET));
//
//                 // 发送消息
//                 SendResult sendResult = producer.send(msg);
//
//                 // 打印发送结果
//                 System.out.println("Send Result: " + sendResult);
//             }
//
//         } catch (MQClientException e) {
//             System.err.println("Client exception: " + e.getErrorMessage());
//         } catch (Exception e) {
//             e.printStackTrace();
//         } finally {
//             // 确保生产者关闭
//             if (producer != null) {
//                 producer.shutdown();
//             }
//         }
//     }
// }
