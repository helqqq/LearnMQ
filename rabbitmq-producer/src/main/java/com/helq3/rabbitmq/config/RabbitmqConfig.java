package com.helq3.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
    public static final String QUEUE_INFORM_PERSON = "queue_inform_person";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    public static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    public static final String ROUTINGKEY_SMS = "inform.#.sms.#";
    public static final String ROUTINGKEY_PERSON = "inform.#.person.#";

    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public TopicExchange EXCHANGE_TOPICS_INFORM(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_TOPICS_INFORM,true,false);
    }
    //声明email队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    //声明sms队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //声明队列
    @Bean(QUEUE_INFORM_PERSON)
    public Queue QUEUE_INFORM_PERSON(){ return new Queue(QUEUE_INFORM_PERSON); }

    @Bean
    public Queue personQueue(){
        Queue queue = new Queue("persons",true,false,false);
        queue.setShouldDeclare(true);
        return queue;
    }

    //绑定交换机和email队列
    @Bean
    public Binding BINDING_ROUTINGKEY_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                            @Qualifier(EXCHANGE_TOPICS_INFORM) TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL);
    }
    //绑定交换机和sms队列
    @Bean
    public Binding BINDING_ROUTINGKEY_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                          @Qualifier(EXCHANGE_TOPICS_INFORM) TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS);
    }

    //绑定队列和路由器
    @Bean
    public Binding bindingPersonExchange(Queue personQueue,
                                         TopicExchange topicExchange){
        return BindingBuilder.bind(personQueue).to(topicExchange).with("inform.person");
    }
}
