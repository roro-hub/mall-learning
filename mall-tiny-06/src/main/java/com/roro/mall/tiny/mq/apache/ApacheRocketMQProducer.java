package com.roro.mall.tiny.mq.apache;

import com.alibaba.fastjson.JSON;
import com.roro.mall.tiny.mq.apache.entity.User;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApacheRocketMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(ApacheRocketMQProducer.class);

    public static void main(String[] args) throws MQClientException {
        logger.info("mqproduder start");
        DefaultMQProducer producer = new DefaultMQProducer("apacheProducer");
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("rmq-instance");
        producer.start();
        try {
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setLoginName("abc" + i);
                user.setPwd(String.valueOf(i));
                Message message = new Message("apacheTopic", "user-tag", JSON.toJSONString(user).getBytes());
                logger.info("生产者发送消息:" + JSON.toJSONString(user));
                SendResult result = producer.send(message);
                logger.info("生产者发送消息id：{} 发送状态：{}", result.getMsgId(), result.getSendStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}
