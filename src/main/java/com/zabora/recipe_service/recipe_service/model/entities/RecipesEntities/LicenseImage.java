package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "licenses_img")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LicenseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_license", length = 100, nullable = false)
    private String name;

    @Column(name = "url_recipe", length = 150, nullable = false)
    private String urlRecipe;
}
