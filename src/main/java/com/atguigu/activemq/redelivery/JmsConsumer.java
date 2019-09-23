    package com.atguigu.activemq.redelivery;

    import org.apache.activemq.ActiveMQConnectionFactory;
    import org.apache.activemq.RedeliveryPolicy;

    import javax.jms.*;
    import java.io.IOException;

    public class JmsConsumer {

        public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String QUEUE_NAME = "queue-redelivery";


        public static void main(String[] args) throws JMSException, IOException {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            redeliveryPolicy.setMaximumRedeliveries(3);
            connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage msg = (TextMessage)message;
                        try {
                            System.out.println("接收到queue-redelivery消息：" + msg.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // 开启事务，但是没有提交，
            // 重复运行6次以后，消息进入死信队列

            // session.commit();

            System.in.read();

            consumer.close();
            session.close();
            connection.close();
        }
    }