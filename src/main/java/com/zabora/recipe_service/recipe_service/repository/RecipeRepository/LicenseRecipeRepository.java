package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.LicenseRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRecipeRepository extends JpaRepository<LicenseRecipe, Integer> {
}
