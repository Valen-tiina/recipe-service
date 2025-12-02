package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Step;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Integer> {
}
