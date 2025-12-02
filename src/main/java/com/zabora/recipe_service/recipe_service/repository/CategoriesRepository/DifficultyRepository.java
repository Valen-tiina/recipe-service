package com.zabora.recipe_service.recipe_service.repository.CategoriesRepository;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {
}
