package com.sber.kafkaconsumer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class MessageDto {
    private String method;
    private String url;
    private String body;
    private HashMap<String, String> headers;
    private String parameters;

}
