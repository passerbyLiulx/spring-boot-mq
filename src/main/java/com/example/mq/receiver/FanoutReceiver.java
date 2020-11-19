package com.example.mq.receiver;

import com.example.mq.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE_A)
    public void processA(String content) {
        logger.info("接收处理队列A当中的消息： " + content);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE_B)
    public void processB(String content) {
        logger.info("接收处理队列B当中的消息： " + content);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.FANOUT_QUEUE_C)
    public void processC(String content) {
        logger.info("接收处理队列C当中的消息： " + content);
    }
}
