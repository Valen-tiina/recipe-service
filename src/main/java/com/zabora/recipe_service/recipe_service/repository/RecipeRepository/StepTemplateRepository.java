package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.StepTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepTemplateRepository extends JpaRepository<StepTemplate, Integer> {
}
