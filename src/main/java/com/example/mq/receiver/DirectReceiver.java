package com.example.mq.receiver;

import com.example.mq.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.DIRECT_QUEUE)
public class DirectReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(String content) {
        if ("error".equals(content)) {
            logger.error("消息消费失败");
            // 这里抛出检查期异常不会进行重试，抛出检查期异常时重试多少次还是会抛出异常
            // 消息被拒绝之后不会进行重试
            // 抛出运行时异常会进行重试
            throw new RuntimeException("消息消费失败");
        }
        logger.info("接收处理队列当中的消息： " + content);
    }
}
