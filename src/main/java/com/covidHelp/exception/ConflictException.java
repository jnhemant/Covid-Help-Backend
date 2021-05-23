package com.covidHelp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Record already exists")
public class ConflictException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ConflictException(String errMessage){
        super(errMessage);
    }
}
