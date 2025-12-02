package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO;

public record ResponseRecipeIngredient(
        Integer id,
        String ingredientName,
        Double quantity,
        String unitName,
        String measurementName
) {
}
