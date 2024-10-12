package com.example.hairsalon.components.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiException extends RuntimeException {
    private int code;
    private String message;
    private Object data;
    public ApiException(HttpStatus status, String overrideMessage, Object data){
        super();
        this.code = status.value();
        this.data = data;
        this.message = overrideMessage;
    }
    public ApiException(HttpStatus status, String overrideMessage){
        super();
        this.code = status.value();
        this.data = null;
        this.message = overrideMessage;
    }
    public ApiException(HttpStatus status, Object data){
        super();
        this.code = status.value();
        this.data = data;
        this.message = status.getReasonPhrase();
    }
    public ApiException(HttpStatus status){
        super();
        this.code = status.value();
        this.data = null;
        this.message = status.getReasonPhrase();
    }
}