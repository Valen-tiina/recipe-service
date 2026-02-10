package com.zabora.recipe_service.recipe_service.service.IngredientsServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.CreateIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.ResponseIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.ingredientDTO.UpdateIngredient;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Ingredient;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.IngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepo;

    public  IngredientService(IngredientRepository ingredientRepo, MeasurementRepository measureRepo) {
        this.ingredientRepo = ingredientRepo;
    }
    public ResponseIngredient createIngredient(CreateIngredient dto){
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.name());
        ingredient.setImageUrl(dto.imageUrl());
        ingredient = ingredientRepo.save(ingredient);

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl()
        );
    }
    public ResponseIngredient getIngredientById(Integer id) {
        Ingredient ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl()
        );
    }

    public List<ResponseIngredient> getAllIngredients() {
        return ingredientRepo.findAll()
                .stream()
                .map(ing -> new ResponseIngredient(
                        ing.getId(),
                        ing.getName(),
                        ing.getImageUrl()
                ))
                .toList();
    }

    public ResponseIngredient updateIngredient(Integer id, UpdateIngredient dto) {

        Ingredient ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        ingredient.setName(dto.name());
        ingredient.setImageUrl(dto.imageUrl());

        ingredient = ingredientRepo.save(ingredient);

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl());
    }

    public void deleteIngredient(Integer id) {
        Ingredient ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        ingredientRepo.delete(ingredient);
    }

}


