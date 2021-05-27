package com.sber.clientservice;

import com.sber.kafkamsg.MessageDTO;
import org.json.JSONException;
import io.restassured.response.Response;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class ClientServiceImpl implements ClientService {
    @Override
    public void getRequest(MessageDTO msgDTO) throws JSONException {
        Response response = given().
                headers(msgDTO.getHeaders()).
                when().
                get(msgDTO.getUrl() + msgDTO.getParameters()).
                then().
                extract().
                response();
        System.out.println(response.getStatusCode());
        System.out.println(response.body());
    }

    @Override
    public void postRequest(MessageDTO msgDTO) throws JSONException {
        Response response = given().
                headers(msgDTO.getHeaders()).
                and().
                body(msgDTO.getBody()).
                when().
                post(msgDTO.getUrl() + msgDTO.getParameters()).
                then().
                extract().
                response();
        System.out.println(response.getStatusCode());
    }

    @Override
    public void putRequest(MessageDTO msgDTO) throws JSONException {
        Response response = given().
                headers(msgDTO.getHeaders()).
                and().
                body(msgDTO.getBody()).
                when().
                put(msgDTO.getUrl() + msgDTO.getParameters()).
                then().
                extract().
                response();
        System.out.println(response.getStatusCode());
    }

    @Override
    public void deleteRequest(MessageDTO msgDTO) throws JSONException {
        Response response = given().
                headers(msgDTO.getHeaders()).
                when().
                delete(msgDTO.getUrl() + msgDTO.getParameters()).
                then().
                extract().
                response();
        System.out.println(response.getStatusCode());
    }
}
