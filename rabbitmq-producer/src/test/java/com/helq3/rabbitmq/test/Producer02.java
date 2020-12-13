package com.helq3.rabbitmq.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Producer01
 * @Description
 * @Author Helena
 * @Date 2020/12/10 11:04
 */
public class Producer02 {
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";

    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";

    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

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
            /**
             * String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments
             * queue:队列
             * durable:是否持久化
             * exclusive:
             * autoDelete:
             * arguments:
             **/
            //声明两个队列,如果队列在mq 中没有则要创建
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            /**
             * String exchange, BuiltinExchangeType type
             * exchange: 交换机的名称
             * type: 交换机的类型
             *      fanout:对应的rabbitmq的工作模式是 publish/subscribe
             *      direct:对应Routing模式
             *      topic:Topics模式
             *      headers:Headers模式
             **/
            //声明一个交换机
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);
            /**
             * String queue, String exchange, String routingKey
             * queue:队列名称
             * exchange:交换机名称
             * routingKey:交换机根据路由key的值将消息转发到指定的队列中，在发布订阅模式中协调维空字符串
             **/
            //交换机和队列的绑定
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");

            //发送消息
            for(int i = 0; i < 5; i++){
                String message = "hello world";
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null, message.getBytes());
                System.out.println("send msg to mq:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {

            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
