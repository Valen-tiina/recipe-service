package com.zabora.recipe_service.recipe_service.controller.IngredientsControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import com.zabora.recipe_service.recipe_service.service.CategoriesServices.CategoryService;
import com.zabora.recipe_service.recipe_service.service.IngredientsServices.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitControllers {
    private final UnitService unitService;
    @Autowired
    public UnitControllers(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<List<UnitResponse>> getAll() {
        return ResponseEntity.ok(unitService.findAll());
    }
}
