package com.zabora.recipe_service.recipe_service.controller.IngredientsControllers;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.CreateIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.UpdateIngredient;
import com.zabora.recipe_service.recipe_service.service.IngredientsServices.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;



    @PostMapping
    public ResponseEntity<ResponseIngredient> createIngredient(@Valid @RequestBody CreateIngredient dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ingredientService.createIngredient(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseIngredient> getIngredientById(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping
    public ResponseEntity<Object> getAllIngredients() {
        List<ResponseIngredient> ingredient = ingredientService.getAllIngredients();
        if (ingredient == null || ingredient.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 404);
            errorResponse.put("message", "Parece que no tenemos ingredientes en este momento, inténtalo de nuevo más tarde");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        return ResponseEntity.ok(ingredientService.getAllIngredients());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseIngredient> updateIngredient(
            @PathVariable Integer id,
            @RequestBody UpdateIngredient dto
    ) {
        return ResponseEntity.ok(ingredientService.updateIngredient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Integer id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }
}
