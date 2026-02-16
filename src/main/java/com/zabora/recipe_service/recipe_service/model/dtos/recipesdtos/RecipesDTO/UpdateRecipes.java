package com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.CreateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.UpdateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.CreateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.UpdateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.CreateSteps;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.UpdateSteps;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UpdateRecipes(
        @NotBlank(message = "El titulo de la receta es obligatorio")
        @Size(min=5, max=100, message="El titulo debe tener entre 5 y 100 caracteres")
        String title,

        @NotBlank(message = "La descripción de la receta es obligatoria")
        String shortDescription,

        @NotNull(message = "La cantidad de porciones es obligatoria")
        @Positive(message = "Ingresa un numero positivo en: cantidad de porciones")
        @Max(value = 50, message = "La cantidad de porciones no puede ser mayor a 30")
        Integer servings,

        @NotBlank(message = "El nombre de la licencia de la receta es obligatorio")
        @Size(min=5, max=100, message="El nombre de la licencia de la receta debe tener entre 5 y 150 caracteres")
        String licenseName,

        @NotBlank(message = "La URL de la licencia de la receta es obligatorio")
        @Pattern(regexp = "^https?://.*", message = "Ingresa una URL válida")
        String licenseUrl,

        @NotNull(message = "Ingresa la dificultad")
        Integer difficultyId,
        @NotNull(message = "Ingresa como minimo una categoria")
        Set<Integer> categoryIds,
        @NotNull(message = "Ingresa como minimo un sabor")
        Set<Integer> flavorIds,
        @NotNull(message = "Ingresa como minimo un ingrediente")
        Set<UpdateRecipeIngredient> ingredients,  // ← Cambio aquí
        @NotNull(message = "Ingresa como minimo una imagen")
        Set<UpdateRecipeImages> images,           // ← Cambio aquí
        @NotNull(message = "Ingresa como minimo un paso")
        Set<UpdateSteps> steps
) {}

