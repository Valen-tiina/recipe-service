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
    private boolean isAdmin(String role) {
        return "ROLE_ADMIN".equals(role);
    }

    private ResponseEntity<Object> forbidden() {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 403);
        error.put("message", "No tienes permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @PostMapping
    public ResponseEntity<Object> createRecipe(
            @RequestBody CreateRecipe dto,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        if (!isAdmin(role)) return forbidden();
        return ResponseEntity.ok(createRecipe.createRecipe(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRecipe(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRecipes dto,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        if (!isAdmin(role)) return forbidden();
        return ResponseEntity.ok(recipeServiceUpdate.updateRecipe(id, dto));
    }

    @GetMapping("/recipeSummary")
    public ResponseEntity<List<RecipeResponseSummary>> getRecipeSummary(
            @RequestHeader(value = "X-User-Id", required = false) Long userId) {

        if (userId == null) {
            return ResponseEntity.ok(recipeServiceSummaries.getRecipeSummary());
        }
        return ResponseEntity.ok(recipeServiceSummaries.getRecipeSummaryByUser(userId));
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
    public ResponseEntity<Object> getAllRecipes(
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {

        List<ResponseRecipes> recipes = recipeServiceRead.getAllRecipes(role);

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 404);
            error.put("message", "Parece que no tenemos recetas en este momento, inténtalo de nuevo más tarde");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchRecipesByTitle(
            @RequestParam String title,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {

        List<RecipeResponseSummary> recipes = recipeServiceRead.searchRecipesByTitle(title, role);

        if (recipes == null || recipes.isEmpty()) {
            List<RecipeResponseSummary> suggestions = recipeServiceRead.getRandomRecipes(role, 5);

            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            response.put("message", "Parece que no encontramos recetas con ese nombre... ¡pero tenemos estas opciones para ti!");
            response.put("suggestions", suggestions);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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




    @GetMapping("/todayMeal")
    public ResponseEntity<List<RecipeResponseSummary>> getRecipesOfTheDay() {

        return ResponseEntity.ok(recipeServiceMenus.getRecipesOfTheDay());
    }

    @GetMapping("/search/ingredient/multiple")
    public ResponseEntity<Object> searchRecipesByIngredientsMultiple(
            @RequestParam List<String> ingredients,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_USER") String role) {

        if (ingredients == null || ingredients.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", "Debes enviar al menos un ingrediente");
            return ResponseEntity.badRequest().body(error);
        }

        List<RecipeResponseSummary> recipes = recipeServiceRead
                .searchRecipesByIngredientsMultiple(ingredients, role);

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 404);
            error.put("message", "No se encuentran recetas con esos ingredientes");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(
            @PathVariable Integer id,
            @RequestHeader(value = "X-User-Role", defaultValue = "") String role) {

        if (!isAdmin(role)) return forbidden();
        recipeServiceDelete.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
