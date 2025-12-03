package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO;

public record UpdateRecipeIngredient(
        Integer recipeIngredientId,
        Integer ingredientId,
        Double quantity,
        Integer unitId
) {
}
