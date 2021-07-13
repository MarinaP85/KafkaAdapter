package com.sber.kafkaconsumer.clientservice;

import com.sber.kafkaconsumer.exception.ClientException;
import com.sber.kafkaconsumer.model.MessageDto;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Locale;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private AssuredClient assuredClient;

    public Response chooseMethod(MessageDto msgDto) throws ClientException {
        try {
            Method method = assuredClient.getClass().getMethod(
                    msgDto.getMethod().toLowerCase(Locale.ROOT), MessageDto.class);
            return (Response) method.invoke(assuredClient, msgDto);
        } catch (Exception e) {
            throw new ClientException("Ошибка работы клиента: " + e.getMessage());
        }
    }
}
