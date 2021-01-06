package com.restarauntvote.restarauntservice.service;

import com.restarauntvote.restarauntservice.exceptions.RestaurantNotFoundException;
import com.restarauntvote.restarauntservice.model.Choice;
import com.restarauntvote.restarauntservice.model.Lunch;
import com.restarauntvote.restarauntservice.model.Restaurant;
import com.restarauntvote.restarauntservice.model.User;
import com.restarauntvote.restarauntservice.util.ChoiceStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface ChoiceService {
    Optional<Choice> getForUserAndDate(Long userId, LocalDate date);

    ChoiceStatus save(User user, Restaurant restaurant);

    ChoiceStatus saveAfterLimitTime(User user, Restaurant restaurant);

    List<Lunch> getForRestaurantAndDate(Restaurant restaurant, LocalDate date);

    Restaurant getCurrent(User user) throws RestaurantNotFoundException;

    ChoiceStatus choiceStatus(User user, Restaurant restaurant);
}
