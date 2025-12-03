package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.LicenseImage;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.DifficultyRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.FlavorRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.IngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.RecipeIngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final FlavorRepository flavorRepository;
    private final DifficultyRepository difficultyRepository;
    private final LicenseRecipeRepository licenseRecipeRepository;
    private final LicenseImageRepository licenseImageRepository;
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    private final StepRepository stepRepository;
    private final StepTemplateRepository stepTemplateRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            CategoryRepository categoryRepository,
            FlavorRepository flavorRepository,
            DifficultyRepository difficultyRepository,
            LicenseRecipeRepository licenseRepository,
            IngredientRepository ingredientRepository,
            UnitRepository unitRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            LicenseImageRepository imageRepository,
            StepRepository stepRepository,
            StepTemplateRepository stepTemplateRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.flavorRepository = flavorRepository;
        this.difficultyRepository = difficultyRepository;
        this.licenseRecipeRepository = licenseRepository;

        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;

        this.licenseImageRepository = imageRepository;

        this.stepRepository = stepRepository;
        this.stepTemplateRepository = stepTemplateRepository;
    }


    @Transactional
    public ResponseRecipes createRecipe(CreateRecipe dto) {

        //validaciones entidades
        var difficulty = difficultyRepository.findById(dto.difficultyId())
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));

        var licenseRecipe = licenseRecipeRepository.findById(dto.licenseRecipeId())
                .orElseThrow(() -> new RuntimeException("Licencia de receta no encontrada"));

        var categories = categoryRepository.findAllById(dto.categoryIds());
        if (categories.size() != dto.categoryIds().size())
            throw new RuntimeException("Alguna categoría no existe");

        var flavors = flavorRepository.findAllById(dto.flavorIds());
        if (flavors.size() != dto.flavorIds().size())
            throw new RuntimeException("Algún sabor no existe");


       //drear receta
        var recipe = new Recipe();
        recipe.setTitle(dto.title());
        recipe.setShortDescription(dto.shortDescription());
        recipe.setServings(dto.servings());
        recipe.setDifficulty(difficulty);
        recipe.setLicense(licenseRecipe);
        recipe.setCategories(Set.copyOf(categories));
        recipe.setFlavors(Set.copyOf(flavors));

        recipe = recipeRepository.save(recipe);


   //ingredientes
        for (var ingDto : dto.ingredients()) {

            var ingredient = ingredientRepository.findById(ingDto.ingredientId())
                    .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

            var unit = unitRepository.findById(ingDto.unitId())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

            var recipeIng = new RecipeIngredient();
            recipeIng.setRecipe(recipe);
            recipeIng.setIngredient(ingredient);
            recipeIng.setQuantity(ingDto.quantity());
            recipeIng.setUnit(unit);

            recipeIngredientRepository.save(recipeIng);
        }


        //img de licencias
        for (var imgDto : dto.images()) {

            var img = new LicenseImage();
            img.setName(imgDto.name());
            img.setUrlRecipe(imgDto.urlImage());
            licenseImageRepository.save(img);

        }


        return new ResponseRecipes(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getServings(),
                recipe.getDifficulty().getName(),
                recipe.getLicense().getName(),
                recipe.getCategories().stream().map(cat -> cat.getName()).toList(),
                recipe.getFlavors().stream().map(fl -> fl.getName()).toList()
        );
    }};
