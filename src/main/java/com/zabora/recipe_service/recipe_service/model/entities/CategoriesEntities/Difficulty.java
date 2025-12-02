package com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "difficulty")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Difficulty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_difficulty", length = 6, nullable = false)
    private String name;
}
