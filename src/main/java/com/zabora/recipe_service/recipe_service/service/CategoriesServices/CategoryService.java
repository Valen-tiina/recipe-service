package com.zabora.recipe_service.recipe_service.service.CategoriesServices;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }


}