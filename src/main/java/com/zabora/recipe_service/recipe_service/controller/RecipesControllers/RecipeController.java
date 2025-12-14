package com.zabora.recipe_service.recipe_service.controller.RecipesControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<ResponseRecipes> createRecipe(@RequestBody CreateRecipe dto) {
        ResponseRecipes createdRecipe = recipeService.createRecipe(dto);
        return ResponseEntity.ok(createdRecipe);
    }

    @GetMapping
    public ResponseEntity<List<ResponseRecipes>> getAllRecipes() {
        List<ResponseRecipes> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRecipes> getRecipeById(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @GetMapping("/multiple")
    public ResponseEntity<List<ResponseRecipes>> getRecipesByIds(@RequestParam List<Integer> ids) {
        List<ResponseRecipes> recipes = recipeService.getRecipesByIds(ids);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseRecipes>> searchRecipesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(recipeService.searchRecipesByTitle(title));
    }

    @GetMapping("/search/ingredient")
    public ResponseEntity<List<ResponseRecipes>> searchRecipesByIngredient(
            @RequestParam String ingredient
    ) {
        return ResponseEntity.ok(recipeService.searchRecipesByIngredient(ingredient));
    }

}
