package com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter


@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_ing", length = 100, nullable = false)
    private String name;

    @Column(name = "image_url", length = 150, nullable = false)
    private String imageUrl;
}