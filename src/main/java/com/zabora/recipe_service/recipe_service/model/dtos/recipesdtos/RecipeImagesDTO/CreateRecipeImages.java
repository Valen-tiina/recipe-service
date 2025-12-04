package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO;

public record CreateRecipeImages(
        String name,
        String imageUrl,
        String altText,
        Integer position,
        Integer licenseId
) {
}
