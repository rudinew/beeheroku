package com.bee.web.exceptions;
/*
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;*/

//@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Person")  // 404
public class PersonNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Немає даної особової карти" ; //super.getMessage();
    }
}
