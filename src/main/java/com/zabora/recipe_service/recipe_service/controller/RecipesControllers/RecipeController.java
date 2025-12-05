package com.zabora.recipe_service.recipe_service.controller.RecipesControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.RecipeService;
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

}
