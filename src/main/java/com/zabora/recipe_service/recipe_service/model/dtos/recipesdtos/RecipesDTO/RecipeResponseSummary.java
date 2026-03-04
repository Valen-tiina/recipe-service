package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

public record RecipeResponseSummary(
        Integer id,
        String title,
        String shortDescription,
        String imageUrl
) {
}
