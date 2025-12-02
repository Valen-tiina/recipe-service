package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepTemplateDTO;

public record ResponseStepTemplate(
        Integer id,
        String title,
        String shortDesc,
        String imageUrl
) {
}
