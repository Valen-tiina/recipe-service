package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO;

public record CreateSteps(
        Integer order,
        String description,
        Integer timeSeconds,
        String imageUrl
) {
}
