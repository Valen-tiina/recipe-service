package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.UpdateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.UpdateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.UpdateRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.UpdateSteps;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Ingredient;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.*;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.DifficultyRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.FlavorRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.IngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.LicenseImageRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.LicenseRecipeRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeServiceUpdate {
    private final RecipeRepository recipeRepository;
    private final LicenseRecipeRepository licenseRecipeRepository;
    private final LicenseImageRepository licenseImageRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeService recipeService;

    public RecipeServiceUpdate(
            RecipeRepository recipeRepository,
            LicenseRecipeRepository licenseRecipeRepository,
            IngredientRepository ingredientRepository,
            UnitRepository unitRepository,
            LicenseImageRepository licenseImageRepository,
            RecipeService recipeService
    ) {
        this.recipeRepository = recipeRepository;
        this.licenseRecipeRepository = licenseRecipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.licenseImageRepository = licenseImageRepository;
        this.recipeService = recipeService;
    }

    @Transactional
    public ResponseRecipes updateRecipe(Integer id, UpdateRecipes dto) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        Difficulty difficulty = recipeService.validateDifficulty(dto.difficultyId());
        var categories = recipeService.validateCategories(dto.categoryIds());
        var flavors = recipeService.validateFlavors(dto.flavorIds());

        updateRecipeBasicInfo(recipe, dto, difficulty, categories, flavors);
        updateRecipeLicense(recipe, dto);
        updateRecipeIngredients(recipe, dto);
        updateRecipeImages(recipe, dto);
        updateRecipeSteps(recipe, dto);

        recipe = recipeRepository.save(recipe);

        return recipeService.mapToResponse(recipe);
    }

// VALIDACIÓN DE ACTUALIZACIÓN INDIVIDUAL

    private void updateRecipeBasicInfo(
            Recipe recipe,
            UpdateRecipes dto,
            Difficulty difficulty,
            Set<Category> categories,
            Set<Flavor> flavors
    ) {
        recipe.setTitle(dto.title());
        recipe.setShortDescription(dto.shortDescription());
        recipe.setServings(dto.servings());
        recipe.setDifficulty(difficulty);
        recipe.setCategories(categories);
        recipe.setFlavors(flavors);
    }

    private void updateRecipeLicense(Recipe recipe, UpdateRecipes dto) {
        LicenseRecipe license = recipe.getLicense();
        license.setName(dto.licenseName());
        license.setUrlImage(dto.licenseUrl());
        licenseRecipeRepository.save(license);
    }

    private void updateRecipeIngredients(Recipe recipe, UpdateRecipes dto) {
        // 1. Obtener los IDs de recipe_ingredients que vienen en el DTO
        Set<Integer> incomingIds = dto.ingredients().stream()
                .map(UpdateRecipeIngredient::recipeIngredientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. Eliminar solo los que YA NO están en el DTO
        recipe.getIngredients().removeIf(recipeIng ->
                recipeIng.getId() != null && !incomingIds.contains(recipeIng.getId())
        );

        // 3. Actualizar o crear cada ingrediente
        for (var ingDto : dto.ingredients()) {
            RecipeIngredient recipeIng;

            if (ingDto.recipeIngredientId() != null) {
                // Buscar el ingrediente existente en la colección
                recipeIng = recipe.getIngredients().stream()
                        .filter(ri -> ri.getId().equals(ingDto.recipeIngredientId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("RecipeIngredient no encontrado: " + ingDto.recipeIngredientId()));
            } else {
                // Es un ingrediente nuevo
                recipeIng = new RecipeIngredient();
                recipe.getIngredients().add(recipeIng);
            }

            // Actualizar los datos
            Ingredient ingredient = ingredientRepository.findById(ingDto.ingredientId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado: " + ingDto.ingredientId()));

            Unit unit = unitRepository.findById(ingDto.unitId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada: " + ingDto.unitId()));

            recipeIng.setRecipe(recipe);
            recipeIng.setIngredient(ingredient);
            recipeIng.setQuantity(ingDto.quantity());
            recipeIng.setUnit(unit);
        }
    }

    private void updateRecipeImages(Recipe recipe, UpdateRecipes dto) {
        // 1. Obtener los IDs de recipe_images que vienen en el DTO
        Set<Integer> incomingIds = dto.images().stream()
                .map(UpdateRecipeImages::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. Eliminar solo las que YA NO están en el DTO
        recipe.getImages().removeIf(img ->
                img.getId() != null && !incomingIds.contains(img.getId())
        );

        // 3. Actualizar o crear cada imagen
        for (var imgDto : dto.images()) {
            RecipeImage img;
            LicenseImage licenseImg;

            if (imgDto.id() != null) {
                // Buscar la imagen existente
                img = recipe.getImages().stream()
                        .filter(i -> i.getId().equals(imgDto.id()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("RecipeImage no encontrada: " + imgDto.id()));

                // Actualizar la licencia existente
                licenseImg = img.getLicense();
                licenseImg.setName(imgDto.licenseName());        // ← FALTABA
                licenseImg.setUrlRecipe(imgDto.licenseUrl());    // ← Estaba mal (usabas imageUrl)
            } else {
                // Es una imagen nueva
                img = new RecipeImage();
                recipe.getImages().add(img);

                licenseImg = new LicenseImage();
                licenseImg.setName(imgDto.licenseName());        // ← FALTABA
                licenseImg.setUrlRecipe(imgDto.licenseUrl());    // ← Estaba mal
            }

            licenseImg = licenseImageRepository.save(licenseImg);

            img.setRecipe(recipe);
            img.setImageUrl(imgDto.imageUrl());
            img.setAltText(imgDto.altText());      // ← FALTABA
            img.setPosition(imgDto.position());    // ← FALTABA
            img.setLicense(licenseImg);
        }
    }

    private void updateRecipeSteps(Recipe recipe, UpdateRecipes dto) {
        Set<Integer> incomingIds = dto.steps().stream()
                .map(UpdateSteps::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        recipe.getSteps().removeIf(step ->
                step.getId() != null && !incomingIds.contains(step.getId())
        );

        int totalTimeSeconds = 0;

        for (var stepDto : dto.steps()) {
            Step step;

            if (stepDto.id() != null) {
                step = recipe.getSteps().stream()
                        .filter(s -> s.getId().equals(stepDto.id()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Step no encontrado: " + stepDto.id()));
            } else {
                // Es un paso nuevo
                step = new Step();
                recipe.getSteps().add(step);
            }

            step.setRecipe(recipe);
            step.setStepOrder(stepDto.stepOrder());
            step.setDescription(stepDto.description());
            step.setTimeSeconds(stepDto.timeSeconds());
            step.setImageUrl(stepDto.imageUrl());

            totalTimeSeconds += stepDto.timeSeconds() != null ? stepDto.timeSeconds() : 0;
        }

        recipe.setTotalTimeMin(totalTimeSeconds / 60);
    }
}
