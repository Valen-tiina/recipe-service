package com.zabora.recipe_service.recipe_service.repository.CategoriesRepository;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DifficultyRepository extends JpaRepository<Difficulty, Integer> {

}
