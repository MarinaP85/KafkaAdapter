package com.sber.kafkaconsumer.listener;

import com.sber.kafkaconsumer.clientservice.ClientService;
import com.sber.kafkaconsumer.clientservice.KafkaMethod;
import com.sber.kafkaconsumer.model.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;


@EnableKafka
@Component
public class MyKafkaListener {

    private final CountDownLatch latch = new CountDownLatch(1);

    private final ClientService clientService;

    @Autowired
    public MyKafkaListener(ClientService clientService) {
        this.clientService = clientService;
    }

    @KafkaListener(topics = "Message1", containerFactory = "msgKafkaListenerContainerFactory", groupId = "msgConsumer")
    public void messageListener(@Payload MessageDTO msgDTO) {
//        System.out.println(headers);
//        System.out.println(msgDTO.getMethod());
//        System.out.println(msgDTO.getUrl());
//        System.out.println(msgDTO.getHeaders());
//        System.out.println(msgDTO.getBody());
//        System.out.println(msgDTO.getParameters());

        chooseMethod(msgDTO);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    private void chooseMethod(MessageDTO msgDTO) {
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
