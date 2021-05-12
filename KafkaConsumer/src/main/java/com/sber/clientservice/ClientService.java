package com.sber.clientservice;

import com.sber.kafkamsg.MessageDTO;
import org.json.JSONException;

public interface ClientService {
    @KafkaMethod(method = "GET")
    void getRequest(MessageDTO msgDTO) throws JSONException;

    @KafkaMethod(method = "POST")
    void postRequest(MessageDTO msgDTO) throws JSONException;

    @KafkaMethod(method = "PUT")
    void putRequest(MessageDTO msgDTO) throws JSONException;

    @KafkaMethod(method = "DELETE")
    void deleteRequest(MessageDTO msgDTO) throws JSONException;

}
