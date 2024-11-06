package com.example.hairsalon.responses;

import java.io.Serializable;

public class TransactionStatusResponse implements Serializable {

    private String status;
    private String message;
    private String data;

    public TransactionStatusResponse() {
    }

    public TransactionStatusResponse(String status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
