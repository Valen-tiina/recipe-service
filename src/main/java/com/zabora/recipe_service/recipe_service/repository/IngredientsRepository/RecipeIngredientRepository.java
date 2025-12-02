package com.zabora.recipe_service.recipe_service.repository.IngredientsRepository;

import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
}
