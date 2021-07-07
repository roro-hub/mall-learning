package com.roro.mall.tiny.mq.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestRocketMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TestRocketMQConsumer.class);

    @Value("${test.rocketmq.consumerGroup}")
    private String consumerGroup;
    @Value("${test.rocketmq.tags}")
    private String tags;
    @Value("${test.rocketmq.topic}")
    private String topic;
    @Value("${test.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void consumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        logger.info("consumer init");
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            consumer.subscribe(topic, tags);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                for (MessageExt messageExt : list) {
                    String content = new String(messageExt.getBody());
                    logger.info("消息消费：{}", content);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
