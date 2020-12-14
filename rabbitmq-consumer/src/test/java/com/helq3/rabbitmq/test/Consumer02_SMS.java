package com.helq3.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Consumer02
 * @Description
 * @Author Helena
 * @Date 2020/12/11 11:58
 */
public class Consumer02_SMS {

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
            connection = connectionFactory.newConnection();

            channel = connection.createChannel();

            //声明一个队列
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            //声明一个交换机
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM,BuiltinExchangeType.FANOUT);

            //绑定交换机和路由器
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");

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
            channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
