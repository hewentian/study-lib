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
 * <b>EmitLogDirect.java</b> 是 根据routingKey订阅特定的消息
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-19 2:42:32 PM
 * @since JDK 1.8
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        System.out.println("Usage:");
        System.out.println("severity value range: [info, warn, error].");
        System.out.println("message: 'exit' to quit.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("input severity: ");
            String severity = scanner.nextLine();
            System.out.print("input message: ");
            String message = scanner.nextLine();

            if (Objects.equals(message, "exit")) {
                break;
            }

            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }

        scanner.close();
        ConnectionUtil.close(connection, channel);
    }
}
