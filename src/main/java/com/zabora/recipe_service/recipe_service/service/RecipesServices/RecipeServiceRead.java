package com.zabora.recipe_service.recipe_service.service.RecipesServices;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecipeServiceRead {
    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;

    public RecipeServiceRead(RecipeService recipeService, RecipeRepository recipeRepository){
        this.recipeService=recipeService;
        this.recipeRepository=recipeRepository;
    }
    private List<ResponseRecipes> limitRecipesByRole(List<ResponseRecipes> recipes, String role) {
        int limit = getRecipeLimitByRole(role);

        return recipes.stream()
                .limit(limit)
                .toList();
    }

    private int limitIngredientsByRole(String role) {
        return switch (role) {
            case "ROLE_PREMIUM" -> 20;
            case "ROLE_USER" -> 7; // Usuario registrado
            default -> 0;
        };
    }
    /*PENDIENTE A CAMBIOS*/
    private int getRecipeLimitByRole(String role) {
        return switch (role) {
            case "ROLE_ADMIN" -> Integer.MAX_VALUE; // Sin límite
            case "ROLE_PREMIUM" -> Integer.MAX_VALUE;
            case "ROLE_USER" -> 20; // Usuario registrado
            case "ROLE_GUEST" -> 4; // Invitado
            default -> 1;
        };
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> getAllRecipes(String role) {
        var allRecipes = recipeRepository.findAll()
                .stream()
                .map(recipeService::mapToResponse)
                .toList();

        return limitRecipesByRole(allRecipes, role);
    }

    @Transactional(readOnly = true)
    public ResponseRecipes getRecipeById(Integer id) {
        Recipe recipe = recipeRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        return recipeService.mapToResponse(recipe);
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
                .map(recipeService::mapToResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<ResponseRecipes> searchRecipesByTitle(String title, String role) {
        var recipes = recipeRepository.findByTitleContainingIgnoreCase(title);

        var recipesResponse = recipes.stream()
                .map(recipeService::mapToResponse)
                .toList();

        return limitRecipesByRole(recipesResponse, role);
    }

    @Transactional(readOnly = true)
    public List<ResponseRecipes> searchRecipesByIngredients(List<String> ingredients, String role) {
        // Limitar la cantidad de ingredientes según el rol
        int maxIngredients = limitIngredientsByRole(role);

        // Si el rol no tiene permitido buscar, retornar lista vacía
        if (maxIngredients == 0) {
            return List.of();
        }

        // Limitar los ingredientes a buscar según el rol
        List<String> limitedIngredients = ingredients.stream()
                .limit(maxIngredients)
                .toList();

        // Convertir a minúsculas para la búsqueda case-insensitive
        List<String> lowerCaseIngredients = limitedIngredients.stream()
                .map(String::toLowerCase)
                .toList();

        // Buscar recetas que contengan AL MENOS UNO de los ingredientes
        var recipes = recipeRepository.findByIngredientsNameIn(lowerCaseIngredients);

        var recipesResponse = recipes.stream()
                .map(recipeService::mapToResponse)
                .toList();

        return limitRecipesByRole(recipesResponse, role);
    }



    //menu del dia
    private List<ResponseRecipes> getRandomRecipesByCategory(Integer categoryId, int count) {
        var recipes = recipeRepository.findByCategoryId(categoryId);

        if (recipes.isEmpty()) {
            throw new RuntimeException("No hay recetas disponibles para la categoría: " + categoryId);
        }

        var random = new Random();
        var selectedRecipes = new ArrayList<ResponseRecipes>();

        int recipesToSelect = Math.min(count, recipes.size());

        var availableRecipes = new ArrayList<>(recipes);

        for (int i = 0; i < recipesToSelect; i++) {
            int randomIndex = random.nextInt(availableRecipes.size());
            var selectedRecipe = availableRecipes.remove(randomIndex);
            selectedRecipes.add(recipeService.mapToResponse(selectedRecipe));
        }

        return selectedRecipes;
    }

    public List<ResponseRecipes> getBreakfastRecipes() {
        Integer BREAKFAST_CATEGORY_ID = 1;
        return getRandomRecipesByCategory(BREAKFAST_CATEGORY_ID, 3);
    }

    public List<ResponseRecipes> getLunchRecipes() {
        Integer LUNCH_CATEGORY_ID = 2;
        return getRandomRecipesByCategory(LUNCH_CATEGORY_ID, 3);
    }

    public List<ResponseRecipes> getDinnerRecipes() {
        Integer DINNER_CATEGORY_ID = 3;
        return getRandomRecipesByCategory(DINNER_CATEGORY_ID, 3);
    }

    public List<ResponseRecipes> getSnacksRecipes(){
        Integer SNACK_CATEGORY_ID = 4;
        return getRandomRecipesByCategory(SNACK_CATEGORY_ID, 4);
    }

    // daily recipes 3 per day
    public List<ResponseRecipes> getRecipesOfTheDay() {
        return Stream.of(1, 2, 3)
                .map(this::getRandomRecipeByCategory)
                .filter(Objects::nonNull)
                .toList();
    }

    private ResponseRecipes getRandomRecipeByCategory(Integer categoryId) {
        var recipes = recipeRepository.findByCategoryId(categoryId);

        if (recipes.isEmpty()) {
            return null;
        }

        var random = new Random();
        var randomRecipe = recipes.get(random.nextInt(recipes.size()));

        return recipeService.mapToResponse(randomRecipe);
    }
}
