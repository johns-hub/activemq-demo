package com.atguigu.activemq.persistent;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String QUEUE_NAME = "queue.persistent";


    public static void main(String[] args) throws JMSException {
        // 1.创建连接工厂，按照指定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

        // 2.通过连接工厂，获取连接并启动访问
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // 3.创建会话
        // 两个参数：第一个叫事务、第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4.创建目的地（具体是队列还是主题）
        Queue queue = session.createQueue(QUEUE_NAME);

        // 5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        // messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // 非持久化
        // messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT); // 持久化消息

        // 6.通过使用消息生产者，发送3条消息到MQ的队列中
        for (int i = 1; i <= 3; i++) {
            // 7.创建消息
            // TextMessage message = session.createTextMessage("queue-not-persistent-msg-" + i);
            TextMessage message = session.createTextMessage("queue-persistent-msg-" + i);

            // 8.通过消息生产者，发送给MQ
            messageProducer.send(message);
        }

        // 9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("消息发送完毕！");
    }
}
