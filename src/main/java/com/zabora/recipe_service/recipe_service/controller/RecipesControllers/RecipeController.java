package com.zabora.recipe_service.recipe_service.controller.RecipesControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.UpdateRecipes;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRecipes> updateRecipe(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRecipes dto
    ) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<ResponseRecipes>> getAllRecipes(
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {
        List<ResponseRecipes> recipes = recipeService.getAllRecipes(role);
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

    @GetMapping("/search/")
    public ResponseEntity<List<ResponseRecipes>> searchRecipesByTitle(
            @RequestParam String title,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {
        return ResponseEntity.ok(recipeService.searchRecipesByTitle(title, role));
    }

    @GetMapping("/search/ingredient/")
    public ResponseEntity<List<ResponseRecipes>> searchRecipesByIngredient(
            @RequestParam String ingredient,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role
    ) {
        return ResponseEntity.ok(recipeService.searchRecipesByIngredient(ingredient, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    // Estos 4 métodos NO tienen límite por rol
    @GetMapping("/todayMeal")
    public ResponseEntity<Map<String, List<ResponseRecipes>>> getRecipesOfTheDay() {
        return ResponseEntity.ok(recipeService.getRecipesOfTheDay());
    }

    @GetMapping("/breakfast")
    public ResponseEntity<List<ResponseRecipes>> getBreakfastRecipes() {
        return ResponseEntity.ok(recipeService.getBreakfastRecipes());
    }

    @GetMapping("/lunch")
    public ResponseEntity<List<ResponseRecipes>> getLunchRecipes() {
        return ResponseEntity.ok(recipeService.getLunchRecipes());
    }

    @GetMapping("/dinner")
    public ResponseEntity<List<ResponseRecipes>> getDinnerRecipes() {
        return ResponseEntity.ok(recipeService.getDinnerRecipes());
    }
}