package com.husen.ci;

import com.husen.ci.mq.MqUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/***
 @Author:MrHuang
 @Date: 2019/7/8 18:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMq {

    @Test
    public void sendMsg() {
        SendResult sendResult = MqUtils.syncSend("topic", "tags", "helllo");
        System.out.println(sendResult);
    }

}
