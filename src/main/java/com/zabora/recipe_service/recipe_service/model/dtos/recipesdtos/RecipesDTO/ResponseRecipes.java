package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import java.util.List;

public record ResponseRecipes(
        Integer id,
        String title,
        String shortDescription,
        Integer servings,
        String difficulty,
        String license,
        List<String> categories,
        List<String> flavors
) {
}
