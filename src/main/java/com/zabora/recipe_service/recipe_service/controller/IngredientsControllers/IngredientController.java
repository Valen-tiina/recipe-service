package com.zabora.recipe_service.recipe_service.controller.IngredientsControllers;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.CreateIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.UpdateIngredient;
import com.zabora.recipe_service.recipe_service.service.IngredientsServices.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @PostMapping
    public ResponseEntity<ResponseIngredient> createIngredient(@RequestBody CreateIngredient dto) {
        return ResponseEntity.ok(ingredientService.createIngredient(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseIngredient> getIngredientById(@PathVariable Integer id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseIngredient>> getAllIngredients() {
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
