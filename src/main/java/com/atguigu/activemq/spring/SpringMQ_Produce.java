package com.atguigu.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Service
public class SpringMQ_Produce {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        SpringMQ_Produce produce = (SpringMQ_Produce)ctx.getBean("springMQ_Produce");

        produce.jmsTemplate.send((session)->{
            // TextMessage message = session.createTextMessage("spring-queue-msg");
            TextMessage message = session.createTextMessage("spring-topic-msg");
            return message;
        });

        System.out.println("发送完毕！");
    }
}
