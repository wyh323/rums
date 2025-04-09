package com.happy_hao.rums.exception;

import com.happy_hao.rums.common.Result;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Hidden
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result serviceException(ServiceException e) {
        return Result.error().status(e.getCode()).message(e.getMessage());
    }
}
