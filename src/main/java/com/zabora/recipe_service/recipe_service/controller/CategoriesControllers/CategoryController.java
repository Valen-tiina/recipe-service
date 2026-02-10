package com.zabora.recipe_service.recipe_service.controller.CategoriesControllers;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.service.CategoriesServices.CategoryService;
import com.zabora.recipe_service.recipe_service.service.RecipesServices.RecipeService;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



    @RestController
    @RequestMapping("/categories")
    public class CategoryController {

        @Autowired
        private CategoryService categoryService;
        @Autowired
        private RecipeService recipeService;



}
