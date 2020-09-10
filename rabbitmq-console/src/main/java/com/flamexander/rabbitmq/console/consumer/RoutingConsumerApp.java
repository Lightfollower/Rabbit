package com.flamexander.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class RoutingConsumerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";
    private List<String> subscribes = new ArrayList<>();

    public RoutingConsumerApp() throws IOException, TimeoutException {
        addSubscribe("Java");
        addSubscribe("Ruby");
        addSubscribe("Python");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        for (int i = 0; i < subscribes.size(); i++) {
            channel.queueBind(queueName, EXCHANGE_NAME, subscribes.get(i));
            channel.queueBind(queueName, EXCHANGE_NAME, subscribes.get(i));
            channel.queueBind(queueName, EXCHANGE_NAME, subscribes.get(i));
        }
//        System.out.println(" [*] Waiting for messages with routing key (" + routingKeyJava + "):");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
//            channel.abort();
//            channel.queueBind(queueName, EXCHANGE_NAME, "Java");

//            subscribes.clear();
//            for
//            channel.un(queueName, EXCHANGE_NAME, subscribes.get(1));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
//        System.out.println("finished");
//        Scanner scanner = new Scanner(System.in);
//        while (true){
//            scanner.next();
//            subscribes.remove(0);
//        }
    }

    public void addSubscribe(String newSubscribe) {
        subscribes.add(newSubscribe);
    }

    public static void main(String[] argv) throws Exception {
        RoutingConsumerApp routingConsumerApp = new RoutingConsumerApp();
//        String routingKeyJava = "Java";
//        String routingKeyRuby = "Ruby";
//        String routingKeyPython = "Python";


//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("localhost");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
//        String queueName = channel.queueDeclare().getQueue();
//
//        List<String> subscribes = new ArrayList<>();
//        String routingKeyJava = "Java";
//        String routingKeyRuby = "Ruby";
//        String routingKeyPython = "Python";
//        channel.queueBind(queueName, EXCHANGE_NAME, routingKeyJava);
//        channel.queueBind(queueName, EXCHANGE_NAME, routingKeyRuby);
//        channel.queueBind(queueName, EXCHANGE_NAME, routingKeyPython);
//        System.out.println(" [*] Waiting for messages with routing key (" + routingKeyJava + "):");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
//
//        };
//        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
//        });
    }
}