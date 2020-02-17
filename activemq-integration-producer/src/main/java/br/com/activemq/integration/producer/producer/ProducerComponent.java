package br.com.activemq.integration.producer.producer;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerComponent {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${queue.primeiraFila}")
    private String queue;

    @Value("${topic.primeiroTopico}")
    private String topic;

    public void runQueue(String msg){
        jmsTemplate.convertAndSend(queue, msg);
    }

    public void runTopic(String msg){
        jmsTemplate.convertAndSend( new ActiveMQTopic(topic), msg);
    }

}
