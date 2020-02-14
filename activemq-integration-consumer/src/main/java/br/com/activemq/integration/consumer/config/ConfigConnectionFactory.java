package br.com.activemq.integration.consumer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.jms.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class ConfigConnectionFactory {

    @Value("${java.naming.provider.url}")
    private String url;

    @Value("${queue.primeiraFila}")
    private String queue;

    public String producer(String msg){
        String retorno = "Messagem: " + msg + " enviada com sucesso!";

        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(queue);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            TextMessage message = session.createTextMessage(msg);

            // Tell the producer to send the message
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println("Caught: " + exception);
        }

        return retorno;

    }

    /**
     * Consome a mensagem da fila e marca como lida.
     * @return
     */
    public List consumer() {
        List<String> retorno = new ArrayList<>();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(queue);

            MessageConsumer consumer = session.createConsumer(destination);
            //Message message = consumer.receive(); //receve uma mensagem de cada vez
            //TextMessage textMessage = (TextMessage) message;
            //retorno = textMessage.getText();

            //sempre escutando a fila
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        TextMessage textMessage = (TextMessage) message;
                        retorno.add(textMessage.getText());
                        System.out.println(textMessage.getText());

                    } catch(Exception exception){
                        System.out.println(exception);
                    }
                }
            });

            consumer.close();
            session.close();
            connection.close();

        } catch(Exception exception){
            System.out.println(exception);
        }

        return retorno;
    }

    /**
     * Apenas verifica se tem mensagem na fila sem consumir ela(retirar da fila e marcar como lida)
     * @return
     */
    public List<String> check(){
        List<String> retorno = new ArrayList<>();
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);

            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(queue);
            QueueBrowser browser = session.createBrowser((Queue) destination);

            Enumeration enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                TextMessage message = (TextMessage) enumeration.nextElement();
                System.out.println("Browse [" + message.getText() + "]");
                retorno.add(message.getText());
            }

            session.close();
            connection.close();

        } catch(Exception exception){
            System.out.println(exception);
        }

        return retorno;

    }

}
