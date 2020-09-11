package com.geekbrains.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RoutingConsumerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";
    private List<String> subscribes = new ArrayList<>();
    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;
    private String queueName;

    public RoutingConsumerApp() throws IOException, TimeoutException {
        fillSubscribes();

        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        queueName = channel.queueDeclare().getQueue();

        for (int i = 0; i < subscribes.size(); i++) {
            channel.queueBind(queueName, EXCHANGE_NAME, subscribes.get(i));
        }

        startConsoleListener();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    private void fillSubscribes() {
        addSubscribe("Java");
        addSubscribe("Ruby");
        addSubscribe("Python");
    }

    public void addSubscribe(String newSubscribe) {
        subscribes.add(newSubscribe);
    }

    private void startConsoleListener() {
        new Thread(() -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String input = null;
                try {
                    input = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] strings = input.split(" ");
                System.out.println(strings.length);
                for (String s :
                        strings) {
                    System.out.println(s);
                }
                switch (strings[0]) {
                    case "add":
                        try {
                            channel.queueBind(queueName, EXCHANGE_NAME, strings[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        subscribes.add(strings[1]);
                        subscribes.forEach(System.out::println);
                        break;
                    case "del":
                        for (int i = 0; i < subscribes.size(); i++) {
                            if (strings[1].equals(subscribes.get(i))) {
                                try {
                                    channel.queueUnbind(queueName, EXCHANGE_NAME, strings[1]);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                subscribes.remove(i);
                                break;
                            }
                        }
                        break;
                }
            }
        }).start();
    }

    public static void main(String[] argv) throws Exception {
        new RoutingConsumerApp();
    }
}