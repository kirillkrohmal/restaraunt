package com.restarauntvote.restarauntservice.to;

import com.restarauntvote.restarauntservice.model.Lunch;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class LunchTo {
    private String name;
    private Integer price;

    public LunchTo(Lunch lunch) {
        this.name = lunch.getName();
        this.price = lunch.getPrice();
    }
}
