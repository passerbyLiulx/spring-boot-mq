package com.example.mq.receiver;

import com.example.mq.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class DirectListenerReceiver implements ChannelAwareMessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 消息的唯一性ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            logger.info("消息：" + message.toString());
            logger.info("消息来自：" + message.getMessageProperties().getConsumerQueue());
            // 手动确认
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 拒绝策略
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }
}
