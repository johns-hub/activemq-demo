    package com.atguigu.activemq.persistent;

    import org.apache.activemq.ActiveMQConnectionFactory;

    import javax.jms.*;
    import java.io.IOException;
    import java.util.UUID;

    public class JmsConsumer_Topic_Persistent {

        public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String TOPIC_NAME = "topic.persistent";


        public static void main(String[] args) throws JMSException, IOException {
            // 1.创建连接工厂，按照指定的url地址，采用默认的用户名和密码
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            // 2.通过连接工厂，获取连接并启动访问
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("client-id-1");

            // 3.创建会话
            // 两个参数：第一个叫事务、第二个叫签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 4.创建目的地（具体是队列还是主题）
            Topic topic = session.createTopic(TOPIC_NAME);
            TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "topic-persistent-subscriber");

            connection.start();

            Message message = topicSubscriber.receive();
            while (message instanceof TextMessage) {
                System.out.println("收到的持久化topic：" + ((TextMessage)message).getText());
                message = topicSubscriber.receive(5000L);
            }

            session.close();
            connection.close();

            /**
             * 1. 一定要先运行一次消费者，等于向MQ注册，类似我订阅了这个主题。
             * 2. 然后再运行生产者，此时
             * 3. 无论消费者是否在线，都会接受到，不在线的话，下次连接的时候，会把没有收过的消息都接收下来。
             */
        }
    }
