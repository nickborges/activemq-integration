package br.com.activemq.integration.consumer.controller;

import br.com.activemq.integration.consumer.config.ConfigConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActiveMqIntegrationConsumer {

    @Autowired
    ConfigConnectionFactory factory;

    @PostMapping("/producer")
    public String execute(@RequestBody String mensagem){
        return factory.producer(mensagem);
    }

    @GetMapping("/consumer")
    public List execute(){
        return factory.consumer();
    }

    @GetMapping("/check")
    public List check(){
        return factory.check();
    }

}
