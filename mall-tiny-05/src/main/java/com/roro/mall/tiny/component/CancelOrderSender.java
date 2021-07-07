package com.roro.mall.tiny.component;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CancelOrderSender {

    private static Logger logger = LoggerFactory.getLogger(CancelOrderSender.class);

    @Value("$(mall.rocketmq.topic)")
    private String topic;
    @Value("$(mall.rocketmq.tags)")
    private String tags;
    @Value("$(mall.rocketmq.producerGroup)")
    private String producerGroup;
    @Value("$(mall.rocketmq.namesrvAddr)")
    private String namesrvAddr;

    public void sendMessage(Long orderId, int delayTimeLevel) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
            Message message = new Message(topic, tags, orderId.toString().getBytes(StandardCharsets.UTF_8));
            // 延时级别，只支持固定延时时间 "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
            message.setDelayTimeLevel(delayTimeLevel);
            // 发送消息，只要不抛异常就是成功。
            SendResult result = producer.send(message);
            logger.info("MessageId: {} SendStatus: {}" + result.getMsgId(), result.getSendStatus());
        } catch (Exception e) {
            logger.warn("MQ异常，{}", e.toString());
            throw new Exception(e);
        }
        producer.shutdown();
    }
}
