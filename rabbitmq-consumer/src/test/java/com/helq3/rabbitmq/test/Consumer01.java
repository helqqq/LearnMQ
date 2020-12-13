package com.helq3.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Consumer01
 * @Description TODO
 * @Author Helena
 * @Date 2020/12/11 11:58
 */
public class Consumer01 {

    private static final String QUEUE = "guard";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE,true,false,false,null);

            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                /**
                 * @Description 接收到消息后此方法将被调用
                 * @Author Helena
                 * @Date 2020/12/11 14:19
                 * @param consumerTag 消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
                 * @param envelope 信封，通过envelope
                 * @param properties 消息属性
                 * @param body 消息内容
                 **/
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String message = new String(body,"utf-8");
                    System.out.println("receive:" + message);

                }
            };
            //监听队列
            //String queue, boolean autoAck, Consumer callback
            channel.basicConsume(QUEUE,true,defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
