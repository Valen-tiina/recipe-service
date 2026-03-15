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
        recipe.getIngredients().clear();

        for (var ingDto : dto.ingredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingDto.ingredientId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado: " + ingDto.ingredientId()));

            Unit unit = unitRepository.findById(ingDto.unitId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada: " + ingDto.unitId()));

            RecipeIngredient recipeIng = new RecipeIngredient();
            recipeIng.setRecipe(recipe);
            recipeIng.setIngredient(ingredient);
            recipeIng.setQuantity(ingDto.quantity());
            recipeIng.setUnit(unit);

            recipe.getIngredients().add(recipeIng);
        }
    }

    private void updateRecipeImages(Recipe recipe, UpdateRecipes dto) {
        recipe.getImages().clear();

        for (var imgDto : dto.images()) {
            LicenseImage licenseImg = new LicenseImage();
            licenseImg.setName(imgDto.licenseName());
            licenseImg.setUrlRecipe(imgDto.licenseUrl());
            licenseImg = licenseImageRepository.save(licenseImg);

            RecipeImage img = new RecipeImage();
            img.setRecipe(recipe);
            img.setImageUrl(imgDto.imageUrl());
            img.setAltText(imgDto.altText());
            img.setPosition(imgDto.position());
            img.setLicense(licenseImg);

            recipe.getImages().add(img);
        }
    }

    private void updateRecipeSteps(Recipe recipe, UpdateRecipes dto) {
        recipe.getSteps().clear();

        int totalTimeSeconds = 0;

        for (var stepDto : dto.steps()) {
            Step step = new Step();
            step.setRecipe(recipe);
            step.setStepOrder(stepDto.stepOrder());
            step.setDescription(stepDto.description());
            step.setTimeSeconds(stepDto.timeSeconds());
            step.setImageUrl(stepDto.imageUrl());

            recipe.getSteps().add(step);
            totalTimeSeconds += stepDto.timeSeconds() != null ? stepDto.timeSeconds() : 0;
        }

        recipe.setTotalTimeMin(totalTimeSeconds / 60);
    }
}
