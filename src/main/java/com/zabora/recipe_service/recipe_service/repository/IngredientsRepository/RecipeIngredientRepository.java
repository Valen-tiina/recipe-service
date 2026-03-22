package com.zabora.recipe_service.recipe_service.repository.IngredientsRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer> {
	@Query("""
			    SELECT ri
			    FROM RecipeIngredient ri
			    JOIN FETCH ri.ingredient i
			    JOIN FETCH ri.unit u
			    JOIN FETCH u.measurement
			    WHERE ri.recipe.id IN :recipeIds
			""")
	List<RecipeIngredient> findByRecipeIds(List<Integer> recipeIds);
}
