package com.atguigu.activemq.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {

    public static final String BROKER_URL = "tcp://192.168.1.204:61616";
    public static final String QUEUE_NAME = "transaction";


    public static void main(String[] args) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            // 1.创建连接工厂，按照指定的url地址，采用默认的用户名和密码
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            // 2.通过连接工厂，获取连接并启动访问
            connection = connectionFactory.createConnection();
            connection.start();

            // 3.创建会话
            // 两个参数：第一个叫事务、第二个叫签收
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            // 4.创建目的地（具体是队列还是主题）
            Queue queue = session.createQueue(QUEUE_NAME);

            // 5.创建消息的生产者
            producer = session.createProducer(queue);

            // 6.通过使用消息生产者，发送3条消息到MQ的队列中
            for (int i = 1; i <= 3; i++) {

                // 7.创建消息
                TextMessage message = session.createTextMessage("transaction-msg-" + i);

                // 8.通过消息生产者，发送给MQ
                producer.send(message);
            }
            // int i = 1 / 0; // 模拟异常，回滚消息
            session.commit(); // 批处理提交
            System.out.println("消息发送完毕！");
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        } finally {

            // 9.关闭资源
            producer.close();
            session.close();
            connection.close();
        }
    }
}
