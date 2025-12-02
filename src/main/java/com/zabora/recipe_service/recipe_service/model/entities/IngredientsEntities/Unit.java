package com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "units")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;

    @Column(name = "name_unit", length = 50, nullable = false)
    private String name;
}
