package com.roro.mall.tiny.mq.test;

import com.alibaba.fastjson.JSON;
import com.roro.mall.tiny.mq.apache.entity.UserContent;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class TestRocketMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(TestRocketMQProducer.class);

    @Value("${test.rocketmq.producerGroup}")
    private String producerGroup;
    @Value("${test.rocketmq.topic}")
    private String topic;
    @Value("${test.rocketmq.tags}")
    private String tags;
    @Value("${test.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @PostConstruct
    public void producer() {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        try {
            producer.setNamesrvAddr(namesrvAddr);
            producer.start();
            for (int i = 0; i < 2; i++) {
                UserContent user = new UserContent("abc"+i, "pwd"+i);
                String content = JSON.toJSONString(user);
                logger.info("生产发送消息：{}", content);
                Message message = new Message(topic, tags, content.getBytes(StandardCharsets.UTF_8));
                SendResult result = producer.send(message);
                logger.info("生产发送消息id：{} 发送状态：{}", result.getMsgId(), result.getSendStatus());
            }
        } catch (MQClientException e) {
            logger.warn("producer异常，{}", e.toString());
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
