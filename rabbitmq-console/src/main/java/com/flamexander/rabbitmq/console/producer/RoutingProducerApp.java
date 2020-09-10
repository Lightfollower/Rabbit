package com.flamexander.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RoutingProducerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String routingKeyJava = "Java";
            String routingKeyRuby = "Ruby";
            String routingKeyPython = "Python";
            String messageJava = routingKeyJava + " is boring";
            String messageCSharp = routingKeyRuby + " is boring";
            String messageAngular = routingKeyPython + " is boring";
            int i = 0;
            while (true) {
                channel.basicPublish(EXCHANGE_NAME, routingKeyJava, null, (messageJava + " " + i).getBytes("UTF-8"));
                channel.basicPublish(EXCHANGE_NAME, routingKeyRuby, null, (messageCSharp + " " + i).getBytes("UTF-8"));
                channel.basicPublish(EXCHANGE_NAME, routingKeyPython, null, (messageAngular + " " + i).getBytes("UTF-8"));
                i++;
                Thread.sleep(2000);
            }//            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }
}