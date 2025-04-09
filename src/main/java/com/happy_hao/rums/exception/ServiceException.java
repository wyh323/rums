package com.happy_hao.rums.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final String code;

    public ServiceException(String message) {
        super(message);
        this.code = "500";
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
