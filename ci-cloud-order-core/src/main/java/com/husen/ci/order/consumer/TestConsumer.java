package com.husen.ci.order.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/***
 @Author:MrHuang
 @Date: 2019/7/1 15:42
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Component
@Slf4j
@RocketMQMessageListener(topic = "test-topic", selectorExpression = "test-tag", consumerGroup = "CI-CLOUG-MQ")
public class TestConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        log.info("消费信息: " + message);
    }
}

