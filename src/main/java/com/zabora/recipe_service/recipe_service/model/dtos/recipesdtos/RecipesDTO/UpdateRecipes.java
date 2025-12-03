package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.UpdateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesImageDTO.UpdateLicenseImg;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.UpdateSteps;

import java.util.Set;

public record UpdateRecipes(
        String title,
        String shortDescription,
        Integer servings,
        Integer totalTimeMin,
        Integer difficultyId,
        Integer licenseRecipeId,
        Set<Integer> categoryIds,
        Set<Integer> flavorIds,
        Set<UpdateRecipeIngredient> ingredients,
        Set<UpdateLicenseImg> images,
        Set<UpdateSteps> steps
) {
}
