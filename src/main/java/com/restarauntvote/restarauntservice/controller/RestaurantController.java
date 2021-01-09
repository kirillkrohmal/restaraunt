package com.restarauntvote.restarauntservice.controller;

import com.restarauntvote.restarauntservice.model.Restaurant;
import com.restarauntvote.restarauntservice.service.RestaurantService;
import com.restarauntvote.restarauntservice.to.RestaurantTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/restaurants", produces = MediaTypes.UBER_JSON_VALUE)
public class RestaurantController {
    @Autowired
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}/lunch")
    public ResponseEntity<RestaurantTo> getLunchToday(@PathVariable("id") Restaurant restaurant) {
        return new ResponseEntity<> (
                restaurantService.getWithLunchToday(restaurant), HttpStatus.OK);
    }
}
