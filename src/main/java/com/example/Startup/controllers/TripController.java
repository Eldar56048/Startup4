package com.example.Startup.controllers;

import com.example.Startup.models.Transport;
import com.example.Startup.models.Trip;
import com.example.Startup.models.User;
import com.example.Startup.repo.TripRepository;
import com.example.Startup.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private TripService tripService;


    @GetMapping
    public String getTripsPage(Model model){
        List<Trip> trips = tripRepository.getAllByDepartureIsGreaterThanOrderByDepartureAsc(LocalDateTime.now());
        model.addAttribute("available",tripRepository.countAllByDepartureIsGreaterThan(LocalDateTime.now()));
        model.addAttribute("trips",trips);
        model.addAttribute("title","Все поездки");
        model.addAttribute("shortTitle","В этой странице находятся все поездки");
        return "trips";
    }

    @GetMapping("/add")
    public String getTripAddPage(Model model){
        model.addAttribute("transports", Transport.values());
        model.addAttribute("title","Добавить поездку");
        model.addAttribute("shortTitle","В этой странице вы можете добавить поездку");
        model.addAttribute("trip",new Trip());
        model.addAttribute("LocalDateTime",LocalDateTime.now());
        return "addtrips";
    }
    @PostMapping("/add")
    public String addingTrip(@AuthenticationPrincipal User user, Trip trip, Model model){
        trip.setUser(user);
        tripRepository.save(trip);
        return "redirect:/trips";
    }

    @GetMapping("/{id}")
    public String getTripById(@PathVariable(name = "id") Long id,Model model){
        Trip trip = tripRepository.getAllById(id);
        if(trip==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct Trip Id");
        }
        model.addAttribute("title","Информация о поездке №"+trip.getId());
        model.addAttribute("shortTitle","В этой странице находиться все информация о ваших поездках");
        model.addAttribute("trip",trip);
        return "single";
    }

    @GetMapping("/{id}/delete")
    public String DeleteUserTripById(@PathVariable(name = "id") Long id,@AuthenticationPrincipal User user, Model model){
        Trip trip = tripRepository.getAllById(id);
        if(trip==null||trip.getUser().getId()!=user.getId()||trip.getDeparture().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provide correct Trip Id");
        }
        tripRepository.delete(trip);
        return "redirect:/users/trips";
    }

    @GetMapping("/search")
    public String getSearchText(@RequestParam String from, @RequestParam String to, Model model){
        List<Trip> trips = tripRepository.findAllByFromCityLikeAndWhereCityLikeAndDepartureGreaterThanOrderByDepartureAsc("%"+from+"%","%"+to+"%",LocalDateTime.now());
        model.addAttribute("available",tripRepository.countAllByDepartureIsGreaterThan(LocalDateTime.now()));
        model.addAttribute("trips",trips);
        model.addAttribute("title","Все поездки "+from+"-"+to);
        model.addAttribute("shortTitle","В этой странице находятся все поездки "+from+"-"+to);
        return "trips";
    }
}
