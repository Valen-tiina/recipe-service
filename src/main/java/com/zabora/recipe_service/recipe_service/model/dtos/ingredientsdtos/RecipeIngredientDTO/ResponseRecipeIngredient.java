package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;

public record ResponseRecipeIngredient(
        Integer id,
        String ingredientName,
        String ingredientImageUrl,
        Double quantity,
        UnitResponse unit
) {
}
