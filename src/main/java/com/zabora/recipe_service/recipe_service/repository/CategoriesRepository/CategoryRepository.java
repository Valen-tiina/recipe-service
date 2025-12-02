package com.zabora.recipe_service.recipe_service.repository.CategoriesRepository;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
