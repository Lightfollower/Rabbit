package com.flamexander.rabbitmq.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoutingProducerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";
    private static List<String> themes = new ArrayList<>();

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String routingKeyJava = "Java";
            String routingKeyRuby = "Ruby";
            String routingKeyPython = "Python";
            themes.add(routingKeyJava);
            themes.add(routingKeyRuby);
            themes.add(routingKeyPython);
            String messageJava = routingKeyJava + " is boring";
            String messageCSharp = routingKeyRuby + " is boring";
            String messageAngular = routingKeyPython + " is boring";
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
                    switch (strings[0]) {
                        case "add":
                            themes.add(strings[1]);
                            break;
                        case "del":
                            for (int i = 0; i < themes.size(); i++) {
                                if(strings[1].equals(themes.get(i))) {
                                    themes.remove(i);
                                    break;
                                }
                            }
                        break;
                    }
                }
            }).start();
            int i = 0;
            while (true) {
                for (int a = 0; a < themes.size(); a++) {
                    channel.basicPublish(EXCHANGE_NAME, themes.get(a), null, (messageJava + " " + i).getBytes("UTF-8"));
//                    channel.basicPublish(EXCHANGE_NAME, routingKeyRuby, null, (messageCSharp + " " + i).getBytes("UTF-8"));
//                    channel.basicPublish(EXCHANGE_NAME, routingKeyPython, null, (messageAngular + " " + i).getBytes("UTF-8"));
                }
                i++;
                Thread.sleep(2000);
            }//            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }
}