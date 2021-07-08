//package com.roro.mall.tiny.mq.aliyun;
//
//import com.aliyun.openservices.ons.api.*;
//
//import java.util.Date;
//import java.util.Properties;
//
///**
// * 延时发送
// */
//public class ProducerDelayTest {
//
//    public static void main(String[] args) {
//        Properties properties = new Properties();
//        // AccessKey ID阿里云身份验证，在阿里云RAM控制台创建。
//        properties.put(PropertyKeyConst.AccessKey, "XXX");
//        // AccessKey Secret阿里云身份验证，在阿里云RAM控制台创建。
//        properties.put(PropertyKeyConst.SecretKey, "XXX");
//        // 设置TCP接入域名，进入消息队列RocketMQ版控制台实例详情页面的接入点区域查看。
//        properties.put(PropertyKeyConst.NAMESRV_ADDR,
//                "XXX");
//
//        Producer producer = ONSFactory.createProducer(properties);
//        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
//        producer.start();
//        Message msg = new Message(
//                // 您在消息队列RocketMQ版控制台创建的Topic。
//                "Topic",
//                // Message Tag,可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在消息队列RocketMQ版服务器过滤。
//                "tag",
//                // Message Body可以是任何二进制形式的数据，消息队列RocketMQ版不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式。
//                "Hello MQ".getBytes());
//        // 设置代表消息的业务关键属性，请尽可能全局唯一。
//        // 以方便您在无法正常收到消息情况下，可通过控制台查询消息并补发。
//        // 注意：不设置也不会影响消息正常收发。
//        msg.setKey("ORDERID_100");
//        try {
//            // 延时消息，单位毫秒（ms），在指定延迟时间（当前时间之后）进行投递，例如消息在3秒后投递。
//            long delayTime = System.currentTimeMillis() + 3000;
//
//            // 设置消息需要被投递的时间。
//            msg.setStartDeliverTime(delayTime);
//
//            SendResult sendResult = producer.send(msg);
//            // 同步发送消息，只要不抛异常就是成功。
//            if (sendResult != null) {
//                System.out.println(new Date() + " Send mq message success. Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
//            }
//        } catch (Exception e) {
//            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理。
//            System.out.println(new Date() + " Send mq message failed. Topic is:" + msg.getTopic());
//            e.printStackTrace();
//        }
//        // 在应用退出前，销毁Producer对象。
//        // 注意：如果不销毁也没有问题。
//        producer.shutdown();
//    }
//}
