package com.hewentian.activemq.util;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * <p>
 * <b>ConnectionUtil.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-18 2:17:28 PM
 * @since JDK 1.8
 */
public class ConnectionUtil {
    public static ActiveMQConnectionFactory getActiveMQConnectionFactory() throws JMSException {
        String userName = ActiveMQConnection.DEFAULT_USER; // null
        String password = ActiveMQConnection.DEFAULT_PASSWORD; // null
        String brokerURL = ActiveMQConnection.DEFAULT_BROKER_URL; // failover://tcp://localhost:61616

        // 如果直接用上面的配置，只有在本地起作用，实际情况是ActiveMQ很少在本地
        userName = "admin";
        password = "admin";
        brokerURL = "tcp://activemq.hewentian.com:61616";

        return new ActiveMQConnectionFactory(userName, password, brokerURL);
    }

    public static Connection getConnection() throws JMSException {
        ConnectionFactory connectionFactory = getActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();

        return connection;
    }

    public static void close(Connection connection, MessageProducer producer, MessageConsumer consumer) {
        if (null != producer) {
            try {
                producer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        if (null != consumer) {
            try {
                consumer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        if (null != connection) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }

}
