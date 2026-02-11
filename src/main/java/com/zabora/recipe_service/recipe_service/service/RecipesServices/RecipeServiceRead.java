package com.zabora.recipe_service.recipe_service.service.RecipesServices;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
                .map(recipeService::mapToResponse) // ✅ Usa el método de RecipeService
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
    public List<ResponseRecipes> searchRecipesByIngredient(String ingredient, String role) {
        var recipes = recipeRepository.findByIngredientNameContaining(ingredient);

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

    // daily recipes 3 per day
    public Map<String, List<ResponseRecipes>> getRecipesOfTheDay() {
        var recipesOfDay = new HashMap<String, List<ResponseRecipes>>();

        recipesOfDay.put("breakfast", getBreakfastRecipes());
        recipesOfDay.put("lunch", getLunchRecipes());
        recipesOfDay.put("dinner", getDinnerRecipes());

        return recipesOfDay;
    }
}
