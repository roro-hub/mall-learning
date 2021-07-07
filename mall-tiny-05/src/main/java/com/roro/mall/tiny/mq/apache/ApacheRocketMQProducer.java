package com.roro.mall.tiny.mq.apache;

import com.alibaba.fastjson.JSON;
import com.roro.mall.tiny.mq.apache.entity.User;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RocketMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQProducer.class);

    public static void main(String[] args) throws MQClientException {
        logger.info("mqproduder start");
        DefaultMQProducer producer = new DefaultMQProducer("test-group");
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("rmq-instance");
        logger.info("mqproduder producer start before");
        producer.start();
        logger.info("mqproduder producer start end");
        try {
            for (int i = 0; i < 2; i++) {
                User user = new User();
                user.setLoginName("abc" + i);
                user.setPwd(String.valueOf(i));
                Message message = new Message("apacheTopic", "user-tag", JSON.toJSONString(user).getBytes());
                logger.info("生产者发送消息:" + JSON.toJSONString(user));
                logger.info("mqproduder producer send before");
                producer.send(message);
                logger.info("mqproduder producer send before");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();
    }
}
