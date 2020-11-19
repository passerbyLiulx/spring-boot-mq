package com.example.mq.produce;

import com.example.mq.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TopicProducer implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，
     * 所以不能自动注入
     */
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public TopicProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 如果设置备份队列则不起作用
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 发送消息
     * @param content
     */
    public void sendMsg(String content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_BINGDING_KEY_A, content, correlationId);
        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE, RabbitConfig.TOPIC_BINGDING_KEY_B, content, correlationId);
    }

    /**
     * 回调
     * @param correlationData 消息唯一表示
     * @param ack 确认结果
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("消息发送成功：correlationData({}), ack({}), cause({})", correlationData, ack, cause);
        } else {
            logger.info("消息发送失败：correlationData({}), ack({}), cause({})", correlationData, ack, cause);
        }
    }

    /**
     * 回调
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        logger.info("消息丢失：交换机({}), 路由键({}), 回应码({}), 回应信息({}), 消息({})",
            returnedMessage.getExchange(), returnedMessage.getRoutingKey(), returnedMessage.getReplyCode(),
            returnedMessage.getReplyText(), returnedMessage.getMessage());
    }
}
