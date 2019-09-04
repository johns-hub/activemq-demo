package com.atguigu.activemq.embed;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

public class EmbedBroker {

    public static void main(String[] args) throws Exception {
        // ActiveMQ 也支持在VM中通信基于嵌入式的 broker
        /*BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://192.168.1.101:61616");
        brokerService.start();*/

        BrokerService broker = BrokerFactory.createBroker(new URI(
                "broker:(tcp://192.168.1.101:61616)"));
        broker.start();
    }
}
