package com.sber.kafkaproducer.runner;

import com.sber.kafkaproducer.model.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;

@Component
public class KafkaProducerRunner implements CommandLineRunner {
    @Autowired
    private KafkaTemplate<String, MessageDto> kafkaMessageTemplate;

    @Override
    public void run(String... args) {
        //отправка тестового сообщение, чтобы проверить работу Кафки
        HashMap<String, String> headers = new HashMap<>();
        headers.put("h1", "test1");
        headers.put("h2", "test2");

        MessageDto msgTest = new MessageDto();
        msgTest.setMethod("GET");
        msgTest.setBody("test");
        msgTest.setHeaders(headers);
        msgTest.setUrl("/test");
        msgTest.setParameters("param");
        ListenableFuture<SendResult<String, MessageDto>> msgFuture = kafkaMessageTemplate.send("Message1", "msg", msgTest);
        msgFuture.addCallback(System.out::println, System.err::println);

    }
}
