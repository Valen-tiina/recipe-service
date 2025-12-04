package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.CategoryResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.FlavorResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesRecipeDTO.ResponseLicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.ResponseSteps;

import java.util.List;

public record ResponseRecipes(
        Integer id,
        String title,
        String shortDescription,
        Integer servings,
        Integer totalTimeMin,
        String difficulty,
        ResponseLicenseRecipe license,
        List<CategoryResponse> categories,
        List<FlavorResponse> flavors,
        List<ResponseRecipeImages> images,
        List<ResponseIngredient> ingredients,
        List<ResponseSteps> steps
) {
}
