package com.hewentian.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.hewentian.activemq.util.ConnectionUtil;

/**
 * <p>
 * <b>DeliveryModeDemo.java</b> 是 DeliveryMode 的示例
 * <ul>
 * <li>队列中 DeliveryMode.PERSISTENT 的消息，在 Broker 重启后，还在；</li>
 * <li>队列中 DeliveryMode.NON_PERSISTENT 的消息，在 Broker 重启后，不在；</li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-30 12:00:47 PM
 * @since JDK 1.8
 */
public class DeliveryModeDemo {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue("hello");
        MessageProducer producer = session.createProducer(destination);

        // send NON_PERSISTENT message
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        TextMessage message = session.createTextMessage("NON_PERSISTENT message");
        producer.send(message);
        System.out.println("NON_PERSISTENT message sent.");

        // send PERSISTENT message
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        message = session.createTextMessage("PERSISTENT message");
        producer.send(message);
        System.out.println("PERSISTENT message sent.");

        System.out.println("2 message sent.");

        ConnectionUtil.close(connection, producer, null);
    }
}
