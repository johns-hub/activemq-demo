package com.atguigu.activemq.asyncsend;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.UUID;

public class JmsProduce {

    private static final Logger LOG = LoggerFactory.getLogger(JmsProduce.class);

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String QUEUE_NAME = "queue-asyncsend";


    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        // 使用异步发送
        connectionFactory.setUseAsyncSend(true);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue(QUEUE_NAME);

        MessageProducer messageProducer = session.createProducer(queue);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        for (int i = 1; i <= 3; i++) {

            TextMessage message = session.createTextMessage("queue-asyncsend-msg-" + i);
            message.setJMSMessageID("order-" + UUID.randomUUID().toString().substring(0, 5));

            final String msgId = message.getJMSMessageID();

            // 异步回调通知
            ((ActiveMQMessageProducer)messageProducer).send(message, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    LOG.info(msgId + " send success");
                }

                @Override
                public void onException(JMSException exception) {
                    LOG.error(msgId + " send exception", exception);
                }
            });
        }

        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("消息发送完毕！");
    }
}
