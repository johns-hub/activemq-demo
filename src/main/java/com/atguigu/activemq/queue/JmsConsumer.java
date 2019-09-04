    package com.atguigu.activemq.queue;

    import org.apache.activemq.ActiveMQConnectionFactory;

    import javax.jms.*;
    import java.io.IOException;

    public class JmsConsumer {

        // public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String BROKER_URL = "tcp://192.168.1.101:61616";
        public static final String QUEUE_NAME = "queue";


        public static void main(String[] args) throws JMSException, IOException {
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

            // 5.创建消费者
            MessageConsumer consumer = session.createConsumer(queue);
           /* while (true) {
                // 6.接收消息
                // TextMessage message = (TextMessage)consumer.receive();
                // 同步阻塞方式(receive())
                // 订阅者或接收者调用receive方法来接收消息，receive方法在能够接收到消息之前（或超时之前）将一直阻塞。
                TextMessage message = (TextMessage)consumer.receive(4000L);
                if (message == null) break;
                System.out.println("***消息者接收到消息：" + message.getText());
            }

            // 7.关闭资源
            consumer.close();
            session.close();
            connection.close();*/

            // 异步非阻塞方式（监听器onMessage）
            // 订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听者
            // 当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法
           consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage msg = (TextMessage)message;
                        try {
                            System.out.println("接收到queue消息：" + msg.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            System.in.read();

            consumer.close();
            session.close();
            connection.close();
        }
    }
