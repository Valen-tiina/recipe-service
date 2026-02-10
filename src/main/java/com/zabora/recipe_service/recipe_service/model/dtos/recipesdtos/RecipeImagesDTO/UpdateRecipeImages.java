package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO;

public record UpdateRecipeImages(
        Integer id,
        String imageUrl,
        String altText,
        Integer position,
        String licenseName,
        String licenseUrl
) {
}
