package com.guide.ex.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

// 화면(@Controller) 요청에서 발생한 예외를 에러 페이지로 보여준다
@ControllerAdvice(annotations = Controller.class)
@Log4j2
public class PageExceptionAdvice {

    @ExceptionHandler({
            EntityNotFoundException.class,
            ObjectRetrievalFailureException.class,
            NoSuchElementException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception e, Model model) {
        log.warn("not found: {}", e.getMessage());
        model.addAttribute("message", "요청한 페이지나 게시글을 찾을 수 없습니다.");
        return "error/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(IllegalArgumentException e, Model model) {
        log.warn("bad request: {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }
}
