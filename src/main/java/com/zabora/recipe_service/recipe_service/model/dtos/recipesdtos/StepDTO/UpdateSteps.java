package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO;

public record UpdateSteps(
        Integer stepTemplateId,
        Integer stepOrder,
        String description,
        Integer timeSeconds,
        String imageUrl
) {
}
