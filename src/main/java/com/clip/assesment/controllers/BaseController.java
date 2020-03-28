package com.clip.assesment.controllers;

import ch.qos.logback.classic.Logger;
import com.clip.assesment.dto.ExceptionDTO;
import com.clip.assesment.exceptions.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseController {

    protected Logger logger = (Logger) LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDTO methodArgumentNotValidExceptionHandler(Exception e) {
        logger.error("Method Argument Not Valid Exception", e);
        return new ExceptionDTO("There was an error during the server process. Argument not valid.");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionDTO exceptionHandler(Exception e) {
        logger.error("Unhandled Exception encountered, replying with 500 - Internal Server Error", e);
        return new ExceptionDTO("There was an error during the server process");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionDTO notFoundExceptionHandler(Exception e) {
        logger.error("Not found Exception encountered", e);
        return new ExceptionDTO(e.getMessage());
    }

}
