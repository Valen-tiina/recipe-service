package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO;

public record CreateIngredient(
        String name,
        Integer measurementId,
        String imageUrl
) {
}
