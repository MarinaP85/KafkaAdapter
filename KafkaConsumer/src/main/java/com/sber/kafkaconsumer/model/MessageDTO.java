package com.sber.kafkaconsumer.model;

import java.util.HashMap;

public class MessageDTO {
    private String method;
    private String url;
    private String body;
    private HashMap<String, String> headers;
    private String parameters;

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getParameters() {
        return parameters;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
