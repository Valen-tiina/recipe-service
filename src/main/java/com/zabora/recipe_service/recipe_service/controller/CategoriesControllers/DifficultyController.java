package com.zabora.recipe_service.recipe_service.controller.CategoriesControllers;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.service.CategoriesServices.DifficultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/difficulties")
public class DifficultyController {

    @Autowired
    private DifficultyService difficultyService;

    @GetMapping
    public ResponseEntity<List<Difficulty>> getAllDifficulties() {

        List<Difficulty> difficulties = difficultyService.findAll();

        return ResponseEntity.ok(difficulties);
    }
}