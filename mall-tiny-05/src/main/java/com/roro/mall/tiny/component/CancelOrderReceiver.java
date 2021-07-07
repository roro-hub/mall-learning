package com.roro.mall.tiny.component;

import com.roro.mall.tiny.service.OmsPortalOrderService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderReceiver {

    private static Logger logger = LoggerFactory.getLogger(CancelOrderReceiver.class);

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Value("$(mall.rocketmq.topic)")
    private String topic;
    @Value("$(mall.rocketmq.tags)")
    private String tags;
    @Value("$(mall.rocketmq.consumerGroup)")
    private String consumerGroup;
    @Value("$(mall.rocketmq.namesrvAddr)")
    private String namesrvAddr;

    public void handle(Long orderId) throws Exception {
        logger.info("receive delay message orderId: {}", orderId);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            consumer.subscribe(topic, tags);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                for (MessageExt messageExt : list) {
                    String content = new String(messageExt.getBody());
                    portalOrderService.cancelOrder(orderId);
                    logger.info("消息消费：{}", content);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (Exception e) {
            logger.warn("消息消费异常：{}", e.toString());
            throw new Exception(e);
        }
    }
}
