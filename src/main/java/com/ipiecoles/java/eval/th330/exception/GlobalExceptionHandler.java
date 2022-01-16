package com.ipiecoles.java.eval.th330.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleENFE(EntityNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMsg", e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMATME(MethodArgumentTypeMismatchException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("type","danger");
        modelAndView.addObject("message","Problème dans le type de données d'un ou plusieurs parametre(s)");
        return modelAndView;
    }

    @ExceptionHandler(ConflitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleConflitException(ConflitException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("type","danger");
        modelAndView.addObject("message",e.getMessage());
        return modelAndView;
    }
}
