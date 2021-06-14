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

    private MessageDto msgDTO = null;

//    private final ClientService clientService;
//
//    @Autowired
//    public MyKafkaListener(ClientService clientService) {
//        this.clientService = clientService;
//    }

    @KafkaListener(topics = "Message1", containerFactory = "msgKafkaListenerContainerFactory", groupId = "msgConsumer")
    public void messageListener(@Payload MessageDto msgDTO) {
        //прослушиваем заданную тему в Кафке
        //если получили сообщение, пишем его в msgDTO
        //и сбрасываем счетчик latch,чтобы отследить момент,
        //когда сообщение было успешно получено
        this.msgDTO = msgDTO;
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public MessageDto getMsgDTO() {
        return msgDTO;
    }

}
