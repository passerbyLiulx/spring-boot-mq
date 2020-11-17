package com.example.mq;

import com.example.mq.msg.MsgProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootMqApplicationTests {

    @Autowired
    private MsgProducer msgProducer;

    @Test
    void MqTest1() {
        msgProducer.sendMsg("不做大哥好多年！");
    }

    @Test
    void MqTest2() {
        for (int i=0; i<10; i++) {
            msgProducer.sendMsg("不做大哥好多年！");
        }
    }
}
