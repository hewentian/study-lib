package com.hewentian.activemq.pub2sub;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.hewentian.activemq.util.ConnectionUtil;

/**
 * <p>
 * <b>ProducerPub2sub.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-30 9:12:28 AM
 * @since JDK 1.8
 */
public class ProducerPub2sub {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 这里创建的是 Topic
        Destination destination = session.createTopic("helloTopic");
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
