package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_images")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;

    @Column(name = "alt_text", length = 100)
    private String altText;

    @Column(name = "position")
    private Integer position = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "license_id")
    private LicenseImage license;
}
