package com.roro.mall.tiny.mq.spring;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "${rocketmq.producer.topic}", consumerGroup = "${rocketmq.producer.group}")
public class RocketMQConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConsumer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(String msg){
        logger.info("收到消息:" + msg);
    }
}
