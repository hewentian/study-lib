package com.hewentian.rabbitmq.demo;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import com.hewentian.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

/**
 * <p>
 * <b>NewTask.java</b> 是 将一个队列的消息分发给多个消费者，顺序分发。例如：有5条消息，有2个消费者，
 * 这样消费者1可能会得到消息：1,3,5；而消费者2可能会得到消息：2，4
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-18 7:43:57 PM
 * @since JDK 1.8
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("input: ");
            String message = scanner.nextLine();
            if (Objects.equals("exit", message)) {
                break;
            }

            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }

        scanner.close();
        ConnectionUtil.close(connection, channel);
    }
}
