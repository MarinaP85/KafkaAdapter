package com.sber.kafkaconsumer.clientservice;

import com.sber.kafkaconsumer.model.MessageDto;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class AssuredClientImpl implements AssuredClient {
    @Override
    public Response get(MessageDto msgDto) {
        return given().
                headers(msgDto.getHeaders()).
                when().
                get(msgDto.getUrl() + msgDto.getParameters()).
                then().
                extract().
                response();
    }

    @Override
    public Response post(MessageDto msgDto) {
        return given().
                headers(msgDto.getHeaders()).
                and().
                body(msgDto.getBody()).
                when().
                post(msgDto.getUrl() + msgDto.getParameters()).
                then().
                extract().
                response();
    }

    @Override
    public Response put(MessageDto msgDto) {
        return given().
                headers(msgDto.getHeaders()).
                and().
                body(msgDto.getBody()).
                when().
                put(msgDto.getUrl() + msgDto.getParameters()).
                then().
                extract().
                response();
    }

    @Override
    public Response delete(MessageDto msgDto) {
        return given().
                headers(msgDto.getHeaders()).
                when().
                delete(msgDto.getUrl() + msgDto.getParameters()).
                then().
                extract().
                response();
    }
}
