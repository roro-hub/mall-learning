package com.roro.mall.tiny.service.impl;

import com.roro.mall.tiny.common.api.CommonResult;
import com.roro.mall.tiny.component.CancelOrderSender;
import com.roro.mall.tiny.dto.OrderParam;
import com.roro.mall.tiny.service.OmsPortalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    private static Logger LOGGER = LoggerFactory.getLogger(OmsPortalOrderServiceImpl.class);

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Value("${rocketmq.mall.delayTimeLevel}")
    private String delayTimeLevel;

    @Override
    public CommonResult generateOrder(OrderParam orderParam) throws Exception {
        //todo 执行一系类下单操作，具体参考mall项目
        LOGGER.info("process generateOrder");
        //下单完成后开启一个延迟消息，用于当用户没有付款时取消订单（orderId应该在下单后生成）
        Long orderId = System.currentTimeMillis();
        LOGGER.info("orderId: {}", orderId);
        sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null, "下单成功");
    }

    @Override
    public void cancelOrder(Long orderId) {
        //todo 执行一系类取消订单操作，具体参考mall项目
        LOGGER.info("process cancelOrder orderId:{}", orderId);
    }

    private void sendDelayMessageCancelOrder(Long orderId) throws Exception {
        //获取订单超时时间，假设为60分钟
        //发送延迟消息
        int delayLevel = Integer.parseInt(delayTimeLevel);
        cancelOrderSender.sendMessage(orderId, delayLevel);
    }
}
