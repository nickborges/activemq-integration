package br.com.activemq.integration.consumer.reciver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ListenerComponent {

    @JmsListener(destination = "${topic.primeiroTopico}", id = "primeiroTopicoId", containerFactory = "jmsFactoryTopic")
    public void onReceiverTopic(String str) {
        System.out.println(str);
    }

}