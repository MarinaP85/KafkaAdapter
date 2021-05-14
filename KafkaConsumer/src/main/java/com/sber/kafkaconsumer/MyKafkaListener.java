package com.sber.kafkaconsumer;

import com.sber.clientservice.ClientService;
import com.sber.clientservice.KafkaMethod;
import com.sber.kafkamsg.MessageDTO;
import com.sber.clientservice.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@EnableKafka
@Component
public class MyKafkaListener {

    private final ClientService clientService;

    @Autowired
    public MyKafkaListener(ClientService clientService) {
        this.clientService = clientService;
    }

    @KafkaListener(topics = "Message1", containerFactory = "msgKafkaListenerContainerFactory", groupId = "msgConsumer")
    public void messageListener(@Payload MessageDTO msgDTO, @Headers MessageHeaders headers) {
        System.out.println(headers);
        System.out.println(msgDTO.getMethod());
        System.out.println(msgDTO.getUrl());
        System.out.println(msgDTO.getHeaders());
        System.out.println(msgDTO.getBody());
        System.out.println(msgDTO.getParameters());

        Method[] methods = clientService.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(KafkaMethod.class)) {
                if (method.getAnnotation(KafkaMethod.class).method().equals(msgDTO.getMethod())) {
                    try {
                        method.invoke(msgDTO);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
