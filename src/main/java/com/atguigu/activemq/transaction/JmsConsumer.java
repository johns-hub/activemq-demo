    package com.atguigu.activemq.transaction;

    import org.apache.activemq.ActiveMQConnectionFactory;

    import javax.jms.*;

    public class JmsConsumer {

        public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String QUEUE_NAME = "transaction";


        public static void main(String[] args) throws JMSException{
            Connection connection = null;
            Session session = null;
            MessageConsumer consumer = null;

            try {
                // 1.创建连接工厂，按照指定的url地址，采用默认的用户名和密码
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

                // 2.通过连接工厂，获取连接并启动访问
                connection = connectionFactory.createConnection();
                connection.start();

                // 3.创建会话
                // 两个参数：第一个叫事务、第二个叫签收
                // session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

                // 4.创建目的地（具体是队列还是主题）
                Queue queue = session.createQueue(QUEUE_NAME);

                // 5.创建消费者
                consumer = session.createConsumer(queue);

                Message message = consumer.receive(5000L);
                while (message instanceof TextMessage) {
                    System.out.println("消费者接收到消息：" + ((TextMessage) message).getText());
                    // message.acknowledge();
                    message = consumer.receive(5000L);
                }
                session.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                consumer.close();
                session.close();
                connection.close();
            }
        }
    }
