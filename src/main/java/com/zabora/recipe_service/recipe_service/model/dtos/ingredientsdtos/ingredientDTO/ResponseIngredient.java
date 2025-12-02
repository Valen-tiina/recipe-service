package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO;

public record ResponseIngredient(
        Integer id,
        String name,
        String imageUrl,
        String measurementName
) {
}
