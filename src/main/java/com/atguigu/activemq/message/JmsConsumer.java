    package com.atguigu.activemq.message;

    import org.apache.activemq.ActiveMQConnectionFactory;

    import javax.jms.*;
    import java.io.IOException;

    public class JmsConsumer {

        public static final String BROKER_URL = "tcp://192.168.1.204:61616";
        public static final String TOPIC_NAME = "message";


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
            Topic topic = session.createTopic(TOPIC_NAME);

            // 5.创建消费者
            MessageConsumer consumer = session.createConsumer(topic);

            // 异步非阻塞方式（监听器onMessage）
            // 订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听者
            // 当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法
           consumer.setMessageListener((message)->{
               try {
                   if (message instanceof MapMessage) {
                       MapMessage mapMessage = (MapMessage) message;
                       System.out.println("消费者接收到消息：" + mapMessage.getString("k1"));
                       System.out.println("消费者接收到消息属性：" + mapMessage.getStringProperty("c01"));
                   }
                   if (message instanceof BytesMessage) {
                       BytesMessage bytesMessage = (BytesMessage) message;
                       byte[] bytes = new byte[1024];
                       bytesMessage.readBytes(bytes);
                       System.out.println(bytes[0]);
                       System.out.println(bytes[1]);
                       System.out.println(bytes[2]);
                   }
                   if (message instanceof ObjectMessage) {
                       ObjectMessage objectMessage = (ObjectMessage) message;
                       String data = (String)objectMessage.getObject();
                       System.out.println(data);
                   }
                   if (message instanceof StreamMessage) {
                       StreamMessage streamMessage = (StreamMessage) message;
                       byte[] bytes = new byte[streamMessage.getIntProperty("len")];
                       streamMessage.readBytes(bytes);
                       System.out.println(new String(bytes));
                   }

               } catch (Exception e) {
                    e.printStackTrace();
               }
           });

            System.in.read();

            consumer.close();
            session.close();
            connection.close();
        }
    }
