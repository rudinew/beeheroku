package com.bee.web.exceptions;

/**
 * Created by Rudoman on 21.07.2017.
 */
public class PersonAccessForbiddenException extends RuntimeException {
    @Override
    public String getMessage() {
        return "У вас немає доступу до даної особової карти" ; //super.getMessage();
    }
}
