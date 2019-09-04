package com.atguigu.activemq.message;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String TOPIC_NAME = "message";


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
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        // 6.通过使用消息生产者，发送3条消息到MQ的队列中
        for (int i = 1; i <= 1; i++) {
            // 7.创建消息
            MapMessage message = session.createMapMessage();
            message.setString("k1", "MapMessage");
            message.setStringProperty("c01", "vip");
            messageProducer.send(message); // 8.通过消息生产者，发送给MQ

            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(new byte[]{1,2});
            messageProducer.send(bytesMessage);

            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(new String("ObjectMessage"));
            messageProducer.send(objectMessage);

            StreamMessage streamMessage = session.createStreamMessage();
            byte[] bytes = "StreamMessage".getBytes();
            streamMessage.setIntProperty("len", bytes.length);
            streamMessage.writeBytes(bytes);
            messageProducer.send(streamMessage);
        }

        // 9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("消息发送完毕！");
    }
}
