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
 * <b>ProducerReplyTo.java</b> 是 测试回复消息的例子。
 * <ul>
 * <li>首先创建两个队列，一个为 hello, 另一个为 helloReply；</li>
 * <li>程序 {@linkplain com.hewentian.activemq.replyto.ProducerReplyTo ProducerReplyTo} 向队列 hello
 * 发送消息"activeMQ"，并在该消息中设置JMSReplyTo为helloReply，让消费者在接收到该消息时，根据JMSReplyTo来向helloReply队列发送消息；</li>
 * <li>程序 {@linkplain com.hewentian.activemq.replyto.ConsumerReplyTo ConsumerReplyTo}
 * 监听队列"hello"中的消息，并在接收到消息的过程中，根据消息的JMSReplyTo指定的队列，发送回复消息；</li>
 * <li>另外，{@linkplain com.hewentian.activemq.replyto.ProducerReplyTo ProducerReplyTo} 中有一个消费者监听队列helloReply。</li>
 * </ul>
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2018-02-03 1:33:28 PM
 * @since JDK 1.8
 */
public class ProducerReplyTo {
    public static void main(String[] args) throws JMSException {
        Connection connection = ConnectionUtil.getConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("hello");
        Destination replyTo = session.createQueue("helloReply");

        Message message = session.createTextMessage("activeMQ");
        message.setJMSReplyTo(replyTo);

        MessageProducer producer = session.createProducer(destination);
        producer.send(message);

        // 下面创建一个消费者，接收 replyTo 这个队列里的消息
        MessageConsumer consumer = session.createConsumer(replyTo);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("receive from replyTo: " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
