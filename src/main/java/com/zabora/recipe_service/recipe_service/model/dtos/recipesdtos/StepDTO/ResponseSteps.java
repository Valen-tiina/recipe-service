package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepTemplateDTO.ResponseStepTemplate;

public record ResponseSteps(
        Integer id,
        Integer stepOrder,
        String description,
        Integer timeSeconds,
        String customImageUrl,
        ResponseStepTemplate template
) {
}
