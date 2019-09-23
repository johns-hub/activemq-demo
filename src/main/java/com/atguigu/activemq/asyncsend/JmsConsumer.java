    package com.atguigu.activemq.asyncsend;

    import org.apache.activemq.ActiveMQConnectionFactory;

    import javax.jms.*;
    import java.io.IOException;

    public class JmsConsumer {

        public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String QUEUE_NAME = "queue-async-send";


        public static void main(String[] args) throws JMSException, IOException {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage msg = (TextMessage)message;
                        try {
                            System.out.println("接收到queue-async-send消息：" + msg.getText());
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
