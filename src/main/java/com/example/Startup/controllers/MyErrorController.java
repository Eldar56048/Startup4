package com.example.Startup.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    public MyErrorController() {}

    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {

            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("title","Ошибка 404");
                model.addAttribute("shortTitle","Ошибка 404 — это ситуация, когда вместо веб-страницы посетителю показывается сообщение о том, что ее не существует");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("title","Ошибка 500");
                model.addAttribute("shortTitle","Ошибка 500 — это класс состояния протокола HTTP, который означает, что операция/запрос пользователя выполнены неудачно и виноват в этом сам сервер.");
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
