package com.hewentian.activemq.p2p;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.hewentian.activemq.util.ConnectionUtil;

/**
 * <p>
 * <b>ProducerP2p.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-29 9:18:18 AM
 * @since JDK 1.8
 */
public class ProducerP2p {
    public static void main(String[] args) throws JMSException {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("hello");
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i = 0; i < 100; i++) {
            TextMessage message = session.createTextMessage("message: " + i);
            producer.send(message);
        }

        System.out.println("100 message sent.");
        ConnectionUtil.close(connection, producer, null);
    }
}
