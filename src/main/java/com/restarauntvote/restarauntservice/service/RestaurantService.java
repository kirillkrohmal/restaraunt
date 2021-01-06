package com.restarauntvote.restarauntservice.service;

import com.restarauntvote.restarauntservice.model.Restaurant;
import com.restarauntvote.restarauntservice.to.RestaurantTo;
import org.springframework.stereotype.Repository;


@Repository
public interface RestaurantService {
    RestaurantTo getWithLunchToday(Restaurant restaurant);
}
