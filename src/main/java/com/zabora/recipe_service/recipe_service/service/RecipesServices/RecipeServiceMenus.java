package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecipeServiceMenus {

    private final RecipeRepository recipeRepository;
    private final RecipeServiceSummaries recipeServiceSummaries;

    private static final Integer BREAKFAST_CATEGORY_ID = 1;
    private static final Integer LUNCH_CATEGORY_ID     = 2;
    private static final Integer DINNER_CATEGORY_ID    = 3;
    private static final Integer SNACK_CATEGORY_ID     = 4;

    public RecipeServiceMenus(RecipeRepository recipeRepository, RecipeServiceSummaries recipeServiceSummaries) {
        this.recipeRepository = recipeRepository;
        this.recipeServiceSummaries = recipeServiceSummaries;
    }


    public List<RecipeResponseSummary> getRecipesOfTheDay() {
        List<RecipeResponseSummary> menu = new ArrayList<>();
        menu.addAll(getBreakfastRecipes());
        menu.addAll(getLunchRecipes());
        menu.addAll(getDinnerRecipes());
        return menu;
    }

    public List<RecipeResponseSummary> getBreakfastRecipes() {
        return getRandomRecipesByCategory(BREAKFAST_CATEGORY_ID, 3);
    }

    public List<RecipeResponseSummary> getLunchRecipes() {
        return getRandomRecipesByCategory(LUNCH_CATEGORY_ID, 3);
    }

    public List<RecipeResponseSummary> getDinnerRecipes() {
        return getRandomRecipesByCategory(DINNER_CATEGORY_ID, 3);
    }

    public List<RecipeResponseSummary> getSnacksRecipes() {
        return getRandomRecipesByCategory(SNACK_CATEGORY_ID, 4);
    }


    public List<RecipeResponseSummary> getRandomRecipesByCategory(Integer categoryId, int limit) {
        var recipes = recipeRepository.findByCategoryId(categoryId);
        if (recipes.isEmpty()) return Collections.emptyList();

        Collections.shuffle(recipes);
        return recipeServiceSummaries.mapToSummary(
                recipes.stream()
                        .limit(Math.min(recipes.size(), limit))
                        .toList()
        );
    }
}