package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.UpdateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.UpdateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.UpdateSteps;

import java.util.Set;

public record UpdateRecipes(
        String title,
        String shortDescription,
        Integer servings,
        Integer difficultyId,
        String licenseName,
        Set<Integer> categoryIds,
        Set<Integer> flavorIds,
        Set<UpdateRecipeIngredient> ingredients,
        Set<UpdateRecipeImages> images,
        Set<UpdateSteps> steps
) {
}
