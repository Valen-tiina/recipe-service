package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO;


public record ResponseSteps(
        Integer id,
        Integer stepOrder,
        String description,
        Integer timeSeconds,
        String imageUrl) {
}
