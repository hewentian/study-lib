package com.hewentian.rabbitmq.demo;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import com.hewentian.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * <p>
 * <b>EmitLogTopic.java</b> 是 订阅者/发布者，需用到模式匹配
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-19 4:38:03 PM
 * @since JDK 1.8
 */
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        System.out.println("Usage:");
        System.out.println("routingKey: like 'kern.critical'");
        System.out.println("message: 'exit' to quit.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("input routingKey: ");
            String routingKey = scanner.nextLine();
            System.out.print("input message: ");
            String message = scanner.nextLine();

            if (Objects.equals(message, "exit")) {
                break;
            }

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }

        scanner.close();
        ConnectionUtil.close(connection, channel);
    }
}
