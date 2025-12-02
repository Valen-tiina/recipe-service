package com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_ingredients")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
