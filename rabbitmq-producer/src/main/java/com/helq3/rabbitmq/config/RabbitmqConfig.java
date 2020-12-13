package com.helq3.rabbitmq.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitmqConfig
 * @Description rabbitmq配置类
 * @Author Helena
 * @Date 2020/12/11 15:31
 */
@Configuration
public class RabbitmqConfig {
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    public static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    public static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    //声明交换机
    public Exchange EXCHANGE_TOPICS_INFORM(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }
    //声明队列

    //绑定交换机和队列
}
