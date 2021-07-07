package com.roro.mall.tiny.mq.apache;

import com.alibaba.fastjson.JSON;
import com.roro.mall.tiny.mq.apache.entity.UserContent;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserProducer.class);

    /**
     * 生产者的组名
     */
    @Value("${suning.rocketmq.producerGroup}")
    private String producerGroup;

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
    public void produder() {
        logger.info("produder init");
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(namesrvAddr);
        try {
            producer.start();
            for (int i = 0; i < 2; i++) {
                UserContent userContent = new UserContent(String.valueOf(i), "abc" + i);
                String jsonstr = JSON.toJSONString(userContent);
                logger.info("发送消息:" + jsonstr);
                Message message = new Message(topic, "user-tag", jsonstr.getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult result = producer.send(message);
                logger.info("发送响应：MsgId:" + result.getMsgId() + "，发送状态:" + result.getSendStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }
    }
}
