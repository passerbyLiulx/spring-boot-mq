package com.example.mq;

import com.example.mq.produce.DirectListenerProducer;
import com.example.mq.produce.DirectProducer;
import com.example.mq.produce.FanoutProducer;
import com.example.mq.produce.TopicProducer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
class SpringBootMqApplicationTests {

    @Autowired
    private DirectProducer directProducer;

    @Autowired
    private FanoutProducer fanoutProducer;

    @Autowired
    private TopicProducer topicProducer;

    @Autowired
    private DirectListenerProducer directListenerProducer;

    @Test
    void DirectExchangeTest() {
        // 当输入会触发重试机制
        //directProducer.sendMsg("error");

        directProducer.sendMsg("ribbitMQ消息队列");
    }

    @Test
    void FanoutExchangeTest() {
        Random random = new Random();
        random.ints().limit(10).forEach(i -> {
            fanoutProducer.sendMsg("ribbitMQ消息队列--" + i);
        });
    }

    @Test
    void TopicExchangeTest() {
        List<String> stringList = Arrays.asList("a", "b", "c", "d", "e","f", "g");
        stringList.stream().forEach(i -> {
            topicProducer.sendMsg("ribbitMQ消息队列--" + i);
        });
    }

    @Test
    void DirectListenerExchangeTest() {
        directListenerProducer.sendMsg("监听ribbitMQ消息队列");
    }

}
