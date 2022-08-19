package com.hewentian.activemq.p2p;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.hewentian.activemq.util.ConnectionUtil;

/**
 * <p>
 * <b>ConsumerP2p.java</b> 是 消费者，如果同时启动两个消费者监听同一个队列，则它们会平分该队列里的消息
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-01-29 9:18:46 AM
 * @since JDK 1.8
 */
public class ConsumerP2p {
    public static void main(String[] args) throws JMSException {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("hello");

        MessageConsumer consumer1 = session.createConsumer(destination);
        consumer1.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    String messageText = ((TextMessage) message).getText();
                    System.out.println("consumer1 receive: " + messageText);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        MessageConsumer consumer2 = session.createConsumer(destination);
        consumer2.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    String messageText = ((TextMessage) message).getText();
                    System.out.println("consumer2 receive: " + messageText);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

//        ConnectionUtil.close(null, null, consumer1);
//        ConnectionUtil.close(connection, null, consumer2);
    }
}
