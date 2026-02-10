package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.CategoryResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.FlavorResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesRecipeDTO.ResponseLicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.ResponseSteps;

import java.util.List;
import java.util.Set;

public record ResponseRecipes(
        Integer id,
        String title,
        String shortDescription,
        Integer servings,
        Integer totalTimeMin,
        String difficulty,
        ResponseLicenseRecipe license,
        Set<CategoryResponse> categories,
        Set<FlavorResponse> flavors,
        List<ResponseRecipeImages> images,
        List<ResponseRecipeIngredient> ingredients,
        List<ResponseSteps> steps
) {
}
