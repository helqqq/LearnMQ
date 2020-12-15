package com.helq3.rabbitmq.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Producer04_topics
 * @Description topics 通配符模式 inform.# 可以匹配多个 inform.email.sms  infrom.*  只能匹配一个
 * @Author Helena
 * @Date 2020/12/14 20:56
 */
public class Producer04_topics {

    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#"; //inform.email.sms  两个都可以接收

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);

            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);

            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);

            for(int i = 0; i < 5; i++){
                String message = "send msg" + i + " to routing sms";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms",null,message.getBytes());
            }
            for(int i = 0; i < 5; i++){
                String message = "send msg" + i + " to routing email and sms";
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms.email",null,message.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                if(Objects.nonNull(channel)){
                    channel.close();
                }
                if(Objects.nonNull(connection)){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
