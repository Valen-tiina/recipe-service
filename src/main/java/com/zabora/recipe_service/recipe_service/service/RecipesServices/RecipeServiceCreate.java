package com.zabora.recipe_service.recipe_service.service.RecipesServices;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
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

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeServiceCreate {
    private final RecipeRepository recipeRepository;
    private final LicenseRecipeRepository licenseRecipeRepository;
    private final LicenseImageRepository licenseImageRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeService recipeService;

    public RecipeServiceCreate(
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
    public ResponseRecipes createRecipe(CreateRecipe dto) {
        var difficulty = recipeService.validateDifficulty(dto.difficultyId());
        var categories = recipeService.validateCategories(dto.categoryIds());
        var flavors = recipeService.validateFlavors(dto.flavorIds());
        var license = createRecipeLicense(dto);

        var recipe = buildRecipe(dto, difficulty, categories, flavors, license);

        recipe.setIngredients(createRecipeIngredients(dto, recipe));
        recipe.setImages(createRecipeImages(dto, recipe));
        recipe.setSteps(createSteps(dto, recipe));

        recipe = recipeRepository.save(recipe);

        return recipeService.mapToResponse(recipe);
    }

    private LicenseRecipe createRecipeLicense(CreateRecipe dto) {
        LicenseRecipe license = new LicenseRecipe();
        license.setName(dto.licenseName());
        license.setUrlImage(dto.licenseUrl());
        return licenseRecipeRepository.save(license);
    }
    // construye receta con lo existente
    private Recipe buildRecipe(
            CreateRecipe dto,
            Difficulty difficulty,
            Set<Category> categories,
            Set<Flavor> flavors,
            LicenseRecipe license
    ) {
        var recipe = new Recipe();
        recipe.setTitle(dto.title());
        recipe.setShortDescription(dto.shortDescription());
        recipe.setServings(dto.servings());
        recipe.setDifficulty(difficulty);
        recipe.setLicense(license);
        recipe.setCategories(categories);
        recipe.setFlavors(flavors);
        return recipe;
    }
    // crea los campos ingresados por el usuario y guarda en la bd
    private Set<RecipeIngredient> createRecipeIngredients(CreateRecipe dto, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for (var ingDto : dto.ingredients()) {
            var ingredient = ingredientRepository.findById(ingDto.ingredientId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado: " + ingDto.ingredientId()));

            var unit = unitRepository.findById(ingDto.unitId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada: " + ingDto.unitId()));

            var recipeIng = new RecipeIngredient();
            recipeIng.setRecipe(recipe);
            recipeIng.setIngredient(ingredient);
            recipeIng.setQuantity(ingDto.quantity());
            recipeIng.setUnit(unit);

            recipeIngredients.add(recipeIng);
        }

        return recipeIngredients;
    }

    private Set<RecipeImage> createRecipeImages(CreateRecipe dto, Recipe recipe) {
        Set<RecipeImage> recipeImages = new HashSet<>();

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

            recipeImages.add(img);
        }

        return recipeImages;
    }

    private Set<Step> createSteps(CreateRecipe dto, Recipe recipe) {
        Set<Step> steps = new HashSet<>();
        int totalTimeSeconds = 0;

        for (var stepDto : dto.steps()) {
            var step = new Step();
            step.setRecipe(recipe);
            step.setStepOrder(stepDto.stepOrder());
            step.setDescription(stepDto.description());
            step.setTimeSeconds(stepDto.timeSeconds());
            step.setImageUrl(stepDto.imageUrl());

            steps.add(step);
            totalTimeSeconds += stepDto.timeSeconds() != null ? stepDto.timeSeconds() : 0;
        }

        recipe.setTotalTimeMin(totalTimeSeconds / 60);
        return steps;
    }

}
