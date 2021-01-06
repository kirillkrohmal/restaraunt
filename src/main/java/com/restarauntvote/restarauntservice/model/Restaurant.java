package com.restarauntvote.restarauntservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "unique_restaurant_name_idx")})
public class Restaurant extends AbstractNamedEntity {
    public Restaurant(Long id, String name) {
        super(id, name);
    }

    public String getName(String name) {
        return this.name = name;
    }
}
