package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;


import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.CreateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.CreateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.CreateSteps;

import java.util.Set;

public record CreateRecipe(
        String title,
        String shortDescription,
        Integer servings,
        Integer difficultyId,
        String licenseName,
        String licenseUrl,
        Set<Integer> categoryIds,
        Set<Integer> flavorIds,
        Set<CreateRecipeIngredient> ingredients,
        Set<CreateRecipeImages> images,
        Set<CreateSteps> steps
) {}

