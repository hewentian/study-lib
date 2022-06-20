package com.hewentian.rabbitmq.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * <p>
 * <b>ConnectionUtil.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-18 2:17:28 PM
 * @since JDK 1.8
 *
 */
public class ConnectionUtil {
	public static Connection getConnection() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("rabbitmq.hewentian.com");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("hewentian");
		connectionFactory.setPassword("12345678");

		Connection connection = connectionFactory.newConnection();
		return connection;
	}

	public static void close(Connection conn, Channel channel) {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}

		try {
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
