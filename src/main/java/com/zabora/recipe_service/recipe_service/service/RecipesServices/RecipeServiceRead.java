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
        int maxIngredients = limitIngredientsByRole(role);

        if (maxIngredients == 0) {
            return List.of();
        }

        List<String> limitedIngredients = ingredients.stream()
                .limit(maxIngredients)
                .toList();

        List<String> lowerCaseIngredients = limitedIngredients.stream()
                .map(String::toLowerCase)
                .toList();

        var recipes = recipeRepository.findByIngredientsNameIn(lowerCaseIngredients);

        var recipesResponse = recipes.stream()
                .map(recipeService::mapToResponse)
                .toList();

        return limitRecipesByRole(recipesResponse, role);
    }



    //menu del dia

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
// daily recipes 9 per day (3 per category)
    public List<ResponseRecipes> getRecipesOfTheDay() {
        List<ResponseRecipes> allRecipes = new ArrayList<>();

        // Sumamos las 3 de cada categoría a una sola lista
        allRecipes.addAll(getBreakfastRecipes());
        allRecipes.addAll(getLunchRecipes());
        allRecipes.addAll(getDinnerRecipes());

        return allRecipes;
    }

    public List<ResponseRecipes> getRandomRecipesByCategory(Integer categoryId, int limit) {
        var recipes = recipeRepository.findByCategoryId(categoryId);

        if (recipes.isEmpty()) {
            return Collections.emptyList();
        }

        // Mezclamos la lista completa para que el orden sea aleatorio
        Collections.shuffle(recipes);

        // Tomamos solo las primeras 'limit' (en este caso 3)
        // Usamos Math.min por si acaso la categoría tiene menos de 3 recetas
        return recipes.stream()
                .limit(Math.min(recipes.size(), limit))
                .map(recipeService::mapToResponse)
                .toList();
    }
}
