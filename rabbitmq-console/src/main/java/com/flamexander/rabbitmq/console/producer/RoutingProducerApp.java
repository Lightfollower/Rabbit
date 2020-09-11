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

public class RoutingProducerApp {
    private final String EXCHANGE_NAME = "topic_exchange";
    private List<String> themes = new ArrayList<>();

    public RoutingProducerApp() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            fillThemes();

            startConsoleListener();
            int i = 0;
            while (true) {
                for (int a = 0; a < themes.size(); a++) {
                    channel.basicPublish(EXCHANGE_NAME, themes.get(a), null, (themes.get(a) + " is boring. " + i).getBytes("UTF-8"));
                }
                i++;
                Thread.sleep(2000);
            }
        }
    }

    private void fillThemes() {
        themes.add("Java");
        themes.add("Ruby");
        themes.add("Python");
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
    }
}