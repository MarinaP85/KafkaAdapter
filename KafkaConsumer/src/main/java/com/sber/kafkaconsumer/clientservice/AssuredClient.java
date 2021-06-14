package com.sber.kafkaconsumer.clientservice;

import com.sber.kafkaconsumer.model.MessageDto;
import io.restassured.response.Response;

public interface AssuredClient {

    /**
     * Отправляет запрос GET
     *
     * @param msgDto - все данные запроса в формате MessageDto, должен быть не null
     */
    Response get(MessageDto msgDto);

    /**
     * Отправляет запрос POST
     *
     * @param msgDto - все данные запроса в формате MessageDto, должен быть не null
     */
    Response post(MessageDto msgDto);

    /**
     * Отправляет запрос PUT
     *
     * @param msgDto - все данные запроса в формате MessageDto, должен быть не null
     */
    Response put(MessageDto msgDto);

    /**
     * Отправляет запрос DELETE
     *
     * @param msgDto - все данные запроса в формате MessageDto, должен быть не null
     */
    Response delete(MessageDto msgDto);
}
