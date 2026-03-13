package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;

import java.util.List;

public record RecipeResponseSummary(
        Integer id,
        String title,
        String shortDescription,
        Integer totalTimeMin,
        String imageUrl,
        List<ResponseRecipeIngredient> ingredients
) {
}
