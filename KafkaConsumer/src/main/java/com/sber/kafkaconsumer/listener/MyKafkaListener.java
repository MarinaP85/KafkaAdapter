package com.sber.kafkaconsumer.listener;

import com.sber.kafkaconsumer.model.MessageDto;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


@EnableKafka
@Component
public class MyKafkaListener {

    private final CountDownLatch latch = new CountDownLatch(1);

    private MessageDto msgDto = null;

    @KafkaListener(topics = "Message1", containerFactory = "msgKafkaListenerContainerFactory",
            groupId = "msgConsumer")
    public void messageListener(@Payload MessageDto msgDto) {
        //прослушиваем заданную тему в Кафке
        //если получили сообщение, пишем его в msgDTO
        //и сбрасываем счетчик latch,чтобы отследить момент,
        //когда сообщение было успешно получено
        this.msgDto = msgDto;
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public MessageDto getMsgDto() {
        return msgDto;
    }

}
