package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "licenses_recipe")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LicenseRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_license", length = 150, nullable = false)
    private String name;
    @Column(name = "url_recipe", length = 150, nullable = false)
    private String urlImage;
}
