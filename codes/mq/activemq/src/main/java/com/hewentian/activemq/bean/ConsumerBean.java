package com.hewentian.activemq.bean;

import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import com.hewentian.activemq.util.ConnectionUtil;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * <p>
 * <b>ConsumerBean.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-28 8:16:48 PM
 * @since JDK 1.8
 */
public class ConsumerBean {
    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory connectionFactory = ConnectionUtil.getActiveMQConnectionFactory();
        // connectionFactory.setTrustedPackages(Arrays.asList("com.hewentian.activemq.bean"));
        connectionFactory.setTrustAllPackages(true);

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("hello");
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    User user = (User) ((ObjectMessage) message).getObject();
                    System.out.println(user);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

//        ConnectionUtil.close(connection, null, consumer);
    }
}
