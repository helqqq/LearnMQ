package com.helq3.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Consumer04_topics_sms
 * @Description 消费方-topics
 * @Author Helena
 * @Date 2020/12/15 19:05
 */
public class Consumer04_topics_sms {
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";

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
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);

            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();
                    String message = new String(body,"utf-8");
                    System.out.println("sms receive msg : " + message);
                }
            };
            //监听队列
            channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
