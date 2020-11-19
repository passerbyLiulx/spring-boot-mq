package com.example.mq.config;

import com.example.mq.receiver.DirectListenerReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageDirectListenerConfig {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private DirectListenerReceiver directListenerReceiver;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new  SimpleMessageListenerContainer(cachingConnectionFactory);
        // 监听队列名
        container.setQueueNames(RabbitConfig.DIRECT_LISTENER_QUEUE);
        // 当前消费者数量
        container.setConcurrentConsumers(1);
        // 最大消费者数量
        container.setMaxConcurrentConsumers(1);
        // AcknowledgeMode.NONE：不确认，不会发送任何ack
        // AcknowledgeMode.AUTO：自动确认，就是自动发送ack，除非抛异常
        // AcknowledgeMode.MANUAL：手动确认，手动发送ack
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 设置监听器
        container.setMessageListener(directListenerReceiver);

        return container;
    }
}
