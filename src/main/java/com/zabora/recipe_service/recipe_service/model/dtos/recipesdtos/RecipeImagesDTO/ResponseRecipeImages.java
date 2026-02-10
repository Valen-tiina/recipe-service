package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO;

public record ResponseRecipeImages(
        Integer id,
        String imageUrl,
        String altText,
        Integer position,
        Integer licenseId,
        String licenseName,
        String licenseUrl
) {
}
