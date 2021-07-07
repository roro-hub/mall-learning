package com.roro.mall.tiny.mq.apache;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserConsumer.class);

    /**
     * 消费者的组名
     */
    @Value("${suning.rocketmq.conumerGroup}")
    private String consumerGroup;

    /**
     * NameServer 地址
     */
    @Value("${suning.rocketmq.namesrvaddr}")
    private String namesrvAddr;

    /**
     * topic
     */
    @Value("${suning.rocketmq.topic}")
    private String topic;

//    @PostConstruct
    public void consumer() {
        logger.info("init defaultMQPushConsumer");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        try {
            consumer.setNamesrvAddr(namesrvAddr);
            consumer.subscribe(topic, "user-tag");
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        logger.info("消费消息: " + new String(messageExt.getBody()));//输出消息内容
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            });
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
