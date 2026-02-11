package com.zabora.recipe_service.recipe_service.service.CategoriesServices;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.DifficultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DifficultyService {
    private final DifficultyRepository difficultyRepo;

    public DifficultyService(DifficultyRepository difficultyRepo) {
        this.difficultyRepo = difficultyRepo;
    }

    public List<Difficulty> findAll() {
        return difficultyRepo.findAll();
    }
}
