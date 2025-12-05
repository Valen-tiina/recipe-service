package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesRecipeDTO.ResponseLicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.LicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.RecipeImage;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.CategoryResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.FlavorResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.ResponseSteps;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepTemplateDTO.ResponseStepTemplate;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.*;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.DifficultyRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.FlavorRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.IngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.RecipeIngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.HashSet;
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
    private final RecipeImageRepository recipeImageRepository;

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
            StepTemplateRepository stepTemplateRepository,
            RecipeImageRepository recipeImageRepository) {
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
        this.recipeImageRepository = recipeImageRepository;
    }

    @Transactional
    public ResponseRecipes createRecipe(CreateRecipe dto) {

        // 1. VALIDACIONES BASE
        var difficulty = difficultyRepository.findById(dto.difficultyId())
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));


        var categories = categoryRepository.findAllById(dto.categoryIds());
        if (categories.size() != dto.categoryIds().size())
            throw new RuntimeException("Alguna categoría no existe");

        var flavors = flavorRepository.findAllById(dto.flavorIds());
        if (flavors.size() != dto.flavorIds().size())
            throw new RuntimeException("Algún sabor no existe");

        LicenseRecipe license = new LicenseRecipe();
        license.setName(dto.licenseName());
        license.setUrlImage(dto.licenseUrl());
        license = licenseRecipeRepository.save(license);

        // 2. CREAR RECETA BASE SIN TOTAL TIME TODAVÍA
        var recipe = new Recipe();
        recipe.setTitle(dto.title());
        recipe.setShortDescription(dto.shortDescription());
        recipe.setServings(dto.servings());
        recipe.setDifficulty(difficulty);
        recipe.setLicense(license);
        recipe.setCategories(new HashSet<>(categories));
        recipe.setFlavors(new HashSet<>(flavors));

        recipe = recipeRepository.save(recipe);



        // ========================
        // 3. INGREDIENTES
        // ========================
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


        // ========================
        // 4. IMÁGENES
        // ========================


        for (var imgDto : dto.images()) {

            LicenseImage licenseImg = new LicenseImage();
            licenseImg.setName(imgDto.name());
            licenseImg.setUrlRecipe(imgDto.imageUrl());// obligatorio
            licenseImg = licenseImageRepository.save(licenseImg);


            var img = new RecipeImage();
            img.setRecipe(recipe);
            img.setImageUrl(imgDto.imageUrl());
            img.setAltText(imgDto.altText());
            img.setPosition(imgDto.position());
            img.setLicense(licenseImg);

            recipeImageRepository.save(img);
        }



        // ========================
        // 5. STEPS
        // ========================
        int totalTime = 0;

        for (var stepDto : dto.steps()) {

            // Asumiendo que tu DTO tiene stepTemplateId
            var template = stepTemplateRepository.findById(stepDto.stepTemplateId())
                    .orElseThrow(() -> new RuntimeException("Plantilla de paso no encontrada"));

            var step = new Step();
            step.setRecipe(recipe);
            step.setOrder(stepDto.stepOrder());
            step.setDescription(stepDto.description());
            step.setTimeSeconds(stepDto.timeSeconds());
            step.setImageUrl(stepDto.imageUrl());
            step.setTemplate(template); // Necesitas setear la plantilla aquí

            stepRepository.save(step);

            totalTime += stepDto.timeSeconds();
        }


        // 6. ACTUALIZAR TOTAL TIME
        recipe.setTotalTimeMin(totalTime / 60); // Convertir a minutos
        recipeRepository.save(recipe);


        // ========================
        // 7. MAPEAR RESPONSE COMPLETO
        // ========================

        return new ResponseRecipes(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getServings(),
                recipe.getTotalTimeMin(),

                // Dificultad (asumida String)
                recipe.getDifficulty().getName(),

                // Licencia de Receta (Corregido a DTO)
                new ResponseLicenseRecipe(
                        recipe.getLicense().getId(),
                        recipe.getLicense().getName(),
                        recipe.getLicense().getUrlImage()
                ),

                // category responses
                recipe.getCategories()
                        .stream()
                        .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                        .toList(),

                // flavor responses
                recipe.getFlavors()
                        .stream()
                        .map(f -> new FlavorResponse(f.getId(), f.getName()))
                        .toList(),

                // images
                recipe.getImages()
                        .stream()
                        .map(img -> new ResponseRecipeImages(
                                img.getId(),
                                img.getImageUrl(),
                                img.getLicense().getName()
                        ))
                        .toList(),

                // ingredients
                recipe.getIngredients()
                        .stream()
                        .map(ri -> new ResponseIngredient( // <-- ¡Construir ResponseIngredient!
                                ri.getIngredient().getId(), // Usar el ID del ingrediente, no de la tabla puente
                                ri.getIngredient().getName(),
                                ri.getIngredient().getImageUrl(),
                                ri.getUnit().getMeasurement().getName() // <-- Simplificar a solo el nombre (String)
                        )).toList(),

                // steps (Corregido el mapeo anidado de StepTemplate)
                recipe.getSteps()
                        .stream()
                        .map(s -> {
                            // 1. Obtener la Entidad StepTemplate
                            StepTemplate templateEntity = s.getTemplate();

                            // 2. Convertir la Entidad a DTO (ResponseStepTemplate)
                            ResponseStepTemplate templateDTO = new ResponseStepTemplate(
                                    templateEntity.getId(),
                                    templateEntity.getTitle(),
                                    templateEntity.getShortDescription(),
                                    templateEntity.getImageUrl()
                            );

                            // 3. Crear el DTO final ResponseSteps
                            return new ResponseSteps(
                                    s.getId(),
                                    s.getOrder(),       // Asumiendo s.getOrder() es el getter para stepOrder
                                    s.getDescription(),
                                    s.getTimeSeconds(),
                                    s.getImageUrl(),    // Asumiendo s.getImageUrl() es el getter para customImageUrl
                                    templateDTO         // <-- DTO convertido
                            );
                        }).toList());


    }
}