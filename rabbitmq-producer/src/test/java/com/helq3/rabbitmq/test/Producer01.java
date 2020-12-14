package com.helq3.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Producer01
 * @Description
 * @Author Helena
 * @Date 2020/12/10 11:04
 */
public class Producer01 {
    private static final String QUEUE = "guard";
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
            //创建新连接
            connection = connectionFactory.newConnection();
            //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            channel = connection.createChannel();
            //声明队列,如果队列在mq 中没有则要创建
            channel.queueDeclare(QUEUE,true,false,false,null);
            //发送消息
            String message = "hello world";
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("send msg to mq:" + message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                if(Objects.nonNull(channel)) {
                    channel.close();
                }
                if(Objects.nonNull(connection)) {
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
