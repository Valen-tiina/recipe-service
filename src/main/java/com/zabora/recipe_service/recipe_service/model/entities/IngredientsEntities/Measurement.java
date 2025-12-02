package com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "measurement")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_measurement", length = 20)
    private String name;
}
