package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "short_desc", length = 255, nullable = false)
    private String shortDescription;

    @Column(name = "total_time_min")
    private Integer totalTimeMin;

    @ManyToOne
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @Column(nullable = false)
    private Integer servings;


    @ManyToOne
    @JoinColumn(name = "license_recipe_id")
    private LicenseRecipe license;

    @ManyToMany
    @JoinTable(
            name = "recipe_categories",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "recipe_flavors",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "flavor_id")
    )
    private Set<Flavor> flavors;

    // 1. Mapeo de Pasos
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Step> steps;

    // 2. Mapeo de Ingredientes (usa la tabla puente recipe_ingredients)
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RecipeIngredient> ingredients;

    // 3. Mapeo de Imágenes
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RecipeImage> images;
}
