package com.starWars.challenge.model;

public class DefaultAPIResponse {

    private int status;
    private String message;

    public DefaultAPIResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
