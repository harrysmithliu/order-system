package com.harry.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // —— 命名约定（最小）——
    public static final String ORDER_EVENTS_QUEUE = "order.events";
    public static final String ORDER_TOPIC_EXCHANGE = "order.topic";
    public static final String ORDER_ROUTING_KEY_PATTERN = "order.*"; // 如：order.created / order.canceled

    /** 队列（持久化） */
    @Bean
    public Queue orderEventsQueue() {
        return QueueBuilder.durable(ORDER_EVENTS_QUEUE).build();
    }

    /** Topic 交换机（持久化） */
    @Bean
    public TopicExchange orderTopicExchange() {
        return ExchangeBuilder.topicExchange(ORDER_TOPIC_EXCHANGE).durable(true).build();
    }

    /** 绑定：order.events <- order.topic(order.*) */
    @Bean
    public Binding orderEventsBinding(Queue orderEventsQueue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(orderEventsQueue)
                .to(orderTopicExchange)
                .with(ORDER_ROUTING_KEY_PATTERN);
    }

    /** JSON 消息转换器（用你项目里已有的 Jackson 配置即可） */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /** RabbitTemplate 使用 JSON 转换器 */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
