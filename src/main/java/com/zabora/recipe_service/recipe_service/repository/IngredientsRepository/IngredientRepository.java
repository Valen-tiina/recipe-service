package com.zabora.recipe_service.recipe_service.repository.IngredientsRepository;

import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    boolean existsByNameIgnoreCase(String name);
}
