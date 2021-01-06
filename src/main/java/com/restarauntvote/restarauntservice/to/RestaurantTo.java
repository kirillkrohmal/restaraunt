package com.restarauntvote.restarauntservice.to;

import com.restarauntvote.restarauntservice.model.Restaurant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
public class RestaurantTo {
    private String name;
    private List<LunchTo> lunches;

    public RestaurantTo(Restaurant restaurant, List<LunchTo> lunches){
        this.name = restaurant.getName(name);
        this.lunches = lunches;
    }
}
