package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeName;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;

@Service
public class RecipeServiceRead {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;
    private final RecipeServiceSummaries recipeSummaryService;

    public RecipeServiceRead(RecipeService recipeService,
                             RecipeRepository recipeRepository,
                             RecipeServiceSummaries recipeSummaryService) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
        this.recipeSummaryService = recipeSummaryService;
    }



    private int getRecipeLimitByRole(String role) {
        return switch (role) {
            case "ROLE_ADMIN", "ROLE_PREMIUM" -> Integer.MAX_VALUE;
            case "ROLE_USER"  -> 30;
            case "ROLE_GUEST" -> 4;
            default           -> 4;
        };
    }

    private int getIngredientLimitByRole(String role) {
        return switch (role) {
            case "ROLE_ADMIN", "ROLE_PREMIUM" -> 20;
            case "ROLE_USER"    -> 7;
            default             -> 0;
        };
    }

    public List<RecipeResponseSummary> getRandomRecipes(String role, int count) {
        List<Recipe> all = recipeRepository.findAll();
        Collections.shuffle(all);
        int limit = Math.min(count, getRecipeLimitByRole(role));
        return recipeSummaryService.mapToSummary(
                all.stream().limit(limit).toList()
        );
    }
    private List<RecipeResponseSummary> applyRoleLimit(List<RecipeResponseSummary> recipes, String role) {
        return recipes.stream()
                .limit(getRecipeLimitByRole(role))
                .toList();
    }



    @Transactional(readOnly = true)
    public List<ResponseRecipes> getAllRecipes(String role, int page, int size) {
        int limit = getRecipeLimitByRole(role);

        Page<Recipe> recipesPage = recipeRepository.findAll(PageRequest.of(page, size));

        return recipesPage.getContent().stream()
                .limit(limit)
                .map(recipeService::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseRecipes getRecipeById(Integer id) {
        Recipe recipe = recipeRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
        return recipeService.mapToResponse(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseSummary> getRecipesByIds(List<Integer> ids) {
        Map<Integer, Recipe> recipesMap = recipeRepository.findAllByIdWithRelations(ids).stream()
                .collect(Collectors.toMap(Recipe::getId, r -> r));

        return recipeSummaryService.mapToSummary(
                ids.stream()
                        .map(recipesMap::get)
                        .filter(r -> r != null)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<String> getRecipeNamesByIds(List<Integer> ids) {
        return recipeRepository.findRecipeNamesByIds(ids).stream()
                .map(RecipeName::title)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<RecipeResponseSummary> searchRecipesByTitle(String title, String role) {
        return applyRoleLimit(
                recipeSummaryService.mapToSummary(
                        recipeRepository.findByTitleContainingIgnoreCase(title)
                ),
                role
        );
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseSummary> searchRecipesByIngredients(List<String> ingredients, String role) {
        int max = getIngredientLimitByRole(role);
        if (max == 0) return List.of();

        List<String> limited = ingredients.stream()
                .limit(max)
                .map(String::toLowerCase)
                .toList();

        return applyRoleLimit(
                recipeSummaryService.mapToSummary(
                        recipeRepository.findByIngredientsNameIn(limited)
                ),
                role
        );
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseSummary> searchRecipesByIngredientsMultiple(List<String> ingredients, String role) {
        int max = getIngredientLimitByRole(role);
        if (max == 0) return List.of();

        List<String> limited = ingredients.stream()
                .limit(max)
                .map(String::toLowerCase)
                .toList();

        List<Recipe> sorted = recipeRepository.findByIngredientsNameIn(limited).stream()
                .map(r -> Map.entry(r, (int) r.getIngredients().stream()
                        .map(ri -> ri.getIngredient().getName().toLowerCase())
                        .filter(limited::contains)
                        .count()))
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        return applyRoleLimit(recipeSummaryService.mapToSummary(sorted), role);
    }
}