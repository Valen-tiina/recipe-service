package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO;

public record CreateSteps(
        Integer stepOrder,
        String description,
        Integer timeSeconds,
        String imageUrl
) {
}
