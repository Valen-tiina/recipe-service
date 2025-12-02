package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.RecipeImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Integer> {
}
