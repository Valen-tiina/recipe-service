package com.zabora.recipe_service.recipe_service.controller.RecipesControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.CreateRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.UpdateRecipes;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeServiceCreate createRecipe;
    private final RecipeServiceDelete recipeServiceDelete;
    private final RecipeServiceUpdate recipeServiceUpdate;
    private final RecipeServiceRead recipeServiceRead;

    public RecipeController(RecipeService recipeService,
                            RecipeServiceCreate createRecipe,
                            RecipeServiceDelete recipeServiceDelete,
                            RecipeServiceUpdate recipeServiceUpdate,
                            RecipeServiceRead recipeServiceRead) {
        this.recipeService = recipeService;
        this.createRecipe=createRecipe;
        this.recipeServiceDelete=recipeServiceDelete;
        this.recipeServiceUpdate=recipeServiceUpdate;
        this.recipeServiceRead=recipeServiceRead;
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
    public ResponseEntity<List<ResponseRecipes>> getRecipesByIds(@RequestParam List<Integer> ids) {
        List<ResponseRecipes> recipes = recipeServiceRead.getRecipesByIds(ids);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRecipes(
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_GUEST") String role) {
        List<ResponseRecipes> recipes = recipeServiceRead.getAllRecipes(role);
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

        List<ResponseRecipes> recipes = recipeServiceRead.searchRecipesByTitle(title, role);

        if (recipes == null || recipes.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "No se encontraron recetas con el título: " + title);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(recipes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRecipe(
            @PathVariable Integer id,
            @RequestHeader(value = "X-User-Role", defaultValue = "ROLE_USER") String role
    ) {
        // Validación manual de seguridad
        if (!"ROLE_ADMIN".equals(role)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Prohibido");
            error.put("message", "No tienes permisos de administrador para realizar esta acción.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        recipeServiceDelete.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
    

    // Estos 4 métodos NO tienen límite por rol
    @GetMapping("/todayMeal")
    public ResponseEntity<List<ResponseRecipes>> getRecipesOfTheDay() {
        // Eliminamos el Map<> del tipo de retorno
        return ResponseEntity.ok(recipeServiceRead.getRecipesOfTheDay());
    }

    @GetMapping("/breakfast")
    public ResponseEntity<List<ResponseRecipes>> getBreakfastRecipes() {
        return ResponseEntity.ok(recipeServiceRead.getBreakfastRecipes());
    }

    @GetMapping("/lunch")
    public ResponseEntity<List<ResponseRecipes>> getLunchRecipes() {
        return ResponseEntity.ok(recipeServiceRead.getLunchRecipes());
    }

    @GetMapping("/dinner")
    public ResponseEntity<List<ResponseRecipes>> getDinnerRecipes() {
        return ResponseEntity.ok(recipeServiceRead.getDinnerRecipes());
    }

    @GetMapping("/snack")
    public ResponseEntity<List<ResponseRecipes>> getSnackRecipes(){
        return ResponseEntity.ok(recipeServiceRead.getSnacksRecipes());
    }
}