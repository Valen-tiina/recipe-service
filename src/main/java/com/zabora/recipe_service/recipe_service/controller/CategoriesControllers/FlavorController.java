package com.zabora.recipe_service.recipe_service.controller.CategoriesControllers;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.service.CategoriesServices.CategoryService;
import com.zabora.recipe_service.recipe_service.service.CategoriesServices.FlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flavors")
public class FlavorController {
    @Autowired
    private FlavorService flavorService;

    @GetMapping
    public ResponseEntity<List<Flavor>> getAllFlavors() {
        List<Flavor> flavors = flavorService.findAll();
        return ResponseEntity.ok(flavors);
    }
}
