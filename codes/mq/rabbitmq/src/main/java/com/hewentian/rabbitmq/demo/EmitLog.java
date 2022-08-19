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
 * <b>EmitLog.java</b> 是 将同一条消息分别分发给多个消费者
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-19 11:01:04 AM
 * @since JDK 1.8
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("input: ");
            String message = scanner.nextLine();
            if (Objects.equals("exit", message)) {
                break;
            }

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }

        scanner.close();
        ConnectionUtil.close(connection, channel);
    }
}
