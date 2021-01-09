package com.restarauntvote.restarauntservice.controller;

import com.restarauntvote.restarauntservice.exceptions.RestaurantNotFoundException;
import com.restarauntvote.restarauntservice.model.Restaurant;
import com.restarauntvote.restarauntservice.service.ChoiceService;
import com.restarauntvote.restarauntservice.userdetails.UserPrincipal;
import com.restarauntvote.restarauntservice.util.ChoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/choice", produces = MediaTypes.UBER_JSON_VALUE)
public class ChoiceController {
    private ChoiceService choiceService;

    @Autowired
    public ChoiceController(ChoiceService choiceService) {
        this.choiceService = choiceService;
    }

    @GetMapping
    public String current() throws RestaurantNotFoundException {
        return "index.html";
    }

    @PostMapping("/{id}")
    public ResponseEntity<Restaurant> choice(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("id") Restaurant restaurant) {
        ChoiceStatus choiceStatus = choiceService.choiceStatus(userPrincipal.getUser(), restaurant);
        return new ResponseEntity<> (choiceStatus.getChoice().getRestaurant(), choiceStatus.isCreated() ? HttpStatus.CREATED : HttpStatus.OK);
    }
}
