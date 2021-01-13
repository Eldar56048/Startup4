package com.example.Startup.controllers;
import com.example.Startup.models.Role;
import com.example.Startup.models.Trip;
import com.example.Startup.models.User;
import com.example.Startup.repo.TripRepository;
import com.example.Startup.repo.UserRepository;
import com.example.Startup.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public String registration(User user, Model model){
        if(userRepository.findByUsername(user.getUsername())!=null){
            model.addAttribute("errorUsername","Email занят");
        }
        if(model.getAttribute("errorUsername")!=null){
            model.addAttribute("user",user);
            return "registration";
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/{id}")
    public String getUser(@AuthenticationPrincipal User user,@PathVariable(value = "id") Long id, Model model){
        if(user.getId()==id){
            model.addAttribute("title","Информация о вас");
            model.addAttribute("shortTitle","Здесь находится подробная информация о вас");
        }
        else{
            user = userRepository.getById(id);
            if(user==null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct User Id");
            }
            model.addAttribute("title","Информация о "+user.getSurname()+" "+user.getName());
            model.addAttribute("shortTitle","Здесь находится подробная информация о "+user.getSurname()+" "+user.getName());
        }
        model.addAttribute("allTrips",tripRepository.countAllByUser(user));
        model.addAttribute("user",user);
        return "profile";
    }

    @GetMapping("/{id}/trips")
    public String getUserTrips(@AuthenticationPrincipal User user,@PathVariable(value = "id") Long id, Model model){
        if(user.getId()==id) {
            model.addAttribute("title", "Все ваши поездки");
            model.addAttribute("shortTitle", "Ниже находиться информация о всех ваших поездках");
        }
        else {
            user = userRepository.getById(id);
            model.addAttribute("title", "Все поездки "+user.getSurname()+" "+user.getName());
            model.addAttribute("shortTitle", "Ниже находиться информация о всех поездках "+user.getSurname()+" "+user.getName());
        }
        List<Trip> activeTrips = tripRepository.getAllByUserAndDepartureGreaterThan(user,LocalDateTime.now());
        List<Trip> nonActiveTrips = tripRepository.getAllByUserAndDepartureLessThan(user,LocalDateTime.now());
        if(activeTrips==null||nonActiveTrips==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct User Id");
        }
        model.addAttribute("activeTrips",activeTrips);
        model.addAttribute("nonActiveTrips",nonActiveTrips);
        return "myTrips";
    }

}
