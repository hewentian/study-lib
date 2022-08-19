package com.hewentian.activemq.replyto;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.hewentian.activemq.util.ConnectionUtil;

/**
 * <p>
 * <b>ConsumerReplyTo.java</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-02-03 1:33:57 PM
 * @since JDK 1.8
 */
public class ConsumerReplyTo {

    public static void main(String[] args) throws JMSException {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("hello");
        MessageConsumer consumer = session.createConsumer(destination);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("receive from producer: " + textMessage.getText());

                    // reply to
                    MessageProducer producer = session.createProducer(message.getJMSReplyTo());
                    producer.send(session.createTextMessage("hello " + textMessage.getText()));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
