package com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateIngredient(
        @NotBlank(message = "El nombre del ingrediente es obligatorio")
        @Size(min=2, max=100, message="El titulo debe tener entre 5 y 100 caracteres")
        String name,
        String imageUrl
) {
}
