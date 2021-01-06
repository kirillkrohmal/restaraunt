package com.restarauntvote.restarauntservice.service;

import com.restarauntvote.restarauntservice.exceptions.DataNotFoundException;
import com.restarauntvote.restarauntservice.exceptions.ResourceNotFoundException;
import com.restarauntvote.restarauntservice.exceptions.RestaurantNotFoundException;
import com.restarauntvote.restarauntservice.exceptions.ValidationLimitException;
import com.restarauntvote.restarauntservice.model.Choice;
import com.restarauntvote.restarauntservice.model.Lunch;
import com.restarauntvote.restarauntservice.model.Restaurant;
import com.restarauntvote.restarauntservice.model.User;
import com.restarauntvote.restarauntservice.repository.ChoiceRepository;
import com.restarauntvote.restarauntservice.repository.LunchRepository;
import com.restarauntvote.restarauntservice.util.ChoiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



@Service
public class ChoiceServiceImpl implements ChoiceService {

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalTime TIME_LIMIT = LocalTime.parse("11:00");
    private final Logger log = LoggerFactory.getLogger(ChoiceService.class);
    private final ChoiceRepository choiceRepository;
    private final LunchRepository lunchRepository;

    @Autowired
    public ChoiceServiceImpl(ChoiceRepository choiceRepository, LunchRepository lunchRepository) {
        this.choiceRepository = choiceRepository;
        this.lunchRepository = lunchRepository;
    }

    @Override
    public Optional<Choice> getForUserAndDate(Long userId, LocalDate date) {
        log.debug("Authorized {}", userId);
        return choiceRepository.getForUserAndDate(userId, date);
    }

    @Override
    public List<Lunch> getForRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return lunchRepository.findAllByRestaurantAndDate(restaurant, date);
    }

    @Override
    @Transactional
    public ChoiceStatus save(User user, Restaurant restaurant) {
        ChoiceStatus choiceStatus = choiceRepository.getForUserAndDate(user.getId(), TODAY)
                .map(c -> {
                    c.setRestaurant(restaurant); //create
                    return new ChoiceStatus(c, false);
                })
                .orElseGet(() -> new ChoiceStatus(
                        new Choice(user, restaurant, TODAY), true)); //update

        choiceRepository.save(choiceStatus.getChoice());

        return choiceStatus;
    }

    @Override
    @Transactional
    public ChoiceStatus saveAfterLimitTime(User user, Restaurant restaurant) {
        return choiceRepository.getForUserAndDate(user.getId(), TODAY)
                .map(c -> new ChoiceStatus(c, false))
                .orElseGet(() -> new ChoiceStatus(choiceRepository.save(new Choice(user, restaurant, TODAY)), true));
    }

    @Override
    public Restaurant getCurrent(User user) throws RestaurantNotFoundException {
        return getForUserAndDate(user.getId(), TODAY).map(Choice::getRestaurant).orElseThrow(() -> new RestaurantNotFoundException("The restaurant was not chosen"));
    }

    @Override
    public ChoiceStatus choiceStatus(User user, Restaurant restaurant) {

        if (restaurant == null) {
            log.info("Restaurant not fount");
            throw new ResourceNotFoundException("Restaurant not found");
        }

        if (getForRestaurantAndDate(restaurant, TODAY).isEmpty()) {
            log.info("The restaurant {} does not have lunch for choice", restaurant.getId());
            throw new DataNotFoundException("The restaurant does not have lunch for choice");
        }

        boolean limit = LocalTime.now().isAfter(TIME_LIMIT);
        ChoiceStatus choiceStatus = limit
                ? saveAfterLimitTime(user, restaurant)
                : save(user, restaurant);

        if (!choiceStatus.isCreated() && limit){
            log.info("Choices time expired. Current time is {}", LocalTime.now().toString());
            throw new ValidationLimitException("Choices time expired");
        }

        return choiceStatus;
    }
}
