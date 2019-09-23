package com.atguigu.activemq.redelivery;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String QUEUE_NAME = "queue-redelivery";


    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue(QUEUE_NAME);

        MessageProducer producer = session.createProducer(queue);

        for (int i = 1; i <= 3; i++) {
            TextMessage message = session.createTextMessage("queue-redelivery-msg-" + i);
            producer.send(message);
        }

        producer.close();
        session.close();
        connection.close();
        System.out.println("消息发送完毕！");
    }
}
