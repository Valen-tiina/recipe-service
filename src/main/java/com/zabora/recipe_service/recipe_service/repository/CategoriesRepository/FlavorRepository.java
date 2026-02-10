package com.zabora.recipe_service.recipe_service.repository.CategoriesRepository;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface FlavorRepository extends JpaRepository<Flavor, Integer> {
}
