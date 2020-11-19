package com.example.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * DirectExchange：直连交换机，根据消息携带的路由键，将消息转发给对应的队列
     */
    public static final String DIRECT_EXCHANGE = "directExchange";

    /**
     * FanoutExchange：扇形交换机，接收到消息后会将消息转发到所有队列
     */
    public static final String FANOUT_EXCHANGE = "fanoutExchange";

    /**
     * TopicExchange：主题交换机，根据消息携带的路由键和交换机与队列绑定键的规则，将消息转发给对应的队列
     * 规则：*（星号）：表示一个字符必须出现；#（井号）：表示任意数量的字符
     */
    public static final String TOPIC_EXCHANGE = "topicExchange";


    /**
     * 绑定key，交换机绑定队列时需要指定
     */
    public static final String DIRECT_BINGDING_KEY = "directRoutingKey";
    public static final String DIRECT_LISTENER_BINGDING_KEY = "directListenerRoutingKey";
    public static final String TOPIC_BINGDING_KEY_A = "topicRoutingKey.A";
    public static final String TOPIC_BINGDING_KEY_B = "topicRoutingKey.#";

    /**
     * 队列名称
     */
    public static final String DIRECT_QUEUE = "directQueue";
    public static final String DIRECT_LISTENER_QUEUE = "directListenerQueue";
    public static final String FANOUT_QUEUE_A = "fanoutQueueA";
    public static final String FANOUT_QUEUE_B = "fanoutQueueB";
    public static final String FANOUT_QUEUE_C = "fanoutQueueC";
    public static final String TOPIC_QUEUE_A = "topicQueueA";
    public static final String TOPIC_QUEUE_B = "topicQueueB";

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        //rabbitTemplate.setMandatory(true);

        /* 确认消息已发送到交换机(Exchange)
         1. 消息推送到server，但是在server里找不到交换机
         2. 消息推送到server，找到交换机了，但是没找到队列
         3. 消息推送成功
         都会触发此回调函数
         */
        /*rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            logger.info("ConfirmCallback:     " + "相关数据：" + correlationData);
            logger.info("ConfirmCallback:     " + "确认情况：" + ack);
            logger.info("ConfirmCallback:     " + "原因：" + cause);
        });*/
        /* 确认消息已发送到队列(Queue)
         消息推送到server，找到交换机了，但是没找到队列
         触发此回调函数
         */
        /*rabbitTemplate.setReturnsCallback(returnCallback -> {
            logger.info("ReturnCallback:     " + "消息：" + returnCallback.getMessage());
            logger.info("ReturnCallback:     " + "回应码：" + returnCallback.getReplyCode());
            logger.info("ReturnCallback:     " + "回应信息：" + returnCallback.getReplyText());
            logger.info("ReturnCallback:     " + "交换机：" + returnCallback.getExchange());
            logger.info("ReturnCallback:     " + "路由键：" + returnCallback.getRoutingKey());
        });*/
        return rabbitTemplate;
    }

    /**
     * 直连交换机
     */
    @Bean
    public DirectExchange directExchange() {
        // name：名称；durable：持久化；autoDelete：自动删除
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    /**
     * 扇形交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        // name：名称；durable：持久化；autoDelete：自动删除
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }

    /**
     * 主题交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        // name：名称；durable：持久化；autoDelete：自动删除
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    /**
     * 队列
     */
    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue directListenerQueue() {
        return new Queue(DIRECT_LISTENER_QUEUE, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue fanoutQueueA() {
        return new Queue(FANOUT_QUEUE_A, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue fanoutQueueB() {
        return new Queue(FANOUT_QUEUE_B, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue fanoutQueueC() {
        return new Queue(FANOUT_QUEUE_C, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue topicQueueA() {
        return new Queue(TOPIC_QUEUE_A, true);
    }

    /**
     * 队列
     */
    @Bean
    public Queue topicQueueB() {
        return new Queue(TOPIC_QUEUE_B, true);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DIRECT_BINGDING_KEY);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding directListenerBinding() {
        return BindingBuilder.bind(directListenerQueue()).to(directExchange()).with(DIRECT_LISTENER_BINGDING_KEY);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding fanoutBindingA() {
        return BindingBuilder.bind(fanoutQueueA()).to(fanoutExchange());
    }

    /**
     * 绑定
     */
    @Bean
    public Binding fanoutBindingB() {
        return BindingBuilder.bind(fanoutQueueB()).to(fanoutExchange());
    }

    /**
     * 绑定
     */
    @Bean
    public Binding fanoutBindingC() {
        return BindingBuilder.bind(fanoutQueueC()).to(fanoutExchange());
    }

    /**
     * 绑定
     */
    @Bean
    public Binding topicBindingA() {
        return BindingBuilder.bind(topicQueueA()).to(topicExchange()).with(TOPIC_BINGDING_KEY_A);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding topicBindingB() {
        return BindingBuilder.bind(topicQueueB()).to(topicExchange()).with(TOPIC_BINGDING_KEY_B);
    }

}
