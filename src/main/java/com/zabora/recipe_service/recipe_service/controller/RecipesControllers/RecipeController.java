package com.zabora.recipe_service.recipe_service.controller.RecipesControllers;
import java.util.Collections;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.UpdateRecipes;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeServiceCreate createRecipe;
    private final RecipeServiceDelete recipeServiceDelete;
    private final RecipeServiceUpdate recipeServiceUpdate;
    private final RecipeServiceRead recipeServiceRead;
    private final RecipeServiceSummaries recipeServiceSummaries;
    private final RecipeServiceMenus recipeServiceMenus;

    public RecipeController(RecipeService recipeService,
            RecipeServiceCreate createRecipe,
            RecipeServiceDelete recipeServiceDelete,
            RecipeServiceUpdate recipeServiceUpdate,
            RecipeServiceRead recipeServiceRead,
                            RecipeServiceSummaries recipeServiceSummaries,
                            RecipeServiceMenus recipeServiceMenus) {
        this.recipeService = recipeService;
        this.createRecipe = createRecipe;
        this.recipeServiceDelete = recipeServiceDelete;
        this.recipeServiceUpdate = recipeServiceUpdate;
        this.recipeServiceRead = recipeServiceRead;
        this.recipeServiceMenus= recipeServiceMenus;
        this.recipeServiceSummaries=recipeServiceSummaries;
    }

    @PostMapping
    public ResponseEntity<ResponseRecipes> createRecipe(@RequestBody CreateRecipe dto) {
        ResponseRecipes createdRecipe = createRecipe.createRecipe(dto);
        return ResponseEntity.ok(createdRecipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRecipes> updateRecipe(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRecipes dto
    ) {
        return ResponseEntity.ok(recipeServiceUpdate.updateRecipe(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRecipes> getRecipeById(@PathVariable Integer id) {
        return ResponseEntity.ok(recipeServiceRead.getRecipeById(id));
    }

    @GetMapping("/multiple")
    public ResponseEntity<List<RecipeResponseSummary>> getRecipesByIds(@RequestParam List<Integer> ids) {
        return ResponseEntity.ok(recipeServiceRead.getRecipesByIds(ids));
    }

    @GetMapping
    public ResponseEntity<Object> getAllRecipes() {
        List<ResponseRecipes> recipes = recipeServiceRead.getAllRecipes("ROLE_ADMIN"); // ← Sin límite

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "Parece que no tenemos recetas en este momento, inténtalo de nuevo más tarde");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchRecipesByTitle(
            @RequestParam String title,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {

        List<RecipeResponseSummary> recipes = recipeServiceRead.searchRecipesByTitle(title, role);

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "No se encontraron recetas con el título: " + title);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search/ingredient")
    public ResponseEntity<Object> searchRecipesByIngredient(
            @RequestParam String ingredient,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_USER") String role) {

        List<RecipeResponseSummary> recipes = recipeServiceRead
                .searchRecipesByIngredients(List.of(ingredient), role);

        if (recipes == null || recipes.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(recipes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Integer id) {
        recipeServiceDelete.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    // Estos 4 métodos NO tienen límite por rol
    @GetMapping("/todayMeal")
    public ResponseEntity<List<RecipeResponseSummary>> getRecipesOfTheDay() {
        // Eliminamos el Map<> del tipo de retorno
        return ResponseEntity.ok(recipeServiceMenus.getRecipesOfTheDay());
    }

    @GetMapping("/search/ingredient/multiple")
    public ResponseEntity<Object> searchRecipesByIngredientsMultiple(
            @RequestParam List<String> ingredients,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_USER") String role) {

        if (ingredients == null || ingredients.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 400);
            errorResponse.put("message", "Debes enviar al menos un ingrediente");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        List<RecipeResponseSummary> recipes = recipeServiceRead
                .searchRecipesByIngredientsMultiple(ingredients, role);

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "No se encuentran recetas con esos ingredientes");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getRecipeNamesByIds(
            @RequestParam List<Integer> ids
    ) {
        return ResponseEntity.ok(
                recipeServiceRead.getRecipeNamesByIds(ids)
        );
    }

    @GetMapping("/breakfast")
    public ResponseEntity<List<RecipeResponseSummary>> getBreakfastRecipes() {
        return ResponseEntity.ok(recipeServiceMenus.getBreakfastRecipes());
    }

    @GetMapping("/lunch")
    public ResponseEntity<List<RecipeResponseSummary>> getLunchRecipes() {
        return ResponseEntity.ok(recipeServiceMenus.getLunchRecipes());
    }

    @GetMapping("/dinner")
    public ResponseEntity<List<RecipeResponseSummary>> getDinnerRecipes() {
        return ResponseEntity.ok(recipeServiceMenus.getDinnerRecipes());
    }

    @GetMapping("/snack")
    public ResponseEntity<List<RecipeResponseSummary>> getSnackRecipes() {
        return ResponseEntity.ok(recipeServiceMenus.getSnacksRecipes());
    }

    @GetMapping("/recipeSummary")
    public ResponseEntity<List<RecipeResponseSummary>> getRecipeSummary(
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {

        if(userId == null){
            return ResponseEntity.ok(recipeServiceSummaries.getRecipeSummary());
        }

        return ResponseEntity.ok(recipeServiceSummaries.getRecipeSummaryByUser(userId));
    }

}
