package com.restarauntvote.restarauntservice.repository;

import com.restarauntvote.restarauntservice.model.Lunch;
import com.restarauntvote.restarauntservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@RepositoryRestResource(collectionResourceRel = "lunch", path = "lunch")
public interface LunchRepository extends JpaRepository<Lunch, Long> {

    @RestResource(path = "by-date")
    @Transactional(readOnly = true)
    List<Lunch> findAllByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @RestResource(path = "by-restaurant")
    @Transactional(readOnly = true)
    List<Lunch> findAllByRestaurant(@Param("restaurant") Restaurant restaurant);

    @RestResource(path = "by-restaurant-and-date")
    @Transactional(readOnly = true)
    List<Lunch> findAllByRestaurantAndDate(@Param("restaurant") Restaurant restaurant, @Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @Override
    @Secured("ROLE_ADMIN")
    Lunch save(Lunch entity);

    @Override
    @Secured("ROLE_ADMIN")
    void delete(Lunch entity);
}
