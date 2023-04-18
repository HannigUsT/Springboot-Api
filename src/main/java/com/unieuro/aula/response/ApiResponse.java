package com.unieuro.aula.response;

public class ApiResponse {
    private String status;
    private String message;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
