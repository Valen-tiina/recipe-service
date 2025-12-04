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
    private final MeasurementRepository measureRepo;

    public  IngredientService(IngredientRepository ingredientRepo, MeasurementRepository measureRepo) {
        this.ingredientRepo = ingredientRepo;
        this.measureRepo = measureRepo;
    }
    public ResponseIngredient createIngredient(CreateIngredient dto){
        var measurement = measureRepo.findById(dto.measurementId())
                .orElseThrow(()->new RuntimeException("Medida no encontrada"));

        var ingredient = new Ingredient();
        ingredient.setName(dto.name());
        ingredient.setImageUrl(dto.imageUrl());
        ingredient.setMeasurement(measurement);

        ingredient = ingredientRepo.save(ingredient);

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl(),
                measurement.getName()
        );
    }
    public ResponseIngredient getIngredientById(Integer id) {
        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl(),
                ingredient.getMeasurement().getName()
        );
    }

    public List<ResponseIngredient> getAllIngredients() {
        return ingredientRepo.findAll()
                .stream()
                .map(ing -> new ResponseIngredient(
                        ing.getId(),
                        ing.getName(),
                        ing.getImageUrl(),
                        ing.getMeasurement().getName()
                ))
                .toList();
    }

    public ResponseIngredient updateIngredient(Integer id, UpdateIngredient dto) {

        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        var measurement = measureRepo.findById(dto.measurementId())
                .orElseThrow(() -> new RuntimeException("Medida no encontrada"));

        ingredient.setName(dto.name());
        ingredient.setImageUrl(dto.imageUrl());
        ingredient.setMeasurement(measurement);

        ingredient = ingredientRepo.save(ingredient);

        return new ResponseIngredient(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImageUrl(),
                measurement.getName()
        );
    }

    public void deleteIngredient(Integer id) {
        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        ingredientRepo.delete(ingredient);
    }

}
