package com.atguigu.activemq.schedule;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;

public class JmsProduce {

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String QUEUE_NAME = "queue-schedule";


    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue(QUEUE_NAME);

        MessageProducer producer = session.createProducer(queue);

        for (int i = 1; i <= 3; i++) {

            // 延迟3秒发送，每次间隔4秒，重复发送3次
            TextMessage message = session.createTextMessage("queue-schedule-msg-" + i);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3 * 1000);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 4 * 1000);
            message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, 3);

            producer.send(message);
        }

        producer.close();
        session.close();
        connection.close();
        System.out.println("消息发送完毕！");
    }
}
