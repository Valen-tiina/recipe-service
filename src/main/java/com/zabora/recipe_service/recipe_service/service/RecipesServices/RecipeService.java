package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.CategoryResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.FlavorResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.UpdateRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesRecipeDTO.ResponseLicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.UpdateRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.UpdateRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.ResponseSteps;
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
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    public RecipeService(
            RecipeRepository recipeRepository,
            CategoryRepository categoryRepository,
            FlavorRepository flavorRepository,
            DifficultyRepository difficultyRepository,
            LicenseRecipeRepository licenseRecipeRepository,
            IngredientRepository ingredientRepository,
            UnitRepository unitRepository,
            LicenseImageRepository licenseImageRepository
    ) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.flavorRepository = flavorRepository;
        this.difficultyRepository = difficultyRepository;
        this.licenseRecipeRepository = licenseRecipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.licenseImageRepository = licenseImageRepository;
    }

    @Transactional
    public ResponseRecipes createRecipe(CreateRecipe dto) {
        var difficulty = validateDifficulty(dto.difficultyId());
        var categories = validateCategories(dto.categoryIds());
        var flavors = validateFlavors(dto.flavorIds());
        var license = createRecipeLicense(dto);

        var recipe = buildRecipe(dto, difficulty, categories, flavors, license);

        recipe.setIngredients(createRecipeIngredients(dto, recipe));
        recipe.setImages(createRecipeImages(dto, recipe));
        recipe.setSteps(createSteps(dto, recipe));

        recipe = recipeRepository.save(recipe);

        return mapToResponse(recipe);
    }

    // validación separada por metodos
    private Difficulty validateDifficulty(Integer difficultyId) {
        return difficultyRepository.findById(difficultyId)
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));
    }

    private Set<Category> validateCategories(Set<Integer> categoryIds) {
        var categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        if (categories.size() != categoryIds.size()) {
            throw new RuntimeException("Una o más categorías no existen");
        }
        return categories;
    }
    private Set<Flavor> validateFlavors(Set<Integer> flavorIds) {
        var flavors = new HashSet<>(flavorRepository.findAllById(flavorIds));
        if (flavors.size() != flavorIds.size()) {
            throw new RuntimeException("Uno o más sabores no existen");
        }
        return flavors;
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


// mapea rta que se mostrara en front
    private ResponseRecipes mapToResponse(Recipe recipe) {
        return new ResponseRecipes(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getServings(),
                recipe.getTotalTimeMin(),
                recipe.getDifficulty().getName(),

                new ResponseLicenseRecipe(
                        recipe.getLicense().getId(),
                        recipe.getLicense().getName(),
                        recipe.getLicense().getUrlImage()
                ),

                recipe.getCategories()
                        .stream()
                        .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                        .collect(Collectors.toSet()),

                recipe.getFlavors()
                        .stream()
                        .map(f -> new FlavorResponse(f.getId(), f.getName()))
                        .collect(Collectors.toSet()),

                recipe.getImages()
                        .stream()
                        .map(img -> new ResponseRecipeImages(
                                img.getId(),
                                img.getImageUrl(),
                                img.getAltText(),
                                img.getPosition(),
                                img.getLicense().getId(),
                                img.getLicense().getName(),
                                img.getLicense().getUrlRecipe()
                        ))
                        .toList(),

                recipe.getIngredients()
                        .stream()
                        .map(ri -> new ResponseRecipeIngredient(
                                ri.getId(),
                                ri.getIngredient().getName(),
                                ri.getIngredient().getImageUrl(),
                                ri.getQuantity(),
                                new UnitResponse(
                                        ri.getUnit().getId(),
                                        ri.getUnit().getName(),
                                        new MeasurementResponse(
                                                ri.getUnit().getMeasurement().getId(),
                                                ri.getUnit().getMeasurement().getName()
                                        )
                                )
                        ))
                        .toList(),

                recipe.getSteps()
                        .stream()
                        .map(s -> new ResponseSteps(
                                s.getId(),
                                s.getStepOrder(),
                                s.getDescription(),
                                s.getTimeSeconds(),
                                s.getImageUrl()
                        ))
                        .toList()
        );
    }

    // UPDATE - ACTUALIZAR RECETA
// UPDATE - ACTUALIZAR RECETA

    @Transactional
    public ResponseRecipes updateRecipe(Integer id, UpdateRecipes dto) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        Difficulty difficulty = validateDifficulty(dto.difficultyId());
        var categories = validateCategories(dto.categoryIds());
        var flavors = validateFlavors(dto.flavorIds());

        updateRecipeBasicInfo(recipe, dto, difficulty, categories, flavors);
        updateRecipeLicense(recipe, dto);
        updateRecipeIngredients(recipe, dto);
        updateRecipeImages(recipe, dto);
        updateRecipeSteps(recipe, dto);

        recipe = recipeRepository.save(recipe);

        return mapToResponse(recipe);
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
        // 1. Obtener los IDs de steps que vienen en el DTO
        Set<Integer> incomingIds = dto.steps().stream()
                .map(UpdateSteps::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. Eliminar solo los que YA NO están en el DTO
        recipe.getSteps().removeIf(step ->
                step.getId() != null && !incomingIds.contains(step.getId())
        );

        int totalTimeSeconds = 0;

        // 3. Actualizar o crear cada paso
        for (var stepDto : dto.steps()) {
            Step step;

            if (stepDto.id() != null) {
                // Buscar el paso existente
                step = recipe.getSteps().stream()
                        .filter(s -> s.getId().equals(stepDto.id()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Step no encontrado: " + stepDto.id()));
            } else {
                // Es un paso nuevo
                step = new Step();
                recipe.getSteps().add(step);
            }

            // Actualizar los datos
            step.setRecipe(recipe);
            step.setStepOrder(stepDto.stepOrder());
            step.setDescription(stepDto.description());
            step.setTimeSeconds(stepDto.timeSeconds());
            step.setImageUrl(stepDto.imageUrl());

            totalTimeSeconds += stepDto.timeSeconds() != null ? stepDto.timeSeconds() : 0;
        }

        recipe.setTotalTimeMin(totalTimeSeconds / 60);
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> getAllRecipes() {
        return recipeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseRecipes getRecipeById(Integer id) {
        Recipe recipe = recipeRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        return mapToResponse(recipe);
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> getRecipesByIds(List<Integer> ids) {
        var recipesMap = recipeRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(
                        Recipe::getId,
                        recipe -> recipe
                ));

        return ids.stream()
                .map(recipesMap::get)
                .filter(r -> r != null)
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> searchRecipesByTitle(String title) {
        var recipes = recipeRepository.findByTitleContainingIgnoreCase(title);

        if (recipes.isEmpty()) {
            throw new RuntimeException("No se encontraron recetas con ese título");
        }

        return recipes.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> searchRecipesByIngredient(String ingredient) {
        var recipes = recipeRepository.findByIngredientNameContaining(ingredient);

        if (recipes.isEmpty()) {
            throw new RuntimeException("No se encontraron recetas con ese ingrediente");
        }

        return recipes.stream()
                .map(this::mapToResponse)
                .toList();
    }
// DELETE - ELIMINAR RECETA

    @Transactional
    public void deleteRecipe(Integer id) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));

        recipeRepository.delete(recipe);
    }

    //menu del dia
    public List<ResponseRecipes> getRecipesOfTheDay() {
        return List.of(
                getRandomRecipeByCategory(1), // Desayuno
                getRandomRecipeByCategory(2), // Almuerzo
                getRandomRecipeByCategory(3)  // Cena
        );
    }

    private ResponseRecipes getRandomRecipeByCategory(Integer categoryId) {
        var recipes = recipeRepository.findByCategoryId(categoryId);

        if (recipes.isEmpty()) {
            throw new RuntimeException("No hay recetas disponibles para la categoría: " + categoryId);
        }

        var random = new Random();
        var randomRecipe = recipes.get(random.nextInt(recipes.size()));

        return mapToResponse(randomRecipe);
    }
}