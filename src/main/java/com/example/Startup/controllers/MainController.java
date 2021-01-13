package com.example.Startup.controllers;

import com.example.Startup.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @GetMapping("/")
    public String getMainPage(Model model){
        model.addAttribute("title","О нас");
        model.addAttribute("shortTitle","На этой странице вы можете найти информацию о нас");
        return "about";
    }

    /*@GetMapping("/trips")
    public String getTripsPage(){
        return "trips";
    }*/

    @GetMapping("reg")
    public String getRegPage(Model model){
        model.addAttribute("user",new User());
        return "registration";
    }


}
