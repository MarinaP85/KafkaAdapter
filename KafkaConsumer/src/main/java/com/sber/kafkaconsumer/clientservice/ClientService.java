package com.sber.kafkaconsumer.clientservice;

import com.sber.kafkaconsumer.exception.ClientException;
import com.sber.kafkaconsumer.model.MessageDto;
import io.restassured.response.Response;

public interface ClientService {

    /**
     * Выбирает и выполняет выбранный метод AssuredClient на основе данных параметра
     *
     * @param msgDto - все данные запроса в формате MessageDto, должен быть не null
     * @throws ClientException - ошибка выполнения метода AssuredClient,
     *                         либо нужный метод не найден
     */
    Response chooseMethod(MessageDto msgDto) throws ClientException;
}
