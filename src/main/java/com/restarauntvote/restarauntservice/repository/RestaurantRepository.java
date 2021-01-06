package com.restarauntvote.restarauntservice.repository;

import com.restarauntvote.restarauntservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;



@RepositoryRestResource(collectionResourceRel = "restaurant", path = "restaurants")
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @RestResource(path = "by-name")
    @Transactional(readOnly = true)
    Restaurant findByName(@Param("name") String name);

    @Override
    @Secured("ROLE_ADMIN")
    Restaurant save(Restaurant entity);

    @Override
    @Secured("ROLE_ADMIN")
    void delete(Restaurant entity);
}
